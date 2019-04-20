package se.iimetra.dataflowgram.worker

import com.google.protobuf.Descriptors
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import se.iimetra.dataflow.FunctionId
import se.iimetra.dataflowgram.lib.fullName
import se.iimetra.dataflowgram.root.ValueTypePair
import java.lang.IllegalStateException
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

  fun executeCommand(functionMeta: FunctionId, args: List<String>, params: Map<String, String>, onMessageReceive: (String) -> Unit): String {
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

    response.forEach {
      if (it.valueCase == Connector.ExecutionResult.ValueCase.MSG) {
        onMessageReceive(it.msg)
      } else {
        return it.ref
      }
    }

    throw IllegalStateException("Should not be here")
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