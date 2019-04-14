package se.iimetra.dataflowgram.controller.ws

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflow.WsRequest
import se.iimetra.dataflow.WsResponse
import se.iimetra.dataflowgram.git.GitListener
import java.util.concurrent.CopyOnWriteArrayList

class TrainWSHandler : GitListener, WebSocketHandler {

  @Volatile
  private var currentConfig: GitContent = GitContent(-1, emptyList())

  private val sessions = CopyOnWriteArrayList<WebSocketSession>()

  @ImplicitReflectionSerializer
  override suspend fun parseUpdate(newContent: GitContent) {
    currentConfig = newContent
    val wsResponse = WsResponse("config", Json.plain.stringify(currentConfig))
    sessions.forEach { session -> session.send(Mono.just(session.textMessage(Json.stringify(wsResponse)))).block() }
  }

  private val logger = LoggerFactory.getLogger(TrainWSHandler::class.java)

  @UseExperimental(ImplicitReflectionSerializer::class)
  override fun handle(session: WebSocketSession): Mono<Void> {
    return session
      .receive()
      .map {
        val payload = it.payloadAsText
        logger.info(payload)
        Json.parse(WsRequest.serializer(), payload)
      }
      .map { request ->
        when (request.eventType) {
          "config" -> processConfigRequest(session)
          else -> throw Exception()
        }.thenAccept {
          session.send(Mono.just(session.textMessage(Json.stringify(it)))).block()
        }
        Mono.`when`()
      }.flatMap { it }.toMono()
  }

  @UseExperimental(ImplicitReflectionSerializer::class)
  private fun processConfigRequest(session: WebSocketSession) = GlobalScope.future {
    sessions.add(session)
    WsResponse("config", Json.plain.stringify(currentConfig))
  }
}