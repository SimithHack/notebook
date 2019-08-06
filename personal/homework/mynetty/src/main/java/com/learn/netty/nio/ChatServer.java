package com.learn.netty.nio;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChatServer {
    public static void main(String[] args) throws Exception {
        Map<String, SocketChannel> clients = new HashMap<>();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress(8800));
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while(true){
            selector.select();
            Set<SelectionKey> sks = selector.selectedKeys();
            for (SelectionKey s : sks) {
                if (s.isAcceptable()) {
                    ServerSocketChannel serverCh = (ServerSocketChannel) s.channel();
                    SocketChannel clientCh = serverCh.accept();
                    clientCh.configureBlocking(false);
                    String key = clientCh.getRemoteAddress().toString();
                    clients.put(key, clientCh);
                    //注册
                    clientCh.register(selector, SelectionKey.OP_READ);
                }else if(s.isReadable()){
                    SocketChannel clientCh = (SocketChannel)s.channel();
                    String key = clientCh.getRemoteAddress().toString();
                    ByteBuffer data = ByteBuffer.allocate(512);
                    //读取数据
                    StringBuilder msg = new StringBuilder();
                    int l ;
                    while((l=clientCh.read(data))>0){
                        data.flip();
                        byte[] sliceData = new byte[data.limit()];
                        data.get(sliceData, data.position(), data.limit());
                        System.out.printf("position=%d, limit=%d, slice=%d\n", data.position(), data.limit(), sliceData.length);
                        msg.append(new String(sliceData, "utf-8"));
                        data.clear();
                    }
                    System.out.println("收到客户端数据："+key+":"+msg);
                    //回写给所有的客户端
                    for(Map.Entry<String, SocketChannel> cl : clients.entrySet()){
                        if(key.equals(cl.getKey())){
                            cl.getValue().write(ByteBuffer.wrap(("我说："+msg.toString()).getBytes()));
                        }else{
                            cl.getValue().write(ByteBuffer.wrap((cl.getKey()+"说："+msg.toString()).getBytes()));
                        }
                    }
                }
            }
            sks.clear();
        }
    }
}
