package com.learn.netty.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class GrpcClient {
    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8800)
                .usePlaintext().build();
        StudentServiceGrpc.StudentServiceStub stuService = StudentServiceGrpc.newStub(channel);
        StreamObserver<StuInfo.StreamRequest> requests = stuService.biTalk(new StreamObserver<StuInfo.StreamResponse>() {
            @Override
            public void onNext(StuInfo.StreamResponse streamResponse) {
                System.out.println("收到服务器消息："+streamResponse.getResponseInfo());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("客户端连接关闭");
            }
        });
        for(int i=0;i<10;i++){
            requests.onNext(StuInfo.StreamRequest.newBuilder().setRequestInfo("客户端时间："+ LocalDateTime.now()).build());
            TimeUnit.SECONDS.sleep(2);
        }
        TimeUnit.SECONDS.sleep(2);
        requests.onCompleted();
    }
}
