package com.learn.conc.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class CountdownTest {
    @Test
    public void testCountdown() throws Exception {
        String[] lines = {
                "22,33,1,98,45",
                "22,33,1,98,45,23,100",
                "22,23,1,98,45,87",
                "22,33,1"
        };
        CountDownLatch latch = new CountDownLatch(lines.length);
        List<FutureTask<Integer>> tasks = new ArrayList<>(lines.length);
        for(String l : lines){
            FutureTask task = new FutureTask(new Task(l,latch));
            tasks.add(task);
            new Thread(task).start();
        }
        latch.await();
        int sum = 0;
        for(FutureTask<Integer> t : tasks){
            sum += t.get();
        }
        System.out.println("sum="+sum);
    }
}
class Task implements Callable<Integer> {
    private String task;
    private CountDownLatch latch;
    public Task(String tsk, CountDownLatch latch){
        this.latch = latch;
        this.task = tsk;
    }
    @Override
    public Integer call() throws Exception {
        final int[] sum = {0};
        Arrays.stream(task.split(",")).forEach(s -> {
            sum[0] += Integer.parseInt(s);
        });
        TimeUnit.SECONDS.sleep(10);
        latch.countDown();
        return sum[0];
    }
}
