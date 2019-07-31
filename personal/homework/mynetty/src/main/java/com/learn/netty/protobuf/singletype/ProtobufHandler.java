package com.learn.netty.protobuf.singletype;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtobufHandler extends SimpleChannelInboundHandler<DataInfo.Student>  {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.Student student) throws Exception {
        System.out.println(student.toString());
    }
}
