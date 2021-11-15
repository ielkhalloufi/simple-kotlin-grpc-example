gRPC Kotlin 1.5.31 Maven Example (tested on Java 11 and 17)
-----------------------------
Examples are from [kotlin-samples](https://github.com/GoogleCloudPlatform/kotlin-samples)

Run Locally:
1. In one shell / terminal window, start the server:
    ```
    ./mvnw compile exec:java -D"exec.mainClass"="io.grpc.examples.helloworld.HelloWorldServerKt"
    ```
2. In another shell / terminal window, run the client:
    ```
    ./mvnw compile exec:java -D"exec.mainClass"="io.grpc.examples.helloworld.HelloWorldClientKt"
    ```

Uncomment the last lines in the HelloWorldClient to test the different service functionalities (streaming, bidirectional streaming)