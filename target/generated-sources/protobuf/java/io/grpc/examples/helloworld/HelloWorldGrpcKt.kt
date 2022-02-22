package io.grpc.examples.helloworld

import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.examples.helloworld.GreeterGrpc.getServiceDescriptor
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls.bidiStreamingRpc
import io.grpc.kotlin.ClientCalls.serverStreamingRpc
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls.bidiStreamingServerMethodDefinition
import io.grpc.kotlin.ServerCalls.serverStreamingServerMethodDefinition
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.String
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.coroutines.flow.Flow

/**
 * Holder for Kotlin coroutine-based client and server APIs for io.grpc.examples.helloworld.Greeter.
 */
object GreeterGrpcKt {
  const val SERVICE_NAME: String = GreeterGrpc.SERVICE_NAME

  @JvmStatic
  val serviceDescriptor: ServiceDescriptor
    get() = GreeterGrpc.getServiceDescriptor()

  val sayHelloMethod: MethodDescriptor<HelloRequest, HelloReply>
    @JvmStatic
    get() = GreeterGrpc.getSayHelloMethod()

  val sayHelloStreamMethod: MethodDescriptor<HelloRequest, HelloReply>
    @JvmStatic
    get() = GreeterGrpc.getSayHelloStreamMethod()

  val biDiSayHelloStreamMethod: MethodDescriptor<HelloRequest, HelloReply>
    @JvmStatic
    get() = GreeterGrpc.getBiDiSayHelloStreamMethod()

  /**
   * A stub for issuing RPCs to a(n) io.grpc.examples.helloworld.Greeter service as suspending
   * coroutines.
   */
  @StubFor(GreeterGrpc::class)
  class GreeterCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT
  ) : AbstractCoroutineStub<GreeterCoroutineStub>(channel, callOptions) {
    override fun build(channel: Channel, callOptions: CallOptions): GreeterCoroutineStub =
        GreeterCoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    suspend fun sayHello(request: HelloRequest, headers: Metadata = Metadata()): HelloReply =
        unaryRpc(
      channel,
      GreeterGrpc.getSayHelloMethod(),
      request,
      callOptions,
      headers
    )
    /**
     * Returns a [Flow] that, when collected, executes this RPC and emits responses from the
     * server as they arrive.  That flow finishes normally if the server closes its response with
     * [`Status.OK`][Status], and fails by throwing a [StatusException] otherwise.  If
     * collecting the flow downstream fails exceptionally (including via cancellation), the RPC
     * is cancelled with that exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return A flow that, when collected, emits the responses from the server.
     */
    fun sayHelloStream(request: HelloRequest, headers: Metadata = Metadata()): Flow<HelloReply> =
        serverStreamingRpc(
      channel,
      GreeterGrpc.getSayHelloStreamMethod(),
      request,
      callOptions,
      headers
    )
    /**
     * Returns a [Flow] that, when collected, executes this RPC and emits responses from the
     * server as they arrive.  That flow finishes normally if the server closes its response with
     * [`Status.OK`][Status], and fails by throwing a [StatusException] otherwise.  If
     * collecting the flow downstream fails exceptionally (including via cancellation), the RPC
     * is cancelled with that exception as a cause.
     *
     * The [Flow] of requests is collected once each time the [Flow] of responses is
     * collected. If collection of the [Flow] of responses completes normally or
     * exceptionally before collection of `requests` completes, the collection of
     * `requests` is cancelled.  If the collection of `requests` completes
     * exceptionally for any other reason, then the collection of the [Flow] of responses
     * completes exceptionally for the same reason and the RPC is cancelled with that reason.
     *
     * @param requests A [Flow] of request messages.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return A flow that, when collected, emits the responses from the server.
     */
    fun biDiSayHelloStream(requests: Flow<HelloRequest>, headers: Metadata = Metadata()):
        Flow<HelloReply> = bidiStreamingRpc(
      channel,
      GreeterGrpc.getBiDiSayHelloStreamMethod(),
      requests,
      callOptions,
      headers
    )}

  /**
   * Skeletal implementation of the io.grpc.examples.helloworld.Greeter service based on Kotlin
   * coroutines.
   */
  abstract class GreeterCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for io.grpc.examples.helloworld.Greeter.SayHello.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun sayHello(request: HelloRequest): HelloReply = throw
        StatusException(UNIMPLEMENTED.withDescription("Method io.grpc.examples.helloworld.Greeter.SayHello is unimplemented"))

    /**
     * Returns a [Flow] of responses to an RPC for
     * io.grpc.examples.helloworld.Greeter.SayHelloStream.
     *
     * If creating or collecting the returned flow fails with a [StatusException], the RPC
     * will fail with the corresponding [Status].  If it fails with a
     * [java.util.concurrent.CancellationException], the RPC will fail with status
     * `Status.CANCELLED`.  If creating
     * or collecting the returned flow fails for any other reason, the RPC will fail with
     * `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open fun sayHelloStream(request: HelloRequest): Flow<HelloReply> = throw
        StatusException(UNIMPLEMENTED.withDescription("Method io.grpc.examples.helloworld.Greeter.SayHelloStream is unimplemented"))

    /**
     * Returns a [Flow] of responses to an RPC for
     * io.grpc.examples.helloworld.Greeter.BiDiSayHelloStream.
     *
     * If creating or collecting the returned flow fails with a [StatusException], the RPC
     * will fail with the corresponding [Status].  If it fails with a
     * [java.util.concurrent.CancellationException], the RPC will fail with status
     * `Status.CANCELLED`.  If creating
     * or collecting the returned flow fails for any other reason, the RPC will fail with
     * `Status.UNKNOWN` with the exception as a cause.
     *
     * @param requests A [Flow] of requests from the client.  This flow can be
     *        collected only once and throws [java.lang.IllegalStateException] on attempts to
     * collect
     *        it more than once.
     */
    open fun biDiSayHelloStream(requests: Flow<HelloRequest>): Flow<HelloReply> = throw
        StatusException(UNIMPLEMENTED.withDescription("Method io.grpc.examples.helloworld.Greeter.BiDiSayHelloStream is unimplemented"))

    final override fun bindService(): ServerServiceDefinition = builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = GreeterGrpc.getSayHelloMethod(),
      implementation = ::sayHello
    ))
      .addMethod(serverStreamingServerMethodDefinition(
      context = this.context,
      descriptor = GreeterGrpc.getSayHelloStreamMethod(),
      implementation = ::sayHelloStream
    ))
      .addMethod(bidiStreamingServerMethodDefinition(
      context = this.context,
      descriptor = GreeterGrpc.getBiDiSayHelloStreamMethod(),
      implementation = ::biDiSayHelloStream
    )).build()
  }
}
