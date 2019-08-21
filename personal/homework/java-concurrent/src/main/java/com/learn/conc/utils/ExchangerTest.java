package com.learn.conc.utils;

import org.junit.Test;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ExchangerTest {

    @Test
    public void testExchanger() throws Exception {

    }

    public void a(Exchanger<String> exchanger){
        System.out.println("a执行");
        try {
            TimeUnit.SECONDS.sleep(1);
            exchanger.exchange("123456");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void b(Exchanger<String> exchanger){
        System.out.println("b执行");
        try {
            TimeUnit.SECONDS.sleep(4);
            exchanger.exchange("123456");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
