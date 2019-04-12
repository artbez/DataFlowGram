package se.iimetra.dataflowgram.git

interface GitListener {
  suspend fun parseUpdate(newContent: GitContent)
}

data class FunctionSignature(val input: List<String>, val output: String)

data class FunctionMeta(val signature: FunctionSignature, val params: String?, val description: String?)

data class FunctionId(val category: String, val file: String, val name: String)

data class FunctionTextView(val id: FunctionId, val args: String, val content: List<String>, val imports: List<String>)

data class FunctionDescription(
  val meta: FunctionMeta,
  val view: FunctionTextView
)

data class GitContent(val version: Long, val functions: List<FunctionDescription>)

data class CategoryContent(val name: String, val files: List<FileContent>)

data class FileContent(val name: String, val functions: List<CustomFunction>)

data class CustomFunction(
  val signature: FunctionSignature,
  val params: String?,
  val description: String?,
  val name: String,
  val args: String,
  val lines: List<String>,
  val imports: List<String>
)

