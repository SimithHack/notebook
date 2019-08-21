package com.learn.netty.nio;

import org.junit.Test;

import java.io.FileInputStream;
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

    /**
     * Nio和Bio的区别
     * Nio是面向缓冲区的，不阻塞的，selector的
     * 缓冲区，负责数据的存取，数组，数据类型不同，提供了相应的缓冲区
     * ByteBuffer CharBuffer ShortBuffer IntBuffer ... 就是没有BoolBuffer
     * XXXBuffer.allocate()
     * 缓冲区的四个熟悉
     *  1 limit 可操作数据的边界
     *  2 position 正在操作的位置
     *  3 capacity 一旦声明，就不能修改
     *  0 <= mark <= position <= limit <= capacity
     * 几个方法的作用
     * flip 将limit = position, position = 0;
     * clear limit=capacity, position = 0;
     * rewind limit=capacity, position = 0 重复读取了
     * reset 回复position=mark的位置
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.put("abcdefq".getBytes());
        System.out.printf("position=%d, limit=%d, capacity=%d\n", bb.position(), bb.limit(), bb.capacity());
        bb.rewind();
        System.out.printf("position=%d, limit=%d, capacity=%d\n", bb.position(), bb.limit(), bb.capacity());
        bb.put("1234567890".getBytes());
        bb.mark();
        bb.put("1234567890".getBytes());
        System.out.printf("position=%d, limit=%d, capacity=%d\n", bb.position(), bb.limit(), bb.capacity());
        bb.reset();
        System.out.printf("position=%d, limit=%d, capacity=%d\n", bb.position(), bb.limit(), bb.capacity());
    }

    /**
     * java.nio.channels.Channel接口
     *     |--FileChanel
     *     |--SocketChannel
     *     |--ServerSocketChannel
     *     |--DatagramChannel
     * 获取通道
     * 1 本地IO
     *     FileInputStream的getChannel
     *     RandomAccessFile的getChannel
     * 2 网络IO
     * @throws Exception
     */
    @Test
    public void testChannel() throws Exception {
        FileInputStream fi = new FileInputStream("1.jpg");
    }
}
