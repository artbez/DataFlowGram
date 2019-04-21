package se.iimetra.dataflowgram.controller.ws

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import se.iimetra.dataflow.WsRequest

class MainWSHandler(
  private val configWsHandler: ConfigWsHandler,
  private val serverEventWsHandler: ServerEventWsHandler,
  private val converterWsHandler: ConverterWsHandler
) : WebSocketHandler {

  private val logger = LoggerFactory.getLogger(MainWSHandler::class.java)

  @UseExperimental(ImplicitReflectionSerializer::class)
  override fun handle(session: WebSocketSession): Mono<Void> {
    return session
      .receive()
      .map {
        val payload = it.payloadAsText
        logger.info(payload)
        logger.info(session.id)
        Json.parse(WsRequest.serializer(), payload)
      }
      .map { request ->
        when (request.eventType) {
          "config" -> configWsHandler.processConfigRequest(session)
          "server" -> serverEventWsHandler.processServerRequest(session, request.content)
          "converter" -> converterWsHandler.processConverterRequest(session, request.content)
          else -> throw Exception()
        }.thenAccept {
          session.send(Mono.just(session.textMessage(Json.stringify(it)))).block()
        }
        Mono.`when`()
      }.flatMap { it }.toMono()
  }
}