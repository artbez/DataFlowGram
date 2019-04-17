package se.iimetra.dataflowgram.root

import se.iimetra.dataflow.FunctionId
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflowgram.git.GitListener
import se.iimetra.dataflowgram.lib.LibHolder
import se.iimetra.dataflowgram.worker.Worker
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class RootDispatcher : GitListener {

  private val libHolder = LibHolder()
  private val cache = FunctionsCache()
  private val worker = Worker()

  init {
    worker.start()
  }

  override suspend fun parseUpdate(newContent: GitContent) {
    val updated = cache.update(newContent.version, newContent.serverSpace.functions)
    libHolder.updateLib(updated)
    worker.update()
  }

  suspend fun initFile(name: String): CompletableFuture<String> {
    return worker.initFile(name)
  }

  suspend fun uploadFile(path: Path): CompletableFuture<String> {
    return worker.uploadFile(path)
  }

  suspend fun loadJson(ref: String): CompletableFuture<String> {
    return worker.getJson(ref)
  }

  suspend fun execute(
    function: FunctionId,
    args: List<String>,
    version: Long,
    params: Map<String, String> = emptyMap(),
    onMessageReceive: (String) -> Unit
  ): CompletableFuture<String> {
    if (!cache.check(function, version)) {
      return CompletableFuture.supplyAsync { throw Exception() }
    }
    return worker.execute(function, args, params, onMessageReceive)
  }
}