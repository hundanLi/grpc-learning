package io.github.hundanli.grpc.streaming;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/17 15:00
 */
public class StreamGrpcClient {
    private static final Logger LOGGER = Logger.getLogger(StreamGrpcClient.class.getName());

    private final StreamServiceGrpc.StreamServiceStub stub;

    public StreamGrpcClient(Channel channel) {
        stub = StreamServiceGrpc.newStub(channel);
    }

    public static void main(String[] args) throws InterruptedException {

        String target = "localhost:8080";
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.err.println("Usage: [target]");
                System.err.println("");
                System.err.println("  target  The server to connect to. Defaults to " + target);
                System.exit(1);
            }
            target = args[0];
        }
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(target)
                .usePlaintext()
                .build();
        try {
            // 构造client
            StreamGrpcClient client = new StreamGrpcClient(channel);
            client.sayHello(names());
        } finally {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private void sayHello(List<String> names) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Random random = new Random();
        // 先定义响应的处理逻辑
        StreamObserver<StreamReply> responseObserver = new StreamObserver<StreamReply>() {
            @Override
            public void onNext(StreamReply streamReply) {
                LOGGER.info("Received reply:" + streamReply.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                LOGGER.warning("Send failed:" + Status.fromThrowable(throwable));
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                LOGGER.info("Finish send");
                latch.countDown();
            }
        };

        // rpc调用
        StreamObserver<StreamRequest> requestObserver = stub.sayHello(responseObserver);
        try {
            for (String name : names) {
                // 发送逻辑
                requestObserver.onNext(StreamRequest.newBuilder().setName(name).build());
                Thread.sleep(random.nextInt(100) + 100);
                if (latch.getCount() == 0) {
                    return;
                }
            }
        } catch (Exception e) {
            requestObserver.onError(e);
            throw e;
        }
        requestObserver.onCompleted();
        if (!latch.await(1, TimeUnit.MINUTES)) {
            LOGGER.warning("Cannot finish within 1 minute.");
        }
    }

    private static List<String> names() {
        return Arrays.asList(
                "Sophia",
                "Jackson",
                "Emma",
                "Aiden",
                "Olivia",
                "Lucas",
                "Ava",
                "Liam",
                "Mia",
                "Noah",
                "Isabella",
                "Ethan",
                "Riley",
                "Mason",
                "Aria",
                "Caden",
                "Zoe",
                "Oliver",
                "Charlotte",
                "Elijah",
                "Lily",
                "Grayson",
                "Layla",
                "Jacob",
                "Amelia",
                "Michael",
                "Emily"
        );
    }
}
