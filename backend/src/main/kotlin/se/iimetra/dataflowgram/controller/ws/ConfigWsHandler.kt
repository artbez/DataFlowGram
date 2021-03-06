package se.iimetra.dataflowgram.controller.ws

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import se.iimetra.dataflow.*
import java.util.concurrent.CopyOnWriteArrayList

class ConfigWsHandler {
  @Volatile
  private var currentConfig: GitContent
      = GitContent(-1, SpaceContent(emptyList()), SpaceContent(emptyList()), SpaceContent(emptyList()))

  private val sessions = CopyOnWriteArrayList<WebSocketSession>()

  @UseExperimental(ImplicitReflectionSerializer::class)
  fun parseUpdate(newContent: GitContent) {
    currentConfig = newContent
    val wsResponse = WsResponse("config", Json.plain.stringify(AllFunctions(currentConfig, connectors.values.toList())))
    sessions.forEach { session -> session.send(Mono.just(session.textMessage(Json.stringify(wsResponse)))).block() }
  }

  @UseExperimental(ImplicitReflectionSerializer::class)
  fun processConfigRequest(session: WebSocketSession) = GlobalScope.future {
    sessions.add(session)
    val response = WsResponse("config", Json.plain.stringify(AllFunctions(currentConfig, connectors.values.toList())))
    session.send(Mono.just(session.textMessage(Json.stringify(response)))).block()
    WsResponse("config", Json.plain.stringify(AllFunctions(currentConfig, connectors.values.toList())))
  }
}

val connectors = listOf(
  SystemFunction(FunctionSignature(listOf("Json"), "Json"), "Json up", "json_up_converter", mapOf("from" to "server")),
  SystemFunction(FunctionSignature(listOf("Json"), "Json"), "Json down", "json_down_converter", mapOf("from" to "client")),
  SystemFunction(FunctionSignature(listOf("Int"), "Int"), "Int up", "int_up_converter", mapOf("from" to "server")),
  SystemFunction(FunctionSignature(listOf("Int"), "Int"), "Int down", "int_converter", mapOf("from" to "client")),
  SystemFunction(FunctionSignature(listOf("String"), "String"), "String up", "string_up_converter", mapOf("from" to "server")),
  SystemFunction(FunctionSignature(listOf("String"), "String"), "String down", "string_down_converter", mapOf("from" to "client")),
  SystemFunction(FunctionSignature(listOf("Float"), "Float"), "Float up", "float_up_converter", mapOf("from" to "server")),
  SystemFunction(FunctionSignature(listOf("Float"), "Float"), "Float down", "float_down_converter", mapOf("from" to "client"))
).map { it.id to it }.toMap()