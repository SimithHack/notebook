package com.xiefq.learn.classloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * cglib测试
 */
@Slf4j
public class CglibTest {
    public static void main(String[] args){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CgHello.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                log.info("before invoke");
                Object rtn = methodProxy.invokeSuper(o, args);
                log.info("return {}", rtn);
                log.info("after invoke");
                return rtn;
            }
        });
        CgHello proxyHello = (CgHello)enhancer.create();
        proxyHello.say();
        proxyHello.wocao();
    }
}
class CgHello {
    public String say(){
        return "hello world";
    }
    public String wocao(){
        return "wocao";
    }
}
