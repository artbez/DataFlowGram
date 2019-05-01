package se.iimetra.dataflowgram.home.controllers

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.w3c.dom.WebSocket
import se.iimetra.dataflow.*
import se.iimetra.dataflowgram.home.configController
import se.iimetra.dataflowgram.home.converterController
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.serverEventController
import kotlin.browser.window

@ImplicitReflectionSerializer
class EventController {

  private val clientWebSocket: WebSocket = WebSocket("ws://localhost:9000/api/ws/all")

  init {
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
      console.log(data)
      val answer = try {
        Json.nonstrict.parse(WsResponse.serializer(), data)
      } catch (e: Exception) {
        window.alert(data)
        clientWebSocket.close()
        throw e
      }
      when (answer.eventType) {

        "config" -> {
          val ans = Json.plain.parse<AllFunctions>(answer.content)
          configController.push(ans)
        }

        "server" -> {
          val ans = Json.plain.parse<ServerResultEventResponse>(answer.content)
          serverEventController.push(ans)
        }

        "converter" -> {
          val ans = Json.plain.parse<ConverterEventResponse>(answer.content)
          converterController.push(ans)
        }
        else -> TODO()
      }
    }
  }

  fun pushServerEvent(function: FunctionDescription, args: List<String>, executionPanelId: Long, blockIndex: Int) {
    val serverEvent = ServerEventRequest(function.view.id, args, function.paramValues, function.meta.version, executionPanelId, blockIndex)
    val request = WsRequest("server", Json.stringify(serverEvent))
    clientWebSocket.send(Json.stringify(request))
  }

  fun pushConverterEvent(function: SystemFunction, arg: String, executionPanelId: Long, blockIndex: Int) {
    val converterEvent = ConverterEventRequest(function.id, executionPanelId, blockIndex, arg)
    val request = WsRequest("converter", Json.stringify(converterEvent))
    clientWebSocket.send(Json.stringify(request))
  }
}