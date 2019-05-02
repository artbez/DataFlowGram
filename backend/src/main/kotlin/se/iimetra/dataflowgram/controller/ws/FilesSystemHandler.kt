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

class FilesSystemHandler {
  @Volatile
  private var currectFileSysem: UserFilesInfo = UserFilesInfo(emptyList())
  private val sessions = CopyOnWriteArrayList<WebSocketSession>()

  @UseExperimental(ImplicitReflectionSerializer::class)
  fun parseUpdate(newInfo: UserFilesInfo) {
    currectFileSysem = newInfo
    val wsResponse = WsResponse("files", Json.plain.stringify(newInfo))
    sessions.forEach { session -> session.send(Mono.just(session.textMessage(Json.stringify(wsResponse)))).block() }
  }

  @UseExperimental(ImplicitReflectionSerializer::class)
  fun processFilesRequest(session: WebSocketSession) = GlobalScope.future {
    sessions.add(session)
    val response = WsResponse("files", Json.plain.stringify(currectFileSysem))
    session.send(Mono.just(session.textMessage(Json.stringify(response)))).block()
    WsResponse("config", Json.plain.stringify(currectFileSysem))
  }
}