package com.learn.conc.rwlock;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadNotification {
    volatile int signal = 0;
    private int count = 0;
    @Test
    public void test1() throws Exception {
        new Thread(()->{
            try {
                System.out.println("1执行");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("1执行完毕");
                signal = 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            while (signal!=1){
                try {
                    System.out.println("2等待");
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("2执行");
        }).start();
        TimeUnit.SECONDS.sleep(5);
    }
    @Test
    public void test2() throws Exception {
        ThreadNotification n = new ThreadNotification();
        new Thread(()->{
            try {
                synchronized (n){
                    System.out.println("1执行");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("1执行完毕");
                    count = 1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        for(int i=2;i<12;i++){
            final int idx = i;
            new Thread(()->{
                try {
                    synchronized (n){
                        if(count!=1){
                            n.wait();
                        }
                    }
                    System.out.println(idx+"执行");
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        TimeUnit.SECONDS.sleep(20);
    }
    @Test
    public void test3() throws Exception {
        final  ConcurrentHashMap<String, Integer> c = new ConcurrentHashMap();
        ReentrantReadWriteLock.WriteLock w = new ReentrantReadWriteLock().writeLock();
        c.put("count", 0);
        for(int j=0; j<10; j++){
            new Thread(()->{
                for(int i=0; i<1000; i++){
                    w.lock();
                    int ct = c.get("count")+1;
                    w.unlock();
                    c.put("count", ct);
                }
            }).start();
        }
        TimeUnit.SECONDS.sleep(10);
        System.out.println(c.get("count"));
    }
}
