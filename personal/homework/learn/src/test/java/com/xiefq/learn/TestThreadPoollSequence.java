package com.xiefq.learn;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * 测试thread pool的
 */
@Slf4j
public class TestThreadPoollSequence {
    @Test
    public void test() throws Exception {
        ThreadPoolExecutor exe = new ThreadPoolExecutor(
                5,
                100,
                100,
                TimeUnit.SECONDS,
                new PriorityBlockingQueue<>());
        for(int i=0;i<100;i++){
            exe.execute((MyRunable) () -> {
                try {
                    printPool(exe);
                    TimeUnit.DAYS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        exe.awaitTermination(1, TimeUnit.DAYS);
    }
    public void printPool(ThreadPoolExecutor exe){
        log.info("当前活跃线程数量={}", exe.getActiveCount());
        log.info("当前等待队列线程数量={}", exe.getQueue().size());
    }
}
interface MyRunable extends Comparable<MyRunable>, Runnable {
    @Override
    default int compareTo(MyRunable o) {
        return 0;
    }
}

