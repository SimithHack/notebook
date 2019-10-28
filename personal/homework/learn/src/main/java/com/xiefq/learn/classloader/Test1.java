package com.xiefq.learn.classloader;

import java.util.Random;
import java.util.UUID;

public class Test1 {
    public static void main(String[] args) {
        Singleton s = Singleton.getInstance();
        System.out.println(Singleton.count1);
        System.out.println(Singleton.count2);
    }
}
class Singleton {
    public static int count1;
    public static Singleton instance = new Singleton();
    private Singleton(){
        count1++;
        count2++;
    }
    public static int count2 = 0;
    public static Singleton getInstance(){
        return instance;
    }
}