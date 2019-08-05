package com.learn.netty.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

public class DirectBufferTest {
    @Test
    public void testAllocat(){
        ByteBuffer buffer = ByteBuffer.allocateDirect(64);
    }
}
