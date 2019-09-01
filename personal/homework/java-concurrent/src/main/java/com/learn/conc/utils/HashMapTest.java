package com.learn.conc.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HashMapTest {
    public static void main(String[] args) throws Exception {
        /*Map<String, Integer> map = new HashMap();
        for(int i=0;i<1000;i++){
            final int idx = i+1;
            new Thread(()->{
                map.put(UUID.randomUUID().toString(), idx);
            }).start();
        }
        Thread.currentThread().join();
*/
        BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(System.out));
        for(T next: new T("nh")){
            System.out.println(next);
        }
    }
}
class T implements Iterable<T> {
    private String ss;

    public T(String ss) {
        this.ss = ss;
    }

    @Override
    public String toString() {
        return ss;
    }

    @Override
    public Iterator iterator() {
        System.out.println("nihao");
        return new Iterator<T>(){

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return new T(UUID.randomUUID().toString());
            }
        };
    }
}