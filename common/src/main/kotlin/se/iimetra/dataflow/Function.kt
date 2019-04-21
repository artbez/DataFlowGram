package se.iimetra.dataflow

import kotlinx.serialization.Serializable

@Serializable
data class FunctionSignature(val input: List<String>, val output: String)

fun FunctionSignature.toMathText() = "(${input.joinToString(",") { it.trim() }}) -> $output"

@Serializable
data class FunctionMeta(val label: String, val signature: FunctionSignature, val paramsMap: Map<String, String>, val description: String?, val version: Long, val language: String)

@Serializable
data class FunctionId(val category: String, val file: String, val name: String)

@Serializable
data class FunctionTextView(val id: FunctionId, val args: String, val content: List<String>)

@Serializable
data class FunctionDescription(
  val meta: FunctionMeta,
  val view: FunctionTextView
)

@Serializable
data class SystemFunction(val functionSignature: FunctionSignature, val name: String, val id: String, val params: Map<String, String> = emptyMap())

fun FunctionDescription.fullId() = "${meta.language}__${view.id.category}__${view.id.file}__${view.id.name}"

@Serializable
data class GitContent(val version: Long, val serverSpace: SpaceContent, val clientSpace: SpaceContent)

@Serializable
data class AllFunctions(val git: GitContent, val connectors: List<SystemFunction>)

@Serializable
data class SpaceContent(val functions: List<FunctionDescription>)

@Serializable
data class CategoryContent(val name: String, val files: List<FileContent>)

@Serializable
data class FileContent(val name: String, val functions: List<CustomFunction>)

@Serializable
data class CustomFunction(
  val meta: FunctionMeta,
  val name: String,
  val args: String,
  val lines: List<String>
)