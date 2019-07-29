package com.learn.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        clients.forEach(ch -> {
            if(ch != channel){
                ch.writeAndFlush(channel.remoteAddress() + " 发送消息："+msg+"\r\n");
            }else{
                ch.writeAndFlush("[自己]："+msg+"\r\n");
            }
        });
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        clients.writeAndFlush("[服务器] - " + channel.remoteAddress() + "上线\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        clients.writeAndFlush("[服务器] - " + channel.remoteAddress() + "下线\n");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("[服务器] - " + channel.remoteAddress() + "加入，客户端数量"+clients.size()+"\n");
        clients.writeAndFlush("[服务器] - " + channel.remoteAddress() + "加入\n");
        clients.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        clients.writeAndFlush("[服务器] - " + channel.remoteAddress() + "离开\n");
    }
}
