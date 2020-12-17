package io.github.hundanli.grpc.alts;

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
 * <pre>
 * The greeting service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.34.1)",
    comments = "Source: alts.proto")
public final class AltsGreeterGrpc {

  private AltsGreeterGrpc() {}

  public static final String SERVICE_NAME = "helloworld.AltsGreeter";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<io.github.hundanli.grpc.alts.AltsRequest,
      io.github.hundanli.grpc.alts.AltsReply> getSayHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sayHello",
      requestType = io.github.hundanli.grpc.alts.AltsRequest.class,
      responseType = io.github.hundanli.grpc.alts.AltsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.github.hundanli.grpc.alts.AltsRequest,
      io.github.hundanli.grpc.alts.AltsReply> getSayHelloMethod() {
    io.grpc.MethodDescriptor<io.github.hundanli.grpc.alts.AltsRequest, io.github.hundanli.grpc.alts.AltsReply> getSayHelloMethod;
    if ((getSayHelloMethod = AltsGreeterGrpc.getSayHelloMethod) == null) {
      synchronized (AltsGreeterGrpc.class) {
        if ((getSayHelloMethod = AltsGreeterGrpc.getSayHelloMethod) == null) {
          AltsGreeterGrpc.getSayHelloMethod = getSayHelloMethod =
              io.grpc.MethodDescriptor.<io.github.hundanli.grpc.alts.AltsRequest, io.github.hundanli.grpc.alts.AltsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sayHello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.github.hundanli.grpc.alts.AltsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.github.hundanli.grpc.alts.AltsReply.getDefaultInstance()))
              .setSchemaDescriptor(new AltsGreeterMethodDescriptorSupplier("sayHello"))
              .build();
        }
      }
    }
    return getSayHelloMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AltsGreeterStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AltsGreeterStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AltsGreeterStub>() {
        @java.lang.Override
        public AltsGreeterStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AltsGreeterStub(channel, callOptions);
        }
      };
    return AltsGreeterStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AltsGreeterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AltsGreeterBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AltsGreeterBlockingStub>() {
        @java.lang.Override
        public AltsGreeterBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AltsGreeterBlockingStub(channel, callOptions);
        }
      };
    return AltsGreeterBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AltsGreeterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AltsGreeterFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AltsGreeterFutureStub>() {
        @java.lang.Override
        public AltsGreeterFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AltsGreeterFutureStub(channel, callOptions);
        }
      };
    return AltsGreeterFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static abstract class AltsGreeterImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public void sayHello(io.github.hundanli.grpc.alts.AltsRequest request,
        io.grpc.stub.StreamObserver<io.github.hundanli.grpc.alts.AltsReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSayHelloMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSayHelloMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                io.github.hundanli.grpc.alts.AltsRequest,
                io.github.hundanli.grpc.alts.AltsReply>(
                  this, METHODID_SAY_HELLO)))
          .build();
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class AltsGreeterStub extends io.grpc.stub.AbstractAsyncStub<AltsGreeterStub> {
    private AltsGreeterStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AltsGreeterStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AltsGreeterStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public void sayHello(io.github.hundanli.grpc.alts.AltsRequest request,
        io.grpc.stub.StreamObserver<io.github.hundanli.grpc.alts.AltsReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class AltsGreeterBlockingStub extends io.grpc.stub.AbstractBlockingStub<AltsGreeterBlockingStub> {
    private AltsGreeterBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AltsGreeterBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AltsGreeterBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public io.github.hundanli.grpc.alts.AltsReply sayHello(io.github.hundanli.grpc.alts.AltsRequest request) {
      return blockingUnaryCall(
          getChannel(), getSayHelloMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class AltsGreeterFutureStub extends io.grpc.stub.AbstractFutureStub<AltsGreeterFutureStub> {
    private AltsGreeterFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AltsGreeterFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AltsGreeterFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<io.github.hundanli.grpc.alts.AltsReply> sayHello(
        io.github.hundanli.grpc.alts.AltsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SAY_HELLO = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AltsGreeterImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AltsGreeterImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SAY_HELLO:
          serviceImpl.sayHello((io.github.hundanli.grpc.alts.AltsRequest) request,
              (io.grpc.stub.StreamObserver<io.github.hundanli.grpc.alts.AltsReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class AltsGreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AltsGreeterBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.github.hundanli.grpc.alts.AltsProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AltsGreeter");
    }
  }

  private static final class AltsGreeterFileDescriptorSupplier
      extends AltsGreeterBaseDescriptorSupplier {
    AltsGreeterFileDescriptorSupplier() {}
  }

  private static final class AltsGreeterMethodDescriptorSupplier
      extends AltsGreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AltsGreeterMethodDescriptorSupplier(String methodName) {
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
      synchronized (AltsGreeterGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AltsGreeterFileDescriptorSupplier())
              .addMethod(getSayHelloMethod())
              .build();
        }
      }
    }
    return result;
  }
}
