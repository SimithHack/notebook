package com.xiefq.learn.classloader;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Java反射测试
 */
public class ProxyTest {
    public static void main(String[] args){
        HelloImpl hello = new HelloImpl();
        HelloInvocationHandler handler = new HelloInvocationHandler(hello);
        Hello proxyHello = (Hello) Proxy.newProxyInstance(HelloImpl.class.getClassLoader(), HelloImpl.class.getInterfaces(), handler);
        proxyHello.say();
    }
}
interface Hello {
    void say();
}
@Slf4j
class HelloImpl implements Hello {
    @Override
    public void say() {
        log.info("say hello");
    }
}
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
class HelloInvocationHandler implements InvocationHandler {
    private Hello hello;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("before invoke");
        Object rtn = method.invoke(hello, args);
        log.info("after invoke");
        return rtn;
    }
}