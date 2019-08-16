package com.learn.conc.rwlock;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantLockTest {
    @Test
    public void test1() throws Exception {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        final Map<String, Integer> ps = new HashMap<>();
        ps.put("count", 0);
        ExecutorService executors = Executors.newFixedThreadPool(10);
        executors.submit(()->{
            for(int i=0;i<1000;i++){
                lock.writeLock().lock();
                ps.put("count", ps.get("count")+1);
                lock.writeLock().unlock();
            }
        });
        for(int i=0;i<9;i++){
            executors.submit(()->{
                for(int j=0;j<1000;j++){
                    lock.readLock().lock();
                    ps.put("count", ps.get("count")+1);
                    lock.readLock().unlock();
                }
            });
        }
        executors.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println(ps.get("count"));
        Assert.assertNotEquals("10000", ps.get("count").toString());
    }
    @Test
    public void test2(){

    }
}
