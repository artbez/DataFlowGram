package se.iimetra.dataflowgram.worker

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import se.iimetra.dataflow.FunctionId
import se.iimetra.dataflow.FunctionSignature
import se.iimetra.dataflowgram.root.ValueTypePair
import se.iimetra.dataflowgram.worker.handlers.UpdateLocation
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

  fun update(repo: String, fileOut: String, userDir: String, locations: List<UpdateLocation>) {
    val requestBuilder = Connector.Update.newBuilder()
    requestBuilder.repo = repo
    requestBuilder.fileOut = fileOut
    requestBuilder.userDir = userDir
    requestBuilder.addAllLocations(locations.map {
      Connector.FileLocation.newBuilder()
        .setCategory(it.category)
        .setFile(it.file)
        .setLanguage(it.language)
        .build()
    })

    try {
      blockingStub.updateLib(requestBuilder.build())
    } catch (e: StatusRuntimeException) {
      logger.error("RPC failed: {0}", e.status)
      throw RuntimeException(e.status.description)
    }

    logger.info("Updated")
  }

  fun outCommand(ref: String, type: String): String {
    val request = Connector.OutRequest.newBuilder().setRef(ref).setType(type).build()
    val response = blockingStub.outData(request)
    return response.value
  }

  fun inCommand(data: ValueTypePair): String {
    val request = Connector.InRequest.newBuilder().setType(data.type).setValue(data.value).build()
    val response = blockingStub.inData(request)
    return response.ref
  }

  fun executeDefaultCommand(function: String, params: Map<String, String>): String {
    val requestBuilder = Connector.ExecutionRequest.newBuilder()
      .setFunction(function)
      .putAllParams(params)

    val request = requestBuilder.build()
    val response = blockingStub.execute(request)

    response.forEach {
      if (it.valueCase == Connector.ExecutionResult.ValueCase.MSG) {
        //onMessageReceive(it.msg)
      } else {
        return it.ref
      }
    }

    throw IllegalStateException("Should not be here")
  }

  fun executeCommand(
    functionMeta: FunctionId,
    signature: FunctionSignature,
    args: List<String>, params: Map<String, String>,
    onMessageReceive: (String) -> Unit
  ): String {
    val requestBuilder = Connector.ExecutionRequest.newBuilder()
      .setCategory(functionMeta.category)
      .setFile(functionMeta.file)
      .setLanguage(functionMeta.language)
      .setTargetType(signature.output)
      .setFunction(functionMeta.name)
      .addAllArgs(args)
      .putAllParams(params)

    val request = requestBuilder.build()

    val response = try {
      blockingStub.execute(request)
    } catch (e: StatusRuntimeException) {
      logger.error("RPC failed: {0}", e.status)
      throw RuntimeException(e.status.description)
    }

    response.forEach {
      if (it.valueCase == Connector.ExecutionResult.ValueCase.MSG) {
        onMessageReceive(it.msg)
      } else {
        return it.ref
      }
    }

    throw IllegalStateException("Should not be here")
  }

  fun removeAll() {
    val request = Connector.RemoveRequest.newBuilder().setAll(true).build()
    try {
      blockingStub.remove(request)
    } catch (e: StatusRuntimeException) {
      logger.error("RPC failed: {0}", e.status)
      throw RuntimeException(e.status.description)
    }
  }

  fun removeRef(ref: String) {
    val request = Connector.RemoveRequest.newBuilder().setRef(ref).build()
    try {
      blockingStub.remove(request)
    } catch (e: StatusRuntimeException) {
      logger.error("RPC failed: {0}", e.status)
      throw RuntimeException(e.status.description)
    }
  }


  companion object {
    private val logger = LoggerFactory.getLogger(PythonServerClient::class.java.name)
  }
}