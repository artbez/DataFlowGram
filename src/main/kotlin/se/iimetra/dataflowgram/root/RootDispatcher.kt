package se.iimetra.dataflowgram.root

import se.iimetra.dataflowgram.git.FunctionId
import se.iimetra.dataflowgram.git.GitContent
import se.iimetra.dataflowgram.git.GitListener
import se.iimetra.dataflowgram.lib.LibHolder
import se.iimetra.dataflowgram.worker.Worker
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

  suspend fun execute(function: FunctionId, args: List<String>, version: Long): CompletableFuture<String?> {
    if (!cache.check(function, version)) {
      return CompletableFuture.supplyAsync { throw Exception() }
    }
    return worker.execute(function, args)
  }
}