gRPC Kotlin 1.5.31 Example (tested on Java 11 and 17)
-----------------------------
Initial example is from [kotlin-samples](https://github.com/GoogleCloudPlatform/kotlin-samples) 

Run Locally:
1. In one shell / terminal window, start the server:
    ```
    ./mvnw compile exec:java -D"exec.mainClass"="io.grpc.examples.helloworld.HelloWorldServerKt"
    ```
2. In another shell / terminal window, run the client:
    ```
    ./mvnw compile exec:java -D"exec.mainClass"="io.grpc.examples.helloworld.HelloWorldClientKt"
    ```
You should see output like: `Greeter client received: Hello world`