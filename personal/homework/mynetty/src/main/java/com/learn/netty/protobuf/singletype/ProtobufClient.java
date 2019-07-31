package com.learn.netty.protobuf.singletype;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ProtobufClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootStrap = new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(worker)
                    .handler(new ProtobufClientInitializer());
            ChannelFuture future = bootStrap.connect("localhost", 8800).sync();
            future.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
