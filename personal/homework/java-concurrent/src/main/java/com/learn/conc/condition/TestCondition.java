package com.learn.conc.condition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestCondition {
    public static void main(String[] args) throws Exception {
        Tmall tmal = new Tmall();
        new Thread(new Producer(tmal)).start();
        new Thread(new Producer(tmal)).start();
        new Thread(new Producer(tmal)).start();
        new Thread(new Consumer(tmal)).start();
        new Thread(new Consumer(tmal)).start();
        ThreadLocal<Integer> locla = new ThreadLocal<>();
    }
}
class Producer implements Runnable {
    private Tmall tmall;

    public Producer(Tmall tmall) {
        this.tmall = tmall;
    }

    @Override
    public void run() {
        try {
            while (true){
                this.tmall.produce();
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class Consumer implements Runnable {
    private Tmall tmall;

    public Consumer(Tmall tmall) {
        this.tmall = tmall;
    }

    @Override
    public void run() {
        try {
            while (true){
                this.tmall.consume();
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class Tmall{
    private ReentrantLock lock = new ReentrantLock();
    private Condition consumeCondition = lock.newCondition();
    private Condition produceCondition = lock.newCondition();
    private int LIMIT = 10;
    private volatile int count = 0;
    public void consume() {
        while(count<=0){
            System.out.println("库存不足");
            try {
                produceCondition.signal();
                consumeCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lock.lock();
        count--;
        lock.unlock();
        System.out.println("消费一件商品，库存"+count);
    }
    public void produce() {
        while(count>=LIMIT){
            System.out.println("库存已满");
            try {
                consumeCondition.signal();
                produceCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        lock.lock();
        count++;
        lock.unlock();
        System.out.println("生产一件商品，库存"+count);
    }
}
