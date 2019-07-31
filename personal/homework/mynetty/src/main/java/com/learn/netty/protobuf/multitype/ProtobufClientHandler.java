package com.learn.netty.protobuf.multitype;

import com.learn.netty.protobuf.singletype.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class ProtobufClientHandler extends SimpleChannelInboundHandler<HomeInfo.Home> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HomeInfo.Home home) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HomeInfo.Home home = null;
        Random random = new Random();
        int idx = random.nextInt(3);
        switch (idx){
            case 0:
                home = HomeInfo.Home.newBuilder()
                        .setDataType(
                            HomeInfo.Home.DataType.PersonType
                        )
                        .setPerson(
                                HomeInfo.Person.newBuilder()
                                    .setAddress("四川成都")
                                    .setAge(33)
                                    .setName("王思聪")
                                .build()
                        )
                        .build();
                break;
            case 1:
                home = HomeInfo.Home.newBuilder()
                        .setDataType(
                                HomeInfo.Home.DataType.DogType
                        )
                        .setDog(
                                HomeInfo.Dog.newBuilder()
                                        .setAge(2)
                                        .setName("狗狗")
                                        .build()
                        )
                        .build();
                break;
            case 2:
                home = HomeInfo.Home.newBuilder()
                        .setDataType(
                                HomeInfo.Home.DataType.CatType
                        )
                        .setCat(
                                HomeInfo.Cat.newBuilder()
                                        .setName("王思聪")
                                        .setCity("上海")
                                        .build()
                        )
                        .build();
                break;
        }
        ctx.channel().writeAndFlush(home);
    }
}
