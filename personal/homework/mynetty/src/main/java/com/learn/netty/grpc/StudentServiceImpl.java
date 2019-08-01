package com.learn.netty.grpc;

import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {
    @Override
    public StreamObserver<StuInfo.StreamRequest> biTalk(StreamObserver<StuInfo.StreamResponse> responseObserver) {
        return new StreamObserver<StuInfo.StreamRequest>() {
            @Override
            public void onNext(StuInfo.StreamRequest streamRequest) {
                System.out.println("收到客户端消息："+streamRequest.getRequestInfo());
                System.out.println("向客户端发送数据");
                responseObserver.onNext(StuInfo.StreamResponse.newBuilder().setResponseInfo("服务器时间："+ LocalDateTime.now()).build());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("服务器通道关闭");
            }
        };
    }
}
