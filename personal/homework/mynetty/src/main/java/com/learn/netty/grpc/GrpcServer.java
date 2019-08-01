package com.learn.netty.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    private Server server;
    private void start() throws IOException {
        this.server = ServerBuilder.forPort(8800).addService(new StudentServiceImpl()).build().start();
        System.out.println("服务器启动");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("关闭jvm");
            stop();
        }));
    }
    private void stop(){
        if(null != this.server){
            this.server.shutdown();
        }
    }
    private void awaitTermination() throws InterruptedException{
        if(null != this.server){
            this.server.awaitTermination();
        }
    }
    public static void main(String[] args) throws Exception {
        GrpcServer server = new GrpcServer();
        server.start();
        server.awaitTermination();
    }
}
