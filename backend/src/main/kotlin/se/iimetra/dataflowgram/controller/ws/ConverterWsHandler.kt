package se.iimetra.dataflowgram.controller.ws

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import se.iimetra.dataflow.*
import se.iimetra.dataflowgram.root.RootDispatcher
import se.iimetra.dataflowgram.root.ValueTypePair
import java.util.concurrent.CompletableFuture

class ConverterWsHandler(val dispatcher: RootDispatcher) {

  @UseExperimental(ImplicitReflectionSerializer::class)
  fun processConverterRequest(session: WebSocketSession, content: String) = GlobalScope.future {
    val objContent = Json.plain.parse<ConverterEventRequest>(content)
    val converterFunc = connectors[objContent.systemFunctionId]!!
    process(converterFunc, objContent.arg)
      .thenAccept { serverResult ->
        val converterResponse = ConverterEventResponse(objContent.systemFunctionId, objContent.executionPanelId, objContent.blockIndex, serverResult.value, null)
        val response = WsResponse("converter", kotlinx.serialization.json.Json.stringify(converterResponse))
        session.send(Mono.just(session.textMessage(Json.stringify(response)))).block()
        response
      }.exceptionally { exception ->
        val converterResponse = ConverterEventResponse(objContent.systemFunctionId, objContent.executionPanelId, objContent.blockIndex, null, exception.message)
        val response = WsResponse("converter", kotlinx.serialization.json.Json.stringify(converterResponse))
        session.send(Mono.just(session.textMessage(Json.stringify(response)))).block()
      }
  }

  private suspend fun process(func: SystemFunction, arg: String): CompletableFuture<ValueTypePair> {
    return if (func.params["from"] == "server") {
      dispatcher.outData(arg, func.functionSignature.output)
    } else {
      dispatcher.inData(arg, func.functionSignature.output)
    }
  }
}