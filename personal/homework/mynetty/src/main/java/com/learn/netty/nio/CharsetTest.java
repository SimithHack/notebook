package com.learn.netty.nio;

import io.netty.util.NettyRuntime;
import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class CharsetTest {
    @Test
    public void testFile() throws Exception {
        RandomAccessFile iptFile = new RandomAccessFile("charset_input.txt", "r");
        RandomAccessFile optFile = new RandomAccessFile("charset_output.txt", "rw");
        Charset charset = Charset.forName("iso-8859-1");
        CharsetEncoder encoder = charset.newEncoder();
        CharsetDecoder decoder = charset.newDecoder();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        while(iptFile.getChannel().read(buffer)>0){
            buffer.flip();
            CharBuffer charBuffer = decoder.decode(buffer);
            optFile.getChannel().write(encoder.encode(charBuffer));
            buffer.clear();
        }
        iptFile.close();
        optFile.close();
    }
    @Test
    public void testCharset(){
        Charset.availableCharsets().forEach((k, v)->{
            System.out.printf("%s=%s\n", k, v);
        });
    }
    @Test
    public void testProcessor(){
        System.out.println(NettyRuntime.availableProcessors());
    }
}
