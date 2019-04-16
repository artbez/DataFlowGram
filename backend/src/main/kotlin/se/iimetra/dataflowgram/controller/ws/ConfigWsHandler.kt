package se.iimetra.dataflowgram.controller.ws

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflow.WsResponse
import se.iimetra.dataflowgram.git.GitListener
import java.util.concurrent.CopyOnWriteArrayList

class ConfigWsHandler : GitListener {
  @Volatile
  private var currentConfig: GitContent = GitContent(-1, emptyList())

  private val sessions = CopyOnWriteArrayList<WebSocketSession>()

  @ImplicitReflectionSerializer
  override suspend fun parseUpdate(newContent: GitContent) {
    currentConfig = newContent
    val wsResponse = WsResponse("config", Json.plain.stringify(currentConfig))
    sessions.forEach { session -> session.send(Mono.just(session.textMessage(Json.stringify(wsResponse)))).block() }
  }

  @UseExperimental(ImplicitReflectionSerializer::class)
  fun processConfigRequest(session: WebSocketSession) = GlobalScope.future {
    sessions.add(session)
    val response = WsResponse("config", Json.plain.stringify(currentConfig))
    session.send(Mono.just(session.textMessage(Json.stringify(response)))).block()
    WsResponse("config", Json.plain.stringify(currentConfig))
  }
}