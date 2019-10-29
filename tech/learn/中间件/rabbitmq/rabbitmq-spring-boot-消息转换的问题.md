# spring-boot 整合rabbitmq 消息转换的问题
问题描述
> 工程使用spring-boot-amqp注解的功能，监听rabbitmq的消息，代码如下

## mq配置
```java
/**
 * 对接FMS系统 MQ配置
 */
@Configuration
public class FmsRabbitMqConfig {

    @Autowired
    private RabbitMqConfig.MqProperties mqProperties;

    /**
     * FMS mq配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "mq.fms")
    public FmsMqProperties fmsProperties(){
        return new FmsMqProperties();
    }

    /**
     * FMS MQ配置
     */
    @Data
    class FmsMqProperties {
        private String virtualHost;
        private String exchange;
        private String routingKey;
        private String queue;
    }

    /**
     * MQ连接工厂
     * @return
     */
    @Bean
    public ConnectionFactory fmsConnectionFactory(){
        FmsMqProperties fms = fmsProperties();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(mqProperties.getAddresses());
        connectionFactory.setUsername(mqProperties.getUsername());
        connectionFactory.setPassword(mqProperties.getPassword());
        connectionFactory.setVirtualHost(fms.getVirtualHost());
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        return connectionFactory;
    }

    /**
     * rabbitmq admin
     * @return
     */
    @Bean
    public RabbitAdmin fmsRabbitAdmin(){
        RabbitAdmin admin = new RabbitAdmin(fmsConnectionFactory());
        admin.setAutoStartup(true);
        return admin;
    }

    /**
     * RabbitTemplate
     * @return
     */
    @Bean
    public RabbitTemplate fmsMqTemplate(){
        return fmsRabbitAdmin().getRabbitTemplate();
    }

    /**
     * 消息监听容器
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory fmsListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory sf = new SimpleRabbitListenerContainerFactory();
        sf.setConnectionFactory(fmsConnectionFactory());
        sf.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return sf;
    }
}

```
消息监听配置如下
```java
public class FmsMqListener {
    @RabbitListener(
            admin = "fmsRabbitAdmin",
            containerFactory = "fmsListenerContainerFactory",
            bindings = {
                    @QueueBinding(
                            value = @Queue(
                                    name="${mq.fms.queue}",
                                    durable = "true"
                            ),
                            exchange = @Exchange(
                                    name="${mq.fms.exchange}",
                                    type= ExchangeTypes.FANOUT
                            ),
                            key = "${mq.fms.routing-key}"
                    )
            }
    )
    public void onMessage(Message msg, @Payload Map record){
        log.info("incoming msg from fms {}", new Gson().toJson(record));
    }
}
```
运行结果
```sh
Method [public void com.juma.risk.core.mq.FmsMqListener.onMessage(org.springframework.amqp.core.Message,java.util.Map)]
Bean [com.juma.risk.core.mq.FmsMqListener@488c6c98]
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:199) ~[spring-rabbit-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.onMessage(MessagingMessageListenerAdapter.java:129) ~[spring-rabbit-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.doInvokeListener(AbstractMessageListenerContainer.java:1542) [spring-rabbit-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.actualInvokeListener(AbstractMessageListenerContainer.java:1468) [spring-rabbit-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.invokeListener(AbstractMessageListenerContainer.java:1456) [spring-rabbit-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.doExecuteListener(AbstractMessageListenerContainer.java:1451) [spring-rabbit-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.executeListener(AbstractMessageListenerContainer.java:1400) [spring-rabbit-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	... 6 common frames omitted
Caused by: org.springframework.messaging.converter.MessageConversionException: Cannot convert from [[B] to [java.util.Map] for GenericMessage [payload=byte[29], headers={amqp_receivedDeliveryMode=PERSISTENT, amqp_receivedExchange=temp, amqp_deliveryTag=1, amqp_consumerQueue=kkk, amqp_redelivered=false, id=f12e4a9e-7a5f-86bd-9b83-b68d67de7863, amqp_consumerTag=amq.ctag-lOQTKRdI-XNT-_MTSkJ9Kg, timestamp=1572334758751}]
	at org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver.resolveArgument(PayloadArgumentResolver.java:144) ~[spring-messaging-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:117) ~[spring-messaging-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.messaging.handler.invocation.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:148) ~[spring-messaging-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.messaging.handler.invocation.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:116) ~[spring-messaging-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.HandlerAdapter.invoke(HandlerAdapter.java:50) ~[spring-rabbit-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:196) ~[spring-rabbit-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	... 12 common frames omitted
```

## 原因
```java
//在DefaultMessageHandlerMethodFactory里默认使用的是GenericMessageConverter，而我们应该使用MappingJacksonMessageConverter
@Override
public void afterPropertiesSet() {
    if (this.messageConverter == null) {
        this.messageConverter = new GenericMessageConverter(this.conversionService);
    }
    if (this.argumentResolvers.getResolvers().isEmpty()) {
        this.argumentResolvers.addResolvers(initArgumentResolvers());
    }
}
```
## 解决办法
自定义RabbitListenerConfigurer覆盖DefaultMessageHandlerMethodFactory，设置MappingJackson2MessageConverter
```java
@Configuration
public class FmsRabbitMqConfig implements RabbitListenerConfigurer {
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(new MappingJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }
}
```
