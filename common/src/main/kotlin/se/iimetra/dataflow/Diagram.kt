package se.iimetra.dataflow

import kotlinx.serialization.Serializable

@Serializable
data class DiagramSaveRequest(
  val name: String,
  val diagram: String
)


@Serializable
data class AllDiagrams(val diagrams: List<String>)

@Serializable
data class DiagramDto(val id: Long, val name: String, val diagram: String)