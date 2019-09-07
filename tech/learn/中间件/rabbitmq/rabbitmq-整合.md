# 与Spring AMQP
1. SimpleMessageListenerContainer
2. MessageListenerAdapter
3. MessageConverter
4. RabbitAdmin
> autoStartup必须设置为true，否则Spring容器不会加载RabbitAdmin类  
RabbitAdmin的作用就是从容器里获取Exchange，Binding，RoutingKey和Queue的声明  
内部是使用RabbitTemplate来做的这些操作
```java
@Bean
public RabbitAdmin rabbitAdmin(ConnectionFactory  conn){
    RabbitAdmin admin = new RabbitAdmin(conn);
    admin.setAutoStartup(true);
    return admin;
}
@Bean
public ConnectionFactory connectionFactory(){
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setAddresses("master:5672");
    connectionFactory.setUsername("admin");
    connectionFactory.setPassword("admin");
    connectionFactory.setVirtualHost("/");
    connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
    return connectionFactory;
}
```
完整测试代码
```java
@Test
public void test(){
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(MqConfig.class);
    ctx.refresh();
    for(String name : ctx.getBeanDefinitionNames()){
        System.out.println(name);
    }
    RabbitAdmin rabbitAdmin = (RabbitAdmin) ctx.getBean("rabbitAdmin");

    rabbitAdmin.declareExchange(new DirectExchange("test.direct.exchange", false, false));
    rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
    rabbitAdmin.declareBinding(
        new Binding(
            "test.direct.queue",
            Binding.DestinationType.QUEUE,
            "test.direct.exchange",
            "test.direct.binding",
            Collections.emptyMap()
        )
    );
    rabbitAdmin.getRabbitTemplate()
        .send(
            "test.direct.exchange",
            "test.direct.binding",
            new Message(
                    "hello world".getBytes(),
                    new MessageProperties()
            )
        );
}
```
也可以这样建立绑定关系
```java
rabbitAdmin.declareBinding(
        BindingBuilder
        .bind(new Queue("xxx", false))
        .to(new TopicExchange("mtopic", false, false))
        .with("user.#")
);
```
其他API
```java
//清空队列的数据
rabbitAdmin.purgeQueue("queue.name", false);
```
5. maven依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```
6. 底层原理
```text
简单的用文字描述来说，我们将rabbitmq的服务器相关连接信息提供后（ConnectionFactory）RabbitAdmin的类声明如下：
    public class RabbitAdmin implements AmqpAdmin, ApplicationContextAware, ApplicationEventPublisherAware, BeanNameAware,  InitializingBean
可以发现，它实现了InitializingBean，说明它在bean的生命周期方法里干了什么事情，我们找到它的afterPropertiesSet方法
    if (this.running || !this.autoStartup) {
        return;
    }
我们发现，如果autoStartup不设置为true，Spring容器就不会初始化
接着配置了重试策略
    if (this.retryTemplate == null && !this.retryDisabled) {
        this.retryTemplate = new RetryTemplate();
        this.retryTemplate.setRetryPolicy(new SimpleRetryPolicy(DECLARE_MAX_ATTEMPTS));
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(DECLARE_INITIAL_RETRY_INTERVAL);
        backOffPolicy.setMultiplier(DECLARE_RETRY_MULTIPLIER);
        backOffPolicy.setMaxInterval(DECLARE_MAX_RETRY_INTERVAL);
        this.retryTemplate.setBackOffPolicy(backOffPolicy);
    }
然后调用了org.springframework.amqp.rabbit.core.RabbitAdmin#initialize方法
在这个方法里，spring将容器中所有的exchange，bingdings，queue 都找到，然后放到集合里
    Collection<Exchange> contextExchanges = new LinkedList<Exchange>(
				this.applicationContext.getBeansOfType(Exchange.class).values());
    Collection<Queue> contextQueues = new LinkedList<Queue>(
            this.applicationContext.getBeansOfType(Queue.class).values());
    Collection<Binding> contextBindings = new LinkedList<Binding>(
            this.applicationContext.getBeansOfType(Binding.class).values());
后来使用RabbitTemplate方法进行绑定
    this.rabbitTemplate.execute(channel -> {
        declareExchanges(channel, exchanges.toArray(new Exchange[exchanges.size()]));
        declareQueues(channel, queues.toArray(new Queue[queues.size()]));
        declareBindings(channel, bindings.toArray(new Binding[bindings.size()]));
        return null;
    });
```
## 消息模板-RabbitTemplate
> 提供了丰富的发送消息的方法，包括可靠性投递消息的方法，回调监听消息接口ConfirmCallback，返回值确认接口
ReturnCallback等等，同样我们需要进行注入到Spring容器中，然后直接使用。

## SimpleMessageListenerContainer
> 简单消息监听容器  
+ 可以进行很多设置，对于消费者的配置项，这个类都可以满足  
+ 监听队列（多个队列），自动启动，自动声明功能
+ 设置事务特性，事务管理器，事务属性，事务容量（并发），是否开启事务，回滚消息等。
+ 设置消费者的数量，最大最小数量，批量消费
+ 消息的接收模式，自动确认还是手动确认，是否重回队列，一场捕获handler函数
+ 设置消费者标签生成策略，是否独占模式，消费者属性等
+ 设置具体的监听器，消息转化器
+ SimpleMessageListenerContainer可以在运行时动态设置，比如动态修改消费者数量的大小，接收消息的模式等

## MessageListenerAdapter 消息监听适配器
+ defaultListenerMethod默认监听方法名称：用于设置监听方法的名称
+ Delegate委托对象：实际真是的委托对象，用于处理消息
+ queueOrTagToMethodName 队列标识与方法名称组合的集合
    - 可以一一进行队列与方法名称的匹配
    - 队列和方法名称绑定，即指定队列里的消息会被绑定的方法所接受处理

```java
SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
container.setConnectionFactory(conn);
//底层，默认的监听方法是 handleMessage 方法，在委托类MessageDelegate必须提供名称相同的，来绑定
MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//可以通过这种方法来改默认方法的名称
adapter.setDefaultListenerMethod("myName");
//可以设置MessageConverter来定义，参数类型的行为，实现下面的方法就可以
adapter.setMessageConverter(new MessageConverter() {
    //把Java对象转换为Message
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(object.toString().getBytes(), messageProperties);
    }
    //把Message对象转换为我们的Java对象，这个返回的是什么类型的对象，就调用什么类型的参数方法
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return message.getBody();
    }
});
//还可以指定队列和方法名绑定
Map<String, String> queueToMethodName = new HashMap<>();
queueToMethodName.put("queue001", "method1");
queueToMethodName.put("queue002", "method2");
queueToMethodName.put("queue003", "method3");
adapter.setQueueOrTagToMethodName(queueToMethodName);

container.setMessageListener(adapter);
```

# 与Spring boot
# 与spring cloud