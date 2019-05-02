package se.iimetra.dataflowgram.root

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import se.iimetra.dataflow.FunctionId
import se.iimetra.dataflowgram.controller.ws.ConfigWsHandler
import se.iimetra.dataflowgram.controller.ws.FilesSystemHandler
import se.iimetra.dataflowgram.files.FileSystemConnector
import se.iimetra.dataflowgram.git.GitConnector
import se.iimetra.dataflowgram.worker.Worker
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class RootDispatcher(
  gitConnector: GitConnector,
  fileSystemConnector: FileSystemConnector,
  private val configWsHandler: ConfigWsHandler,
  private val fileSystemHandler: FilesSystemHandler
) {

  private val worker: Worker = Worker(gitConnector, fileSystemConnector)
  private val logger = LoggerFactory.getLogger(RootDispatcher::class.java)

  init {
    worker.start()
    var version = 0L
    GlobalScope.launch {
      while (true) {
        try {
          val update = worker.update(version).await()
          update?.let {
            version = it.version
            logger.info("Sending update to client")
            configWsHandler.parseUpdate(it)
          }
        } catch (ex: Exception) {
          logger.error("Exception while updating ", ex)
        }
        delay(3000)
      }
    }
    GlobalScope.launch {
      while (true) {
        try {
          val files = fileSystemConnector.getAllFiles()
          if (files != null) {
            fileSystemHandler.parseUpdate(files)
          }
        } catch (ex: Exception) {
          logger.error("Exception while updating ", ex)
        }
        delay(3000)
      }
    }
  }

  suspend fun initFile(name: String): CompletableFuture<String> {
    return worker.initFile(name)
  }

  suspend fun uploadFile(path: Path): CompletableFuture<String> {
    return worker.uploadFile(path)
  }

  suspend fun outData(ref: String, type: String): CompletableFuture<ValueTypePair> {
    return worker.outData(ref, type)
  }

  suspend fun inData(value: String, type: String): CompletableFuture<ValueTypePair> {
    return worker.inData(value, type)
  }

  suspend fun execute(
    function: FunctionId,
    args: List<String>,
    version: Long,
    params: Map<String, String> = emptyMap(),
    onMessageReceive: (String) -> Unit
  ): CompletableFuture<String> {
    return worker.execute(function, args, params, version, onMessageReceive)
  }
}

data class ValueTypePair(val value: String, val type: String)