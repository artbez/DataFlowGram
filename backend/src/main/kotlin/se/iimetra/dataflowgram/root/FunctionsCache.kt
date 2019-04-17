package se.iimetra.dataflowgram.root

import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.FunctionId


data class CacheValue(val version: Long, val description: FunctionDescription)

class FunctionsCache {
  private val innerVersionCache = HashMap<FunctionId, CacheValue>()

  @Synchronized
  fun update(version: Long, functions: List<FunctionDescription>) = functions.mapNotNull { description ->
    val previous = innerVersionCache[description.view.id]
    if (previous != null && previous.description == description) {
      return@mapNotNull null
    }
    innerVersionCache[description.view.id] = CacheValue(version, description)
    return@mapNotNull description
  }

  @Synchronized
  fun check(function: FunctionId, version: Long): Boolean {
    val currentVersion = innerVersionCache[function]?.version ?: return false
    return currentVersion == version
  }
}
