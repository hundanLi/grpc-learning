package io.github.hundanli.grpc.alts;

import io.grpc.ManagedChannel;
import io.grpc.alts.AltsChannelBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/17 17:01
 */
public class AltsGrpcClient {
    private static final Logger logger = Logger.getLogger(AltsGrpcClient.class.getName());
    private String serverAddress = "localhost:10001";

    public static void main(String[] args) throws InterruptedException {
        new AltsGrpcClient().run(args);
    }

    private void parseArgs(String[] args) {
        boolean usage = false;
        for (String arg : args) {
            if (!arg.startsWith("--")) {
                System.err.println("All arguments must start with '--': " + arg);
                usage = true;
                break;
            }
            String[] parts = arg.substring(2).split("=", 2);
            String key = parts[0];
            if ("help".equals(key)) {
                usage = true;
                break;
            }
            if (parts.length != 2) {
                System.err.println("All arguments must be of the form --arg=value");
                usage = true;
                break;
            }
            String value = parts[1];
            if ("server".equals(key)) {
                serverAddress = value;
            } else {
                System.err.println("Unknown argument: " + key);
                usage = true;
                break;
            }
        }
        if (usage) {
            AltsGrpcClient c = new AltsGrpcClient();
            System.out.println(
                    "Usage: [ARGS...]"
                            + "\n"
                            + "\n  --server=SERVER_ADDRESS        Server address to connect to. Default "
                            + c.serverAddress);
            System.exit(1);
        }
    }

    private void run(String[] args) throws InterruptedException {
        parseArgs(args);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        ManagedChannel channel = AltsChannelBuilder.forTarget(serverAddress).executor(executor).build();
        try {
            AltsGreeterGrpc.AltsGreeterBlockingStub stub = AltsGreeterGrpc.newBlockingStub(channel);
            AltsReply reply = stub.sayHello(AltsRequest.newBuilder().setName("Waldo").build());

            logger.log(Level.INFO, "Got {0}", reply);
        } finally {
            channel.shutdown();
            channel.awaitTermination(1, TimeUnit.SECONDS);
            // Wait until the channel has terminated, since tasks can be queued after the channel is
            // shutdown.
            executor.shutdown();
        }
    }
}
