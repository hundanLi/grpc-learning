package io.github.hundanli.grpc.helloworld;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/16 20:10
 */
public class HelloGrpcServer {
    private static final Logger LOGGER = Logger.getLogger(HelloGrpcServer.class.getName());
    private Server server;

    private void start(int port) throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new HelloServiceImpl())
                .build().start();
        LOGGER.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                LOGGER.warning("*** shutting down gRPC server since JVM is shutting down");
                try {
                    HelloGrpcServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                LOGGER.warning("*** server shutdown");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUtilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HelloGrpcServer server = new HelloGrpcServer();
        server.start(9090);
        server.blockUtilShutdown();
    }

    static class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            String name = request.getName();
            LOGGER.info("Receiving greeting from name: " + name);
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello, " + name).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
