package com.learn.netty.nio;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class DirectBufferTest {
    public static void main(String[] args) throws Exception {

    }
    @Test
    public void testRandomAccessFile() throws Exception {
        RandomAccessFile file = new RandomAccessFile("NioTest.txt", "rw");
        FileChannel channel = file.getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 20);
        buffer.put("你真好".getBytes());
        file.close();
    }
    @Test
    public void testFileLock() throws Exception {
        RandomAccessFile file = new RandomAccessFile("D:/NioTest.txt", "rw");
        FileChannel channel = file.getChannel();
        //用于实现自定义协议
        FileLock lock = channel.lock(3, 6, true);
        System.out.printf("lock valid = %b, is shared = %b", lock.isValid(), lock.isShared());
        lock.release();
        file.close();
    }

    @Test
    public void testScatteringAndGathering() throws Exception {
        ServerSocketChannel serverCh = ServerSocketChannel.open();
        serverCh.socket().bind(new InetSocketAddress(8800));
        long messageLength = 2+3+4;
        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] =  ByteBuffer.allocate(2);
        buffers[1] =  ByteBuffer.allocate(3);
        buffers[2] =  ByteBuffer.allocate(4);
        SocketChannel socketCh = serverCh.accept();
        while(true){
            long byReaded = 0;
            while(byReaded < messageLength){
                long r = socketCh.read(buffers);
                byReaded += r;
                System.out.println("byteReaded "+ byReaded);
                Arrays.asList(buffers).stream().forEach(b->{
                    System.out.printf("position = %d , limit = %d , capacity = %d \n",
                            b.position(),
                            b.limit(),
                            b.capacity());
                });
            }
            //开始写到客户端
            Arrays.asList(buffers).stream().forEach(b->{
                b.flip();
            });
            long byteWritten = 0;
            while(byteWritten<messageLength){
                long w = socketCh.write(buffers);
                byteWritten += w;
            }
            Arrays.asList(buffers).stream().forEach(b->{
                b.clear();
            });
        }
    }
}
