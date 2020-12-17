# RPC学习笔记

## 1.grpc-java

### 1.1 快速入门

#### 1.1.1 定义Service

创建maven或者gradle项目，在src/main/proto目录下新建helloworld.proto文件：

```protobuf
syntax = "proto3";

option java_multiple_files = true;
// 生成的源码包名
option java_package = "io.github.hundanli.grpc.helloworld";
option java_outer_classname = "HelloWorldProto";
option objc_class_prefix = "HLW";

// 定义服务
service HelloService {
  // 定义rpc api
  rpc sayHello(HelloRequest) returns (HelloReply) {}
}

// 定义序列化数据类型
message HelloRequest {
  string name = 1;
}

message HelloReply {
  string message = 1;
}

```

#### 1.1.2 代码生成

##### 1.maven

修改pom.xml，添加依赖和编译插件

```xml

<dependencies>
    <dependency>
        <groupId>io.grpc</groupId>
        <artifactId>grpc-netty-shaded</artifactId>
        <version>1.34.1</version>
    </dependency>
    <dependency>
        <groupId>io.grpc</groupId>
        <artifactId>grpc-protobuf</artifactId>
        <version>1.34.1</version>
    </dependency>
    <dependency>
        <groupId>io.grpc</groupId>
        <artifactId>grpc-stub</artifactId>
        <version>1.34.1</version>
    </dependency>
    <dependency> <!-- necessary for Java 9+ -->
        <groupId>org.apache.tomcat</groupId>
        <artifactId>annotations-api</artifactId>
        <version>6.0.53</version>
        <scope>provided</scope>
    </dependency>
</dependencies>


<build>
    <extensions>
        <extension>
            <groupId>kr.motd.maven</groupId>
            <artifactId>os-maven-plugin</artifactId>
            <version>1.6.2</version>
        </extension>
    </extensions>
    <plugins>
        <plugin>
            <groupId>org.xolstice.maven.plugins</groupId>
            <artifactId>protobuf-maven-plugin</artifactId>
            <version>0.6.1</version>
            <configuration>
                <protocArtifact>com.google.protobuf:protoc:3.12.0:exe:${os.detected.classifier}</protocArtifact>
                <pluginId>grpc-java</pluginId>
                <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.34.1:exe:${os.detected.classifier}</pluginArtifact>
                <!--设置grpc生成代码到指定路径-->
                <outputDirectory>${project.build.sourceDirectory}</outputDirectory>
                <!--生成代码前是否清空目录-->
                <clearOutputDirectory>false</clearOutputDirectory>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                        <goal>compile-custom</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

执行以下命令生成命令：

```bash
mvn protobuf:compile-custom
```



**将生成的代码与自主编写的代码分离**：

修改pom.xml的protobuf-maven-plugin插件配置，并添加build-helper-maven-plugin插件：

```xml
<plugins>
    <plugin>
        <groupId>org.xolstice.maven.plugins</groupId>
        <artifactId>protobuf-maven-plugin</artifactId>
        <version>0.6.1</version>
        <configuration>
            <protocArtifact>com.google.protobuf:protoc:3.12.0:exe:${os.detected.classifier}</protocArtifact>
            <pluginId>grpc-java</pluginId>
            <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.34.1:exe:${os.detected.classifier}</pluginArtifact>
            <!--设置grpc生成代码到指定路径-->
            <outputDirectory>${project.basedir}/src/main/gen</outputDirectory>
            <!--生成代码前是否清空目录-->
            <clearOutputDirectory>false</clearOutputDirectory>
        </configuration>
        <executions>
            <execution>
                <goals>
                    <goal>compile</goal>
                    <goal>compile-custom</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

    <!-- 设置多个源文件夹 -->
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>build-helper-maven-plugin</artifactId>
        <version>3.0.0</version>
        <executions>
            <!-- 添加主源码目录 -->
            <execution>
                <id>add-source</id>
                <phase>generate-sources</phase>
                <goals>
                    <goal>add-source</goal>
                </goals>
                <configuration>
                    <sources>
                        <source>${project.basedir}/src/main/gen</source>
                        <source>${project.basedir}/src/main/java</source>
                    </sources>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```

执行命令：

```bash
mvn clean compile
```



##### 2.gradle

修改build.gradle，添加依赖和编译插件：

```groovy
plugins {
    id 'java'
    id 'com.google.protobuf' version '0.8.14'
    id 'idea'
}

group 'io.github.hunadnli'
version '1.0'

repositories {
    mavenLocal()
    maven {
        url 'https://maven.aliyun.com/repository/public/'
    }
    maven {
        url 'https://maven.aliyun.com/repository/google/'
    }
    maven {
        url 'https://maven.aliyun.com/repository/gradle-plugin'
    }
    mavenCentral()
}

dependencies {
    implementation 'io.grpc:grpc-netty-shaded:1.34.1'
    implementation 'io.grpc:grpc-protobuf:1.34.1'
    implementation 'io.grpc:grpc-stub:1.34.1'
    compileOnly 'org.apache.tomcat:annotations-api:6.0.53' // necessary for Java 9+
    testCompile group: 'junit', name: 'junit', version: '4.12'
}

protobuf {
    // 生成代码的路径
    generatedFilesBaseDir = "$projectDir/src/gen"
    protoc {
        artifact = "com.google.protobuf:protoc:3.12.0"
    }
    plugins {
        grpc {
            artifact = 'io.grpc:protoc-gen-grpc-java:1.34.1'
        }
    }
    generateProtoTasks {
        all()*.plugins {
            grpc {}
        }
    }
}

idea {
    // idea插件会自动将proto目录和生成的代码目录添加为源代码目录
    module {
        // proto files and generated Java files are automatically added as source dirs.
        // If you have additional sources, add them here:
    }
}
```

运行`./gradlew build`命令编译和生成代码。

##### 3.protoc

还可以使用[protoc编译工具](https://github.com/protocolbuffers/protobuf/releases)来生成代码：

```bash
#!/bin/bash
OUTPUT_DIR=src/main/java
DIR_OF_PROTO_FILE=src/main/proto
PROTO_FILE=helloworld.proto
protoc --plugin=protoc-gen-grpc-java \
                  --java_out="$OUTPUT_DIR" --proto_path="$DIR_OF_PROTO_FILE" "$PROTO_FILE"
```



#### 1.1.3 编写server和client

**HelloGrpcServer.java**：

```java
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

```

**HelloGrpcClient.java**：

```java
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

```

依次运行Server和Client即可。



### 1.2 流式RPC

GRPC支持单向/双向流式RPC，双方都可以使用读写流发送一系列消息。

#### 1.2.1 定义Service

streaming.proto:

```protobuf

syntax = "proto3";

option java_multiple_files = true;
option java_package = "io.github.hundanli.grpc.streaming";
option java_outer_classname = "StreamingProto";
option objc_class_prefix = "HLW";

service StreamService {
  rpc sayHello(stream StreamRequest) returns (stream StreamReply) {}
}

message StreamRequest {
  string name = 1;
}

message StreamReply {
  string message = 1;
}

```

#### 1.2.2 代码生成

```bash
mvn protobuf:compile
mvn protobuf:compile-custom
mvn compile
```

#### 1.2.3 编写server和client

StreamGrpcServer.java:

```java
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

```

StreamGrpcClient.java:

```java
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

```







