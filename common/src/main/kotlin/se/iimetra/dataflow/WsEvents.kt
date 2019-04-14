package se.iimetra.dataflow

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class WsRequest(val eventType: String, val content: String)

@Serializable
data class WsResponse(val eventType: String, val content: String)
