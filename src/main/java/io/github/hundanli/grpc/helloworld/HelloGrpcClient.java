package io.github.hundanli.grpc.helloworld;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/16 20:41
 */
public class HelloGrpcClient {
    private static final Logger LOGGER = Logger.getLogger(HelloGrpcClient.class.getName());

    private final HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

    public HelloGrpcClient(Channel channel) {
        this.blockingStub = HelloServiceGrpc.newBlockingStub(channel);
    }

    public void greet(String name) {
        LOGGER.info("Try to greet " + name + "...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply reply;
        try {
            reply = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            LOGGER.warning("RPC failed: " + e.getStatus());
            return;
        }
        LOGGER.info("Greeting: " + reply.getMessage());
    }

    public static void main(String[] args) throws InterruptedException {
        String name = "grpc";
        String target = "localhost:9090";
        // 传参
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.err.println("Usage: [name [target]]");
                System.err.println("");
                System.err.println("  name    The name you wish to be greeted by. Defaults to " + name);
                System.err.println("  target  The server to connect to. Defaults to " + target);
                System.exit(1);
            }
            name = args[0];
        }
        if (args.length > 1) {
            target = args[1];
        }
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
        try {
            HelloGrpcClient client = new HelloGrpcClient(channel);
            client.greet(name);

        } finally {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
