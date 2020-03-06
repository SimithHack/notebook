---
标题: 平安面试总结
时间: 2019-11-01
---

<!-- TOC -->

- [1. rabbitmq-消息拒收处理过程（spring-amqp)](#1-rabbitmq-消息拒收处理过程spring-amqp)
- [2. 线程池的执行流程](#2-线程池的执行流程)

<!-- /TOC -->

# 1. rabbitmq-消息拒收处理过程（spring-amqp)
> 就昨天面试官的问题 “如果消息队列中有一条消息被拒收（异常或者业务逻辑处理）此条消息的处理流程怎样?”

+ 我的回答
```
消息达到重试次数，会进入死信队列。如果一直nack，会将此条消息放回队尾，从而重复消费。
```

+ 考官答案
```
此条消息会一直循环，卡住后面的消息消费
```

+ spring-amqp官方参考
```java
/**
     * Reject one or several received messages.
     *
     * Supply the <code>deliveryTag</code> from the {@link com.rabbitmq.client.AMQP.Basic.GetOk}
     * or {@link com.rabbitmq.client.AMQP.Basic.GetOk} method containing the message to be rejected.
     * @see com.rabbitmq.client.AMQP.Basic.Nack
     * @param deliveryTag the tag from the received {@link com.rabbitmq.client.AMQP.Basic.GetOk} or {@link com.rabbitmq.client.AMQP.Basic.Deliver}
     * @param multiple true to reject all messages up to and including
     * the supplied delivery tag; false to reject just the supplied
     * delivery tag.
     * @param requeue true if the rejected message(s) should be requeued rather
     * than discarded/dead-lettered
     * @throws java.io.IOException if an error is encountered
     */
    void basicNack(long deliveryTag, boolean multiple, boolean requeue)
            throws IOException;
```
提到，如果在回复nck的时候，指定multiple参数为true就会阻止其余的消息接受，如果发为false在纸上拒绝当前的delivery-tag的消息  
还有一个参数 requeue，如果为true表示可以重回队列，消费而不是直接去死信队列

+ 代码验证
1）将响应修改为手动，因为自动响应会有很多问题
```java
/**
    * 消息监听容器
    * @return
    */
@Bean
public SimpleRabbitListenerContainerFactory listenerContainerFactory(ConnectionFactory connectionFactory){
    SimpleRabbitListenerContainerFactory sf = new SimpleRabbitListenerContainerFactory();
    sf.setConnectionFactory(connectionFactory);
    sf.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    return sf;
}
```
2）消息监听代码
> 仅为了测试目的，使用直连exchange
```java
@RabbitListener(
        admin = "rabbitAdmin",
        containerFactory = "listenerContainerFactory",
        bindings = {
                @QueueBinding(
                        value = @Queue(
                                name="test",
                                durable = "true"
                        ),
                        exchange = @Exchange(
                                name="test",
                                type= ExchangeTypes.DIRECT
                        ),
                        key = "test"
                )
        }
)
public void onMessage(Channel channel, Message message, @Payload Record record) throws Exception {
        log.info("收到消息={}", record.getIndex());
        Long delveryTag = (Long)message.getHeaders().get("amqp_deliveryTag");
        if(record.getIndex()==5){
            TimeUnit.SECONDS.sleep(2);
            channel.basicNack(delveryTag, false, true);
        }else{
            channel.basicAck(delveryTag, false);
        }
    }
```
3）消息发送
```java
public void sendMsg(){
    for(int i=0; i<100; i++){
        try {
            TimeUnit.SECONDS.sleep(1);
            template.convertAndSend(new Record(i));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
4) 测试结果
```
2019-11-01 11:44:34.550  INFO 1576 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 6 ms
2019-11-01 11:44:35.653  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=0
2019-11-01 11:44:36.625  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=1
2019-11-01 11:44:37.625  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=2
2019-11-01 11:44:38.626  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=3
2019-11-01 11:44:39.627  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=4
2019-11-01 11:44:40.627  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=5
2019-11-01 11:44:42.629  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=6
2019-11-01 11:44:42.630  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=7
2019-11-01 11:44:42.631  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=5
2019-11-01 11:44:44.632  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=8
2019-11-01 11:44:44.632  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=9
2019-11-01 11:44:44.634  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=5
2019-11-01 11:44:46.634  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=10
2019-11-01 11:44:46.635  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=11
2019-11-01 11:44:46.635  INFO 1576 --- [ntContainer#0-1] com.xiefq.learn.service.MqListener       : 收到消息=5
```
5) 结果证明
```
他确实是放到队尾，然后不影响其他消息的消费，证明理论是没问题的
但是，如果，发送重回队列的消息太快，就会严密后面来的消息，导致造成死循环的假象，比如上述代码，如给将TimeUnit.SECONDS.sleep(2);去掉，就会一直死循环
```

# 2. 线程池的执行流程
> 问题：请问线程池的执行流程是什么？

+ 我的回答
```
先分配coreSize，满了之后再分配核心以外的线程，达到maximumPoolSize的时候，线程进入等待队列
```
+ 面试官解答
```
顺序反了，应该coreSize满了进入等待队列，等待队列满了后，分配核心以外的线程执行
```
+ 我的疑问
```
那如果，等待队列是无界的，那岂不是maximumPoolSize设置就没用了么。
```

+ 实验代码
```java
@Test
    public void test() throws Exception {
        ThreadPoolExecutor exe = new ThreadPoolExecutor(
                5,
                100,
                100,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100));
        for(int i=0;i<100;i++){
            exe.submit(()->{
                try {
                    printPool(exe);
                    TimeUnit.DAYS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        exe.awaitTermination(1, TimeUnit.DAYS);
    }
    public void printPool(ThreadPoolExecutor exe){
        log.info("当前活跃线程数量={}", exe.getActiveCount());
        log.info("当前等待队列线程数量={}", exe.getQueue().size());
    }
```
1） 我是用有界队列，队列的容量完全能满足任务线程执行结果
```
12:22:21.195 [pool-1-thread-1] INFO com.xiefq.learn.TestThreadPoollSequence - 当前活跃线程数量=4
12:22:21.195 [pool-1-thread-5] INFO com.xiefq.learn.TestThreadPoollSequence - 当前活跃线程数量=5
12:22:21.195 [pool-1-thread-4] INFO com.xiefq.learn.TestThreadPoollSequence - 当前活跃线程数量=4
12:22:21.195 [pool-1-thread-2] INFO com.xiefq.learn.TestThreadPoollSequence - 当前活跃线程数量=4
12:22:21.195 [pool-1-thread-3] INFO com.xiefq.learn.TestThreadPoollSequence - 当前活跃线程数量=4
12:22:21.200 [pool-1-thread-5] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
12:22:21.200 [pool-1-thread-4] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
12:22:21.200 [pool-1-thread-2] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
12:22:21.200 [pool-1-thread-3] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
12:22:21.200 [pool-1-thread-1] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
```
证明这种情况下，面试官的说法是正确的

2）我改为无界队列
```java
/**
 * 测试thread pool的
 */
@Slf4j
public class TestThreadPoollSequence {
    @Test
    public void test() throws Exception {
        ThreadPoolExecutor exe = new ThreadPoolExecutor(
                5,
                100,
                100,
                TimeUnit.SECONDS,
                new PriorityBlockingQueue<>());
        for(int i=0;i<100;i++){
            exe.execute((MyRunable) () -> {
                try {
                    printPool(exe);
                    TimeUnit.DAYS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        exe.awaitTermination(1, TimeUnit.DAYS);
    }
    public void printPool(ThreadPoolExecutor exe){
        log.info("当前活跃线程数量={}", exe.getActiveCount());
        log.info("当前等待队列线程数量={}", exe.getQueue().size());
    }
}
interface MyRunable extends Comparable<MyRunable>, Runnable {
    @Override
    default int compareTo(MyRunable o) {
        return 0;
    }
}
```
测试结果
```
12:34:24.991 [pool-1-thread-1] INFO com.xiefq.learn.TestThreadPoollSequence - 当前活跃线程数量=5
12:34:24.991 [pool-1-thread-4] INFO com.xiefq.learn.TestThreadPoollSequence - 当前活跃线程数量=5
12:34:24.991 [pool-1-thread-3] INFO com.xiefq.learn.TestThreadPoollSequence - 当前活跃线程数量=5
12:34:24.994 [pool-1-thread-2] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
12:34:24.994 [pool-1-thread-4] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
12:34:24.994 [pool-1-thread-5] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
12:34:24.994 [pool-1-thread-3] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
12:34:24.994 [pool-1-thread-1] INFO com.xiefq.learn.TestThreadPoollSequence - 当前等待队列线程数量=95
```

+ 结论
```
确实是如面试官所说
但是，这个maximumPoolSize就没撒用了啊
```