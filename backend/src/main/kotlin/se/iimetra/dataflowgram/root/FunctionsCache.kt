package se.iimetra.dataflowgram.root

import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.FunctionId


data class CacheValue(val version: Long, val description: FunctionDescription)

class FunctionsCache {
  private val innerVersionCache = HashMap<FunctionId, CacheValue>()

  fun get(version: Long, functions: List<FunctionDescription>) = functions.mapNotNull { description ->
    val previous = innerVersionCache[description.view.id]
    if (previous != null
      && previous.description.view == description.view
      && previous.description.meta.copy(version = version) == description.meta
    ) {
      return@mapNotNull null
    }
    return@mapNotNull description
  }

  fun update(version: Long, functions: List<FunctionDescription>) {
    functions.forEach { description ->
      innerVersionCache[description.view.id] = CacheValue(version, description)
    }
  }

  @Synchronized
  fun check(function: FunctionId, version: Long): Boolean {
    val currentVersion = innerVersionCache[function]?.version ?: return false
    return currentVersion <= version
  }
}
