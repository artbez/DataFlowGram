package se.iimetra.dataflowgram.worker

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import se.iimetra.dataflow.FunctionId
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

  fun getJson(ref: String): String {
    val request = Connector.OutRequest.newBuilder().setRef(ref).build()
    val response = blockingStub.out(request)
    return response.json
  }

  fun executeDefaultCommand(function: String, params: Map<String, String>): String {
    val requestBuilder = Connector.ExecutionRequest.newBuilder()
      .setFunction(function)
      .putAllParams(params)

    val request = requestBuilder.build()
    val response = blockingStub.execute(request)
    return response.ref
  }

  fun executeCommand(functionMeta: FunctionId, args: List<String>, params: Map<String, String>): String {
    val requestBuilder = Connector.ExecutionRequest.newBuilder()
      .setFunction(fullName(functionMeta.category, functionMeta.file, functionMeta.name))
      .addAllArgs(args)
      .putAllParams(params)

    val request = requestBuilder.build()

    val response = try {
      blockingStub.execute(request)
    } catch (e: StatusRuntimeException) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.status)
      throw Exception()
    }

    return response.ref
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