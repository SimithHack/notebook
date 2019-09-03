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

# 与Spring boot
# 与spring cloud