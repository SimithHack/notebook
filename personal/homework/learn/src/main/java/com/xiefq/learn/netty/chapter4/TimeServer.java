package com.xiefq.learn.netty.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.nio.channels.ServerSocketChannel;

public class TimeServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap boot = new ServerBootstrap();
        boot.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChildChanelHandler());
        ChannelFuture f = boot.bind(1000).sync();
        f.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
    //连接成功之后就会执行这个方法，为请求准备handler
    static class ChildChanelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            //为channel增加handler类似于拦截器一样
            channel.pipeline().addLast("httpCodec", new HttpServerCodec())
            .addLast("timeHandler", new TimeSeviceHandler());
        }
    }
    static class TimeSeviceHandler extends SimpleChannelInboundHandler<HttpObject> {
        /**
         * 读取客户端的请求，并向客户端发送响应
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
            ByteBuf content = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            ctx.writeAndFlush(response);
        }
    }
}
