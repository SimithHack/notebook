package com.learn.netty.decorator;

public class ConcreteDecorator2 extends Decorator {
    public ConcreteDecorator2(Component comp) {
        super(comp);
    }

    @Override
    public void doSomthing() {
        doOtherThing();
        super.doSomthing();
    }

    private void doOtherThing() {
        System.out.println("ConcreteDecorator2.doOtherThing");
    }

}
