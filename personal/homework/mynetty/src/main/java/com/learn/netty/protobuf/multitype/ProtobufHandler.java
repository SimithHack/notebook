package com.learn.netty.protobuf.multitype;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtobufHandler extends SimpleChannelInboundHandler<HomeInfo.Home>  {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HomeInfo.Home home) throws Exception {
        System.out.println(home.toString());
    }
}
