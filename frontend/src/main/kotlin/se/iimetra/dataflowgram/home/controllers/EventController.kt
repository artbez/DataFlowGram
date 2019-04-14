package se.iimetra.dataflowgram.home.controllers

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.w3c.dom.WebSocket
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflow.WsRequest
import se.iimetra.dataflow.WsResponse
import se.iimetra.dataflowgram.home.configController
import kotlin.browser.window

@ImplicitReflectionSerializer
class EventController {
  init {
    val clientWebSocket = WebSocket("ws://localhost:9000/api/ws/all")
    clientWebSocket.onopen = {
      val rr = WsRequest("config", "")
      console.log(rr)
      clientWebSocket.send(Json.plain.stringify(rr))
    }
    clientWebSocket.onerror = {
      window.alert(it.asDynamic().data)
      clientWebSocket.close()
    }
    clientWebSocket.onmessage = {
      val data = it.asDynamic().data
      val answer = try {
        Json.nonstrict.parse(WsResponse.serializer(), data)
      } catch (e: Exception) {
        window.alert(data)
        clientWebSocket.close()
        throw e
      }
      when (answer.eventType) {

        "config" -> {
          val ans = Json.plain.parse<GitContent>(answer.content)
          configController.push(ans)
        }

        else -> TODO()
      }
    }
  }
}