package com.learn.netty.decorator;

public class DecoratorMain {
    public static void main(String[] args) throws Exception {
        Component c = new ConcreteDecorator1(new ConcreteDecorator2(new ConcreteComponent1()));
        c.doSomthing();
    }
}
