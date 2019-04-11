package se.iimetra.dataflowgram.root

import se.iimetra.dataflowgram.git.FunctionDescription
import se.iimetra.dataflowgram.git.GitContent

data class FunctionMeta(
  val category: String, val file: String,
  val name: String, val argsNumber: Int
)

class FunctionsCache {
  private val innerVersionCache = HashMap<FunctionMeta, Long>()

  @Synchronized
  fun update(content: GitContent) = content.functions.mapNotNull { description ->
    val previous = innerVersionCache.put(description.toMeta(), content.version)
    return@mapNotNull if (previous == content.version) null else description
  }

  @Synchronized
  fun check(function: FunctionMeta, version: Long): Boolean {
    val currentVersion = innerVersionCache[function] ?: return false
    return currentVersion == version
  }
}

fun FunctionDescription.toMeta() = FunctionMeta(category, file, name, argsNumber)
