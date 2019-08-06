package com.learn.netty.nio;

import io.netty.buffer.ByteBuf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;

public class NioTest {
    public static void main(String[] args) throws Exception {
        new NioTest().copyFile();
    }

    private void copyFile() throws Exception {
        FileInputStream inputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("out.txt");
        FileChannel inputCh = inputStream.getChannel();
        FileChannel outCh = outputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(true){
            buffer.clear();
            int read = inputCh.read(buffer);
            if(read==-1){
                break;
            }
            buffer.flip();
            outCh.write(buffer);
        }
        inputStream.close();
        outputStream.close();
    }
    private void write() throws Exception {
        FileOutputStream fos = new FileOutputStream("NioTest.txt");
        FileChannel ch = fos.getChannel();
        ByteBuffer data = ByteBuffer.allocate(1024);
        printParams(data);
        data.put("你是一个好人，真的".getBytes());
        data.flip();
        printParams(data);
        ch.write(data);
        printParams(data);
        fos.close();
    }
    private void printParams(Buffer buffer){
        System.out.printf("position=%d, limit=%d, capacity=%d \n", buffer.position(), buffer.limit(), buffer.capacity());
    }
    private void read() throws Exception {
        FileInputStream fins = new FileInputStream("NioTest.txt");
        FileChannel channel = fins.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        while(buffer.hasRemaining()){
            byte b = buffer.get();
            System.out.print((char)b);
        }
        fins.close();
    }
    private void bufferTest(){
        IntBuffer buffer = IntBuffer.allocate(10);
        for(int i=0;i<buffer.capacity();i++){
            int randomNumb = new SecureRandom().nextInt(20);
            buffer.put(randomNumb);
        }
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
