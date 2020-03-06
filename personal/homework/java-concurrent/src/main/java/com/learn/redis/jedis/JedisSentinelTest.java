package com.learn.redis.jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;

import java.util.HashSet;
import java.util.Set;


public class JedisSentinelTest {
    @Test
    public void test(){
        Set<String> servers = new HashSet<String>();
        servers.add(new HostAndPort("master", 26379).toString());
        servers.add(new HostAndPort("master", 26380).toString());
        servers.add(new HostAndPort("master", 26381).toString());
        //看看当前的master是谁
        JedisSentinelPool spool = new JedisSentinelPool("mymaster", servers);
        System.out.println(spool.getCurrentHostMaster().toString());
        Jedis jedis = spool.getResource();
        for(int i=0; i<100; i++){
            Pipeline pipeline = jedis.pipelined();
            for(int j=0;j<100;j++){
                pipeline.set("key"+(i*100+j), "value"+(i*100+j));
            }
            pipeline.syncAndReturnAll();
        }
        jedis.close();
    }
}
