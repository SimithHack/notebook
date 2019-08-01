package com.learn.netty.decorator;

public class ConcreteComponent1 implements Component {
    @Override
    public void doSomthing() {
        System.out.println("ConcreteComponent1");
    }
}
