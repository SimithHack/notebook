package com.learn.redis.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * redis执行一次命令的时间 = 命令执行时间 + 网络传输时间
 * redis本身来说，执行速度很快，但是，网络传输延迟特别的大，使用pipeline可用将许多命令打包执行，减少网络带宽的使用次数
 * ===pipleline的使用注意===
 * 1 注意每次携带的数据量，太大对网络带宽压力大
 * 2 pipeline命令执行不是原子的，而M操作是原子的
 * 3 pipeline每次只能作用在一个redis节点上
 */
public class PipeLineTest {
    @Test
    public void testNoPipeLine(){
        long start = System.currentTimeMillis();
        Jedis jedis = new Jedis("192.168.211.132", 6379);
        for(int i=0; i<10000; i++){
            jedis.hset("hashkey:"+i, "field_"+i, "value_"+i);
        }
        long end = System.currentTimeMillis();
        System.out.println("用时"+(end-start));
    }

    /**
     * 使用pipeLine进行测试，没100个执行一次
     */
    @Test
    public void testWithPipeLine(){
        long start = System.currentTimeMillis();
        Jedis jedis = new Jedis("192.168.211.132", 6379);
        for(int i=0; i<100; i++){
            Pipeline pipeline = jedis.pipelined();
            for(int j=i*100; j<(i+1)*100; j++){
                pipeline.hset("hashkey:"+j, "field_"+j, "value_"+j);
            }
            pipeline.syncAndReturnAll();
        }
        long end = System.currentTimeMillis();
        System.out.println("用时"+(end-start));
    }
}
