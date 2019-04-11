package se.iimetra.dataflowgram.worker

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

/**
 * A simple client that requests a greeting from the [HelloWorldServer].
 */
class HelloWorldClient
/** Construct client for accessing HelloWorld server using the existing channel.  */
internal constructor(private val channel: ManagedChannel) {
  private val blockingStub: GreeterGrpc.GreeterBlockingStub = GreeterGrpc.newBlockingStub(channel)

  /** Construct client connecting to HelloWorld server at `host:port`.  */
  constructor(host: String, port: Int) : this(
    ManagedChannelBuilder.forAddress(host, port)
      // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
      // needing certificates.
      .usePlaintext()
      .build()
  )

  @Throws(InterruptedException::class)
  fun shutdown() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }

  /** Say hello to server.  */
  fun greet(name: String): String {
    logger.info("Will try to greet $name ...")
    val request = Connector.HelloRequest.newBuilder().setName(name).build()
    val response: Connector.HelloReply
    try {
      response = blockingStub.sayHello(request)
    } catch (e: StatusRuntimeException) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.status)
      return "BAD!!!"
    }

    logger.info("Greeting: " + response.message)
    return response.message
  }

  companion object {
    private val logger = Logger.getLogger(HelloWorldClient::class.java.name)
  }
}