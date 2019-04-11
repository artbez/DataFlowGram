package se.iimetra.dataflowgram.root

import se.iimetra.dataflowgram.git.FunctionDescription
import se.iimetra.dataflowgram.git.GitContent

data class FunctionMeta(
  val category: String, val file: String,
  val name: String, val argsNumber: Int
)

data class CacheValue(val version: Long, val description: FunctionDescription)

class FunctionsCache {
  private val innerVersionCache = HashMap<FunctionMeta, CacheValue>()

  @Synchronized
  fun update(content: GitContent) = content.functions.mapNotNull { description ->
    val previous = innerVersionCache[description.toMeta()]
    if (previous != null && previous.description == description) {
      return@mapNotNull null
    }
    innerVersionCache[description.toMeta()] = CacheValue(content.version, description)
    return@mapNotNull description
  }

  @Synchronized
  fun check(function: FunctionMeta, version: Long): Boolean {
    val currentVersion = innerVersionCache[function]?.version ?: return false
    return currentVersion == version
  }
}

fun FunctionDescription.toMeta() = FunctionMeta(category, file, name, argsNumber)
