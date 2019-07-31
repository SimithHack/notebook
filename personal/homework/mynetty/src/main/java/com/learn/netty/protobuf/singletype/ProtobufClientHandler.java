package com.learn.netty.protobuf.singletype;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtobufClientHandler extends SimpleChannelInboundHandler<DataInfo.Student> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.Student student) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DataInfo.Student stu = DataInfo.Student.newBuilder()
                .setAddress("四川成都")
                .setAge(12)
                .setName("王思聪")
                .build();
        ctx.channel().writeAndFlush(stu);
    }
}
