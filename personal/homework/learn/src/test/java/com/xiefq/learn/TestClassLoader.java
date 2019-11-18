package com.xiefq.learn;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class TestClassLoader {
    @Test
    public void getArrayClassLoader(){
        //数组的classloader
        String[] array = new String[]{};
        //因为String是由根类加载器加载的，所以，返回为null
        log.info("classloader={}", array.getClass().getClassLoader());
        TestClassLoader[] loaders = new TestClassLoader[1];
        log.info("classloader={}", loaders.getClass().getClassLoader());
        //如果类型是原生的类型的话，没有classloader
        int[] ints = new int[1];
        log.info("classloader={}", ints.getClass().getClassLoader());
    }
}
