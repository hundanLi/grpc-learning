package io.github.hundanli.grpc.streaming;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/17 15:00
 */
public class StreamGrpcServer {

    private static final Logger LOGGER = Logger.getLogger(StreamGrpcServer.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        // 构造server服务器并启动
        final Server server = ServerBuilder
                .forPort(port)
                .addService(new StreamServiceImpl())
                .build().start();
        LOGGER.info("Server started. Listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("Shutting down");
                try {
                    server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        });
        server.awaitTermination();
    }

    static class StreamServiceImpl extends StreamServiceGrpc.StreamServiceImplBase {
        @Override
        public StreamObserver<StreamRequest> sayHello(StreamObserver<StreamReply> responseObserver) {
            return new StreamObserver<StreamRequest>() {

                @Override
                public void onNext(StreamRequest streamRequest) {
                    String name = streamRequest.getName();
                    LOGGER.info("Received hello from " + name);
                    // 写回响应
                    responseObserver.onNext(StreamReply.newBuilder().setMessage("Hello, " + name).build());
                }

                @Override
                public void onError(Throwable throwable) {
                    LOGGER.warning(throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    LOGGER.info("Completed");
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
