package se.iimetra.dataflowgram.worker.handlers

import org.slf4j.LoggerFactory
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.FunctionId
import se.iimetra.dataflowgram.git.GitConnector
import se.iimetra.dataflowgram.root.FunctionsCache
import se.iimetra.dataflowgram.worker.PythonServerClient
import se.iimetra.dataflowgram.worker.WorkerAction

class UpdateHandler(private val gitConnector: GitConnector, private val client: PythonServerClient) {

  private val cache = FunctionsCache()
  private val logger = LoggerFactory.getLogger(UpdateHandler::class.java)
  private val updateList = ArrayList<UpdateLocation>()

  fun checkOutdated(functionId: FunctionId, version: Long) = cache.check(functionId, version)

  @Synchronized
  fun processUpdate(action: WorkerAction.Update) {
    val gitContent = gitConnector.update(action.reqVersion)

    if (gitContent == null) {
      action.result.complete(null)
      return
    }

    val updated = cache.get(gitContent.version, gitContent.serverSpace.functions)
    val files = toLocations(updated)
    val repo = gitConnector.localRepoDirectory.toAbsolutePath().toString()

    try {
      client.update(repo, files)
      cache.update(gitContent.version, updated)
      action.result.complete(gitContent)
    } catch (ex: Exception) {
      action.result.completeExceptionally(ex)
      logger.error("Error on sending update to python worker", ex)
      throw RuntimeException("Can't update git files ${files.joinToString(";")}")
    }
  }

  private fun toLocations(funcs: List<FunctionDescription>): List<UpdateLocation> {
    val files = funcs
      .groupBy { it.view.id.category }
      .flatMap { group -> group.value.map { UpdateLocation(group.key, it.view.id.file) } }
      .toSet()

    return files.toList()
  }
}

data class UpdateLocation(val category: String, val file: String)