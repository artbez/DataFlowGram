package se.iimetra.dataflow

import kotlinx.serialization.Serializable

@Serializable
data class FunctionSignature(val input: List<String>, val output: String)

fun FunctionSignature.toMathText() = "(${input.joinToString(",") { it.trim() }}) -> $output"

@Serializable
data class FunctionMeta(val signature: FunctionSignature, val params: String?, val description: String?, val version: Long)

@Serializable
data class FunctionId(val category: String, val file: String, val name: String)

@Serializable
data class FunctionTextView(val id: FunctionId, val args: String, val content: List<String>, val imports: List<String>)

@Serializable
data class FunctionDescription(
  val meta: FunctionMeta,
  val view: FunctionTextView
)

fun FunctionDescription.fullId() = "${view.id.category}__${view.id.file}__${view.id.name}"

@Serializable
data class GitContent(val version: Long, val functions: List<FunctionDescription>)

@Serializable
data class CategoryContent(val name: String, val files: List<FileContent>)

@Serializable
data class FileContent(val name: String, val functions: List<CustomFunction>)

@Serializable
data class CustomFunction(
  val signature: FunctionSignature,
  val params: String?,
  val description: String?,
  val name: String,
  val args: String,
  val lines: List<String>,
  val imports: List<String>
)