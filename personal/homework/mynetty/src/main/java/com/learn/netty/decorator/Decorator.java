package com.learn.netty.decorator;

public class Decorator implements Component {
    private Component component;
    public Decorator(Component comp){
        this.component = comp;
    }
    @Override
    public void doSomthing() {
        component.doSomthing();
    }

}
