package com.xiefq.learn.nio;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
@Slf4j
public class NioTest {
    public static void main(String[] args) throws Exception {
        //绑定端口
        ServerSocketChannel ss = ServerSocketChannel.open()
                .bind(new InetSocketAddress(1000), 1024);
        //配置阻塞模式为非阻塞
        ss.configureBlocking(false);
        //初始化Selector
        Selector selector = Selector.open();
        //绑定channel到selector上，并表示我感兴趣链接建立事件
        ss.register(selector, SelectionKey.OP_ACCEPT);
        //进入事件循环
        while (true){
            //获取到所有感兴趣的事件
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIter = keys.iterator();
            while(keyIter.hasNext()){
                SelectionKey key = keyIter.next();
                if(key.isAcceptable()){
                    log.info("有客户端连接进来");
                    ServerSocketChannel sc = (ServerSocketChannel)key.channel();
                    SocketChannel ch = sc.accept();
                    ch.configureBlocking(false);
                    ch.register(selector, SelectionKey.OP_READ);
                }
                if(key.isReadable()){
                    SocketChannel ch = (SocketChannel)key.channel();
                    log.info("通道可读");
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    ch.read(buffer);
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    log.info("收到客户端消息：{}", new String(bytes));
                    ch.write(ByteBuffer.wrap(("欢迎"+ch.getRemoteAddress().toString()).getBytes()));
                }
                keyIter.remove();
            }
        }
    }
}
