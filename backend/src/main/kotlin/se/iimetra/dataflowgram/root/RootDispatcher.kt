package se.iimetra.dataflowgram.root

import se.iimetra.dataflowgram.git.FunctionId
import se.iimetra.dataflowgram.git.GitContent
import se.iimetra.dataflowgram.git.GitListener
import se.iimetra.dataflowgram.lib.LibHolder
import se.iimetra.dataflowgram.worker.Worker
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class RootDispatcher : GitListener {

  private val libHolder = LibHolder()
  private val cache = FunctionsCache()
  private val worker = Worker()

  fun start() {
    worker.start()
  }

  override suspend fun parseUpdate(newContent: GitContent) {
    val updated = cache.update(newContent)
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
    params: Map<String, String> = emptyMap()
  ): CompletableFuture<String> {
    if (!cache.check(function, version)) {
      return CompletableFuture.supplyAsync { throw Exception() }
    }
    return worker.execute(function, args, params)
  }
}