# 生产者

![](./imgs/1.png "生产者总体架构图")  
__生产者总体架构图__

## 简单生产者配置
```java
private Properties kafkaProps = new Properties();
kafkaProps.put("bootstrap.servers", "broker1:9092,broker2:9092");
kafkaProps.put("key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer");
kafkaProps.put("value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer");
producer = new KafkaProducer<String, String>(kafkaProps);
```

## 消息发送方式
+ Fire-and-forget
+ Synchronous send
+ Asynchronous send

## 消息发送代码

### 无回调同步方式
```java
ProducerRecord<String, String> record =
                new ProducerRecord<>("CustomerCountry", "Precision Products", "France");
try {
    Future<RecordMetadata> metaresult = producer.send(record);
    //metaresult里可以查看消息发送是否成功
    //调用get方法就阻塞，变成同步了
    metaresult.get();
} catch (Exception e) {
    //可抛出SerializationException和TimeoutException两种异常
    e.printStackTrace();
}
```
### 异步方式
+ 回调实现
```java
private class DemoProducerCallback implements Callback {
	@Override
	public void onCompletion(RecordMetadata recordMetadata, Exception e) {
		if (e != null) {
			e.printStackTrace();
		}
	}
}
```

+ 注册监听器
```java
ProducerRecord<String, String> record =
				new ProducerRecord<>("CustomerCountry", "Biomedical Materials", "USA");
producer.send(record, new DemoProducerCallback());
```

## 生产者配置参数
+ acks 
> how many partition replicas must receive the record before the producer can consider the write successful
```
- 0 the producer will not wait for a reply from the broker before assuming the message was sent successfully  
- 1 the producer will receive a success response from the broker the moment the leader replica received the message  
- all the producer will receive a success response from the broker once all in-sync replicas received the message.
```

+ buffer.memory
> the amount of memory the producer will use to buffer messages waiting to be sent to brokers.

+ compression.type
> snappy, gzip, or lz4

+ retries
+ batch.size
> multiple records are sent to the same partition, the producer will batch
them together. This parameter controls the amount of memory in bytes (not
messages!) that will be used for each batch.

+ linger.ms
> the amount of time to wait for additional messages before
sending the current batch.

+ client.id
+ max.in.flight.requests.per.connection
> how many messages the producer will send to the server without
receiving responses.

+ timeout.ms, request.timeout.ms, and metadata.fetch.timeout.ms
+ max.block.ms
> how long the producer will block when calling send()

+ max.request.size
> the size of a produce request sent by the producer.

+ receive.buffer.bytes and send.buffer.bytes
> the TCP send and receive buffers used by the sockets
when writing and reading data. If these are set to -1, the OS defaults will be
used.

## 消息顺序注意事项
Apache Kafka preserves the order of messages within a partition.

## 自定义Serializers
实现 Serializer 接口，并实现 serialize方法里边的逻辑

## key的作用
> Keys serve
two goals: they are additional information that gets stored with the message, and
they are also used to decide which one of the topic partitions the message will be
written to.
keykey是空值，空值的消息使用kafka默认的负载策略，决定消息往哪个partion

## 实现自定义分区策略
实现 Partitioner 接口 并实现partition方法的逻辑

# 生产者
## 组的概念以及组的消息分发机制
> 看下面几张图完全说明

![](./imgs/2.png)
![](./imgs/3.png)
![](./imgs/4.png)
![](./imgs/5.png)
![](./imgs/6.png)

leader consumer 保有全部其他consumer的信息
>The way consumers maintain membership in a consumer group and ownership
of the partitions assigned to them is by sending heartbeats to a Kafka broker
designated as the group coordinator.  
If the consumer stops sending heartbeats for long enough, its session will time
out and the group coordinator will consider it dead and trigger a rebalance.