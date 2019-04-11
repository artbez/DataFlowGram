package se.iimetra.dataflowgram.worker

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import se.iimetra.dataflowgram.git.FunctionId
import se.iimetra.dataflowgram.lib.fullName
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger


class PythonServerClient internal constructor(private val channel: ManagedChannel) {
  private val blockingStub: ExecutorGrpc.ExecutorBlockingStub = ExecutorGrpc.newBlockingStub(channel)

  constructor(host: String, port: Int) : this(
    ManagedChannelBuilder.forAddress(host, port)
      .usePlaintext()
      .build()
  )

  @Throws(InterruptedException::class)
  fun shutdown() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }

  fun executeCommand(functionMeta: FunctionId, args: List<String>) {
    val requestBuilder = Connector.ExecutionRequest.newBuilder()
      .setFunction(fullName(functionMeta.category, functionMeta.file, functionMeta.name))
    args.forEachIndexed { index, s -> requestBuilder.setArgs(index, s) }
    val request = requestBuilder.build()

    val response = try {
      blockingStub.execute(request)
    } catch (e: StatusRuntimeException) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.status)
      throw Exception()
    }

    logger.info(response.message.toString())
  }

  fun update(): Boolean {
    val request = Connector.Update.newBuilder().build()//.setName(name).build()
    try {
      blockingStub.updateLib(request)
    } catch (e: StatusRuntimeException) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.status)
      return false
    }

    logger.info("Updated")
    return true
  }

  companion object {
    private val logger = Logger.getLogger(PythonServerClient::class.java.name)
  }
}