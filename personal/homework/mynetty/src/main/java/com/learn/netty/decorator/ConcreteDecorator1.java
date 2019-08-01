package com.learn.netty.decorator;

public class ConcreteDecorator1 extends Decorator {
    public ConcreteDecorator1(Component comp) {
        super(comp);
    }

    @Override
    public void doSomthing() {
        doOtherthing();
        super.doSomthing();
    }

    private void doOtherthing() {
        System.out.println("ConcreteDecorator1.doOtherthing");
    }
}
