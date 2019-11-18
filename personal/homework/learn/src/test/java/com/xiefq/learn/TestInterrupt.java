package com.xiefq.learn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 测试线程中断
 */
@Slf4j
public class TestInterrupt {
    @Test
    public void test() throws Exception {
        Thread t1 = new Thread(()->{
            try {
                while(true){
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(()->{
            while(true){
            }
        });
        t1.start();
        t2.start();
        TimeUnit.SECONDS.sleep(5);
        t1.interrupt();
        t2.interrupt();
        log.info("t1 isinterrupt = {}", t1.isInterrupted());
        log.info("t2 isinterrupt = {}", t2.isInterrupted());
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testWait() throws Exception {
        Integer lock = new Integer(1);
        final ConditionFlag flag = new ConditionFlag(false);
        Thread t1 = new Thread(()->{
            synchronized (lock){
                while(!flag.isFlag()){
                    log.info("t1 等待条件成立");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("t1 正常执行完毕!");
            }
        });
        Thread t2 = new Thread(()->{
            synchronized (lock){
                log.info("t2 开始执行");
                flag.setFlag(true);
                lock.notifyAll();
            }
        });
        t1.start();
        t2.start();
        TimeUnit.SECONDS.sleep(5);
    }
    @Data
    @AllArgsConstructor
    class ConditionFlag {
        private boolean flag;
    }
}
