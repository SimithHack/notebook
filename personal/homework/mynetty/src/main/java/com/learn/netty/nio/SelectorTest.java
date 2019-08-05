package com.learn.netty.nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class SelectorTest {
    @Test
    public void test() throws Exception {
        int[]ports = {5001, 5002, 5003, 5004};
        Selector selector  = Selector.open();
        for(int i=0;i<ports.length;i++){
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(ports[i]));
            //我们只关心连接建立的事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口"+ports[i]);
        }
        while (true){
            int numbers = selector.select();
            System.out.printf("已经有%d个客户端准备就绪\n", numbers);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for(SelectionKey sk : selectionKeys){
                //已经有连接进来了
                if(sk.isAcceptable()){
                    //处理连接
                    ServerSocketChannel ssc = (ServerSocketChannel)sk.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    //再次进行注册-表示我们已经准备好读了
                    sc.register(selector, SelectionKey.OP_READ);
                    System.out.println("连接已经建立，准备读取"+sc);
                    //处理完需要删除-表示事件已经用完了
                    selectionKeys.remove(sk);
                }else if(sk.isReadable()){
                    SocketChannel sc = (SocketChannel)sk.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(512);
                    while(sc.read(buffer)>0){
                        buffer.flip();
                        sc.write(buffer);
                        buffer.clear();
                    }
                    selectionKeys.remove(sk);
                }
            }
        }
    }
}
