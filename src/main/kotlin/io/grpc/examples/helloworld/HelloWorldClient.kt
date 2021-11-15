/*
 * Copyright 2020 gRPC authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.grpc.examples.helloworld

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusException
import io.grpc.examples.helloworld.GreeterGrpcKt.GreeterCoroutineStub
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.io.Closeable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class HelloWorldClient(val channel: ManagedChannel) : Closeable {
    private val stub: GreeterCoroutineStub = GreeterCoroutineStub(channel)

    fun greet(s: String) = runBlocking {
        val request = helloRequest { name = s }
        try {
            val response = stub.sayHello(request)
            println("Greeter client received: ${response.message}")
        } catch (e: StatusException) {
            println("RPC failed: ${e.status}")
        }
    }

    fun streamGreet(s: String) = runBlocking {
        val request = helloRequest { name = s }
        val stream = stub.sayHelloStream(request)
        stream.collect { response ->
            println(response.message)
        }
    }

    suspend fun bidiGreet(producer: Flow<HelloRequest>): Unit = coroutineScope {
        stub.biDiSayHelloStream(producer).collect { helloResponse ->
            println(helloResponse.message)
        }
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

/**
 * Greeter, uses first argument as name to greet if present;
 * greets "world" otherwise.
 */
suspend fun main(args: Array<String>) {
    val isRemote = args.size == 1

    Executors.newFixedThreadPool(10).asCoroutineDispatcher().use { dispatcher ->
        val builder = when {
            isRemote -> ManagedChannelBuilder.forTarget(args[0].removePrefix("https://") + ":443").useTransportSecurity()
            else -> ManagedChannelBuilder.forTarget("localhost:50051").usePlaintext()
        }

        HelloWorldClient(
            builder.executor(dispatcher.asExecutor()).build()
        ).use {
            val user = args.singleOrNull() ?: "World"
            // it.greet(user)
            // it.streamGreet(user)

            val helloFlow = flow {
                while (true) {
                    delay(1000)
                    emit(helloRequest { name = user })
                }
            }
            it.bidiGreet(helloFlow)
        }
    }
}
