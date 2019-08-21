package com.learn.redis.jedis;

/**
 * 发布订阅-并不是一个完备功能的消息队列
 *    频道
 *    消费者-消费者是抢这个消息的功能，它不会派发到所有的订阅者
 *    生产者-发布命令(publish channel message //返回订阅数量)
 *    订阅-(subscribe [channel, ..] //可用订阅多个)
 *    取消订阅-(unsubscribe [channel, ..])
 *    订阅模式-(psubscribe [pattern] //比如)
 *    列举出至少有i个订阅者的频道 (pubsub channels)
 *    列举指定频道的频道的订阅数量 (pubsub numsub [channel, ...])
 */
public class PubSubTest {
}
