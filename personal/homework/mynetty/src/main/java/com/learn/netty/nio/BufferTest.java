package com.learn.netty.nio;

import java.nio.ByteBuffer;

public class BufferTest {
    public static void main(String[] args) throws Exception {
        new BufferTest().sliceTest();
    }
    private void readOnlyBuffer(){
        ByteBuffer buffer = ByteBuffer.allocate(40);
        for(int i=0;i<buffer.capacity();i++){
            buffer.put((byte)i);
        }
        ByteBuffer ro = buffer.asReadOnlyBuffer();

    }
    private void putBuffer(){
        ByteBuffer buffer = ByteBuffer.allocate(40);
        buffer.putInt(23);
        buffer.putDouble(22.4d);
        buffer.flip();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getDouble());
    }
    private void sliceTest(){
        ByteBuffer buffer = ByteBuffer.allocate(40);
        for(int i=0;i<buffer.capacity();i++){
            buffer.put((byte)i);
        }
        buffer.position(10);
        System.out.println(buffer.get());
        buffer.position(10);
        buffer.limit(12);
        ByteBuffer sliceBuffer = buffer.slice();
        System.out.printf("position = %d, limit = %d, capacity = %d \n", sliceBuffer.position(), sliceBuffer.limit(), sliceBuffer.capacity());
        for(int i=0;i<sliceBuffer.capacity();i++){
            sliceBuffer.put(i, (byte)(sliceBuffer.get(i)*2));
        }
        buffer.flip();
        buffer.limit(buffer.capacity());
        while (buffer.hasRemaining()){
            System.out.print(buffer.get()+",");
        }
    }
}
