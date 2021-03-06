package se.iimetra.dataflowgram.controller.ws

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import se.iimetra.dataflow.ServerEventRequest
import se.iimetra.dataflow.ServerResultEventResponse
import se.iimetra.dataflow.WsResponse
import se.iimetra.dataflowgram.root.RootDispatcher

class ServerEventWsHandler(val dispatcher: RootDispatcher) {


  @UseExperimental(ImplicitReflectionSerializer::class)
  fun processServerRequest(session: WebSocketSession, content: String) = GlobalScope.future {
    val objContent = Json.plain.parse<ServerEventRequest>(content)
    dispatcher.execute(objContent.functionId, objContent.arguments, objContent.version, objContent.paramValues) {
      val serverResult = ServerResultEventResponse(objContent.executionPanelId, objContent.blockIndex, it, null, null)
      val response = WsResponse("server", Json.stringify(serverResult))
      session.send(Mono.just(session.textMessage(Json.stringify(response)))).block()
      response
    }.thenAccept {
      val serverResult = ServerResultEventResponse(objContent.executionPanelId, objContent.blockIndex, null, it, null)
      val response = WsResponse("server", Json.stringify(serverResult))
      session.send(Mono.just(session.textMessage(Json.stringify(response)))).block()
      response
    }.exceptionally { exception ->
      val serverResult = ServerResultEventResponse(objContent.executionPanelId, objContent.blockIndex, null, null, exception.message)
      val response = WsResponse("server", Json.stringify(serverResult))
      session.send(Mono.just(session.textMessage(Json.stringify(response)))).block()
    }
  }
}