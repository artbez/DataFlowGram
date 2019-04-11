package se.iimetra.dataflowgram.git

interface GitListener {
  suspend fun parseUpdate(newContent: GitContent)
}

data class FunctionDescription(
  val category: String, val file: String,
  val name: String, val args: String,
  val content: List<String>, val imports: List<String>
) {
  val argsNumber = args.let { if (it.isBlank()) 0 else it.split(",").size }
}

data class GitContent(val version: Long, val functions: List<FunctionDescription>)

data class CategoryContent(val name: String, val files: List<FileContent>)

data class FileContent(val name: String, val functions: List<CustomFunction>)

data class CustomFunction(val name: String, val args: String, val lines: List<String>, val imports: List<String>)

