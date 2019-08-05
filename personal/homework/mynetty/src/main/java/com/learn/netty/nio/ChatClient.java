package com.learn.netty.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;

public class ChatClient {
    public static void main(String[] args) throws Exception {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        Selector selector = Selector.open();
        sc.register(selector, SelectionKey.OP_CONNECT);
        sc.connect(new InetSocketAddress("127.0.0.1", 8800));
        while(true){
            selector.select();
            Set<SelectionKey> sks = selector.selectedKeys();
            for(SelectionKey sk : sks){
                if(sk.isConnectable()){
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    if(socketChannel.isConnectionPending()){
                        socketChannel.finishConnect();
                        //写数据
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        buffer.put(("客户端时间："+ LocalDateTime.now()+"建立连接").getBytes());
                        buffer.flip();
                        socketChannel.write(buffer);
                        //开启新发送数据
                        new Thread(()->{
                            while (true){
                                try{
                                    buffer.clear();
                                    InputStreamReader reader = new InputStreamReader(System.in);
                                    BufferedReader br = new BufferedReader(reader);
                                    buffer.put(br.readLine().getBytes());
                                    buffer.flip();
                                    socketChannel.write(buffer);
                                }catch (IOException e){
                                }
                            }
                        }).start();
                    }
                }
            }
        }
    }
}
