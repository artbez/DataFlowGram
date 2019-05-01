package se.iimetra.dataflow

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class WsRequest(val eventType: String, val content: String)

@Serializable
data class WsResponse(val eventType: String, val content: String)

@Serializable
data class ConverterEventRequest(
  val systemFunctionId: String,
  val executionPanelId: Long,
  val blockIndex: Int,
  val arg: String
)

@Serializable
data class ConverterEventResponse(
  val systemFunctionId: String,
  val executionPanelId: Long,
  val blockIndex: Int,
  val result: String?,
  val error: String?
)

@Serializable
data class ServerEventRequest(
  val functionId: FunctionId,
  val arguments: List<String>,
  val paramValues: MutableMap<String, String>,
  val version: Long,
  val executionPanelId: Long,
  val blockIndex: Int
)

@Serializable
data class ServerResultEventResponse(
  val executionPanelId: Long,
  val blockIndex: Int,
  val msg: String?,
  val ref: String?,
  val error: String?
)

