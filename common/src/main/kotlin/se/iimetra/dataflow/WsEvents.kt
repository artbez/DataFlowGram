package se.iimetra.dataflow

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class WsRequest(val eventType: String, val content: String)

@Serializable
data class WsResponse(val eventType: String, val content: String)

@Serializable
data class ServerEventRequest(
  val functionId: FunctionId,
  val arguments: List<String>,
  val version: Long,
  val executionPanelId: Long,
  val blockIndex: Int
)

@Serializable
data class ServerMsgEventResponse(
  val executionPanelId: Long,
  val blockIndex: Int,
  val msg: String
)

@Serializable
data class ServerResultEventResponse(
  val executionPanelId: Long,
  val blockIndex: Int,
  val ref: String
)

