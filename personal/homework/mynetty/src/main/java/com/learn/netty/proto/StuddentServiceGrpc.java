package com.learn.netty.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.22.1)",
    comments = "Source: Student.proto")
public final class StuddentServiceGrpc {

  private StuddentServiceGrpc() {}

  public static final String SERVICE_NAME = "com.learn.netty.proto.StuddentService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.learn.netty.proto.StuInfo.StreamRequest,
      com.learn.netty.proto.StuInfo.StreamResponse> getBikTalkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "bikTalk",
      requestType = com.learn.netty.proto.StuInfo.StreamRequest.class,
      responseType = com.learn.netty.proto.StuInfo.StreamResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.learn.netty.proto.StuInfo.StreamRequest,
      com.learn.netty.proto.StuInfo.StreamResponse> getBikTalkMethod() {
    io.grpc.MethodDescriptor<com.learn.netty.proto.StuInfo.StreamRequest, com.learn.netty.proto.StuInfo.StreamResponse> getBikTalkMethod;
    if ((getBikTalkMethod = StuddentServiceGrpc.getBikTalkMethod) == null) {
      synchronized (StuddentServiceGrpc.class) {
        if ((getBikTalkMethod = StuddentServiceGrpc.getBikTalkMethod) == null) {
          StuddentServiceGrpc.getBikTalkMethod = getBikTalkMethod = 
              io.grpc.MethodDescriptor.<com.learn.netty.proto.StuInfo.StreamRequest, com.learn.netty.proto.StuInfo.StreamResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.learn.netty.proto.StuddentService", "bikTalk"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.learn.netty.proto.StuInfo.StreamRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.learn.netty.proto.StuInfo.StreamResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new StuddentServiceMethodDescriptorSupplier("bikTalk"))
                  .build();
          }
        }
     }
     return getBikTalkMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StuddentServiceStub newStub(io.grpc.Channel channel) {
    return new StuddentServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StuddentServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new StuddentServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StuddentServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new StuddentServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class StuddentServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.learn.netty.proto.StuInfo.StreamRequest> bikTalk(
        io.grpc.stub.StreamObserver<com.learn.netty.proto.StuInfo.StreamResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getBikTalkMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getBikTalkMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.learn.netty.proto.StuInfo.StreamRequest,
                com.learn.netty.proto.StuInfo.StreamResponse>(
                  this, METHODID_BIK_TALK)))
          .build();
    }
  }

  /**
   */
  public static final class StuddentServiceStub extends io.grpc.stub.AbstractStub<StuddentServiceStub> {
    private StuddentServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StuddentServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StuddentServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StuddentServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.learn.netty.proto.StuInfo.StreamRequest> bikTalk(
        io.grpc.stub.StreamObserver<com.learn.netty.proto.StuInfo.StreamResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getBikTalkMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class StuddentServiceBlockingStub extends io.grpc.stub.AbstractStub<StuddentServiceBlockingStub> {
    private StuddentServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StuddentServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StuddentServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StuddentServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class StuddentServiceFutureStub extends io.grpc.stub.AbstractStub<StuddentServiceFutureStub> {
    private StuddentServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StuddentServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StuddentServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StuddentServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_BIK_TALK = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StuddentServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StuddentServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BIK_TALK:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.bikTalk(
              (io.grpc.stub.StreamObserver<com.learn.netty.proto.StuInfo.StreamResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class StuddentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StuddentServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.learn.netty.proto.StuInfo.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StuddentService");
    }
  }

  private static final class StuddentServiceFileDescriptorSupplier
      extends StuddentServiceBaseDescriptorSupplier {
    StuddentServiceFileDescriptorSupplier() {}
  }

  private static final class StuddentServiceMethodDescriptorSupplier
      extends StuddentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StuddentServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StuddentServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StuddentServiceFileDescriptorSupplier())
              .addMethod(getBikTalkMethod())
              .build();
        }
      }
    }
    return result;
  }
}
