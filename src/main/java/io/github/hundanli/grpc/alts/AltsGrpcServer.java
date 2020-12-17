package io.github.hundanli.grpc.alts;

import io.grpc.Server;
import io.grpc.alts.AltsServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/17 16:52
 */
public class AltsGrpcServer extends AltsGreeterGrpc.AltsGreeterImplBase {

    private static final Logger LOGGER = Logger.getLogger(AltsGrpcServer.class.getName());

    private int port = 7070;

    public static void main(String[] args) throws IOException, InterruptedException {
        new AltsGrpcServer().start(args);
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
            if ("port".equals(key)) {
                port = Integer.parseInt(value);
            } else {
                System.err.println("Unknown argument: " + key);
                usage = true;
                break;
            }
        }
        if (usage) {
            AltsGrpcServer s = new AltsGrpcServer();
            System.out.println(
                    "Usage: [ARGS...]"
                            + "\n"
                            + "\n  --port=PORT           Server port to bind to. Default "
                            + s.port);
            System.exit(1);
        }
    }

    private void start(String[] args) throws IOException, InterruptedException {
        parseArgs(args);
        Server server = AltsServerBuilder.forPort(port)
                .addService(this)
                .executor(Executors.newFixedThreadPool(1))
                .build();
        server.start();
        LOGGER.log(Level.INFO, "Started on {0}", port);
        server.awaitTermination();
    }

    @Override
    public void sayHello(AltsRequest request, StreamObserver<AltsReply> responseObserver) {
        LOGGER.info("Received greeting from " + request.getName());
        responseObserver.onNext(AltsReply.newBuilder().setMessage("Hello, " + request.getName()).build());
        responseObserver.onCompleted();
    }
}
