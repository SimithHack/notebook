package com.learn.conc.utils;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CycBarrierTest {
    public static void main(String[] args) throws Exception {
        final CyclicBarrier session = new CyclicBarrier(10,()->{
            System.out.println("所有人已经到位，现在开始开会");
        });
        for(int i=0;i<10;i++){
            final int idx = i+1;
            new Thread(()->{
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(20));
                    System.out.printf("第%d位领导已经到了\n", idx);
                    session.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
