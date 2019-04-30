package se.iimetra.dataflowgram.worker

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import se.iimetra.dataflow.FunctionId
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflowgram.git.GitConnector
import se.iimetra.dataflowgram.root.ValueTypePair
import se.iimetra.dataflowgram.worker.handlers.ConvertersHandler
import se.iimetra.dataflowgram.worker.handlers.ExecutionHandler
import se.iimetra.dataflowgram.worker.handlers.FileActionsHandler
import se.iimetra.dataflowgram.worker.handlers.UpdateHandler
import java.nio.file.Path
import java.util.concurrent.CompletableFuture


class Worker(private val gitConnector: GitConnector) {
  private val actionQueue = Channel<WorkerAction>()
  private val logger = LoggerFactory.getLogger(Worker::class.java)

  private lateinit var client: PythonServerClient
  private lateinit var updateHandler: UpdateHandler
  private lateinit var executionHandler: ExecutionHandler
  private lateinit var convertersHandler: ConvertersHandler
  private lateinit var fileActionsHandler: FileActionsHandler

  @Volatile
  private var blockQueueMsg: String? = null

//  init {
//    GlobalScope.launch {
//      try {
//        val process = ProcessBuilder("python3", "python/server.py").start()
//        BufferedReader(InputStreamReader(process.inputStream)).lines().forEach {
//          println(it)
//        }
//      } finally {
//        client.shutdown()
//      }
//    }
//  }

  fun start() = GlobalScope.launch {
    delay(3000)
    client = PythonServerClient("localhost", 50051)
    initHandlers()

    while (true) {
      val action = actionQueue.poll()
      if (blockQueueMsg != null && action !is WorkerAction.Update) {
        if (action is CompletableAction<*>) {
          action.result.completeExceptionally(RuntimeException(blockQueueMsg))
        }
        continue
      }

      when (action) {
        is WorkerAction.Update -> {
          try {
            updateHandler.processUpdate(action)
            val msg = blockQueueMsg
            if (msg != null) {
              logger.info("Unblocking Queue Processing")
              blockQueueMsg = null
            }
          } catch (ex: Exception) {
            blockQueueMsg = ex.message
            logger.error("Blocking Queue Processing due to ", ex)
          }
        }
        is WorkerAction.Execute -> {
          if (!updateHandler.checkOutdated(action.function, action.version)) {
            logger.warn("Incorrect version")
            action.result.completeExceptionally(RuntimeException("Wrong version"))
          } else {
            val description = updateHandler.getFullFunction(action.function, action.version)
              ?: throw IllegalStateException("Inconsistent state")

            executionHandler.processExecution(action, description)
          }
        }
        is WorkerAction.InData, is WorkerAction.OutData -> {
          convertersHandler.processConversion(action)
        }
        is WorkerAction.CreateFile, is WorkerAction.InitFile -> {
          fileActionsHandler.processFileAction(action)
        }
      }
    }
  }

  suspend fun update(reqVersion: Long): CompletableFuture<GitContent?> {
    val future = CompletableFuture<GitContent?>()
    actionQueue.send(WorkerAction.Update(reqVersion, future))
    return future
  }

  suspend fun execute(function: FunctionId, args: List<String>, params: Map<String, String>, version: Long, onMessageReceive: (String) -> Unit): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    actionQueue.send(WorkerAction.Execute(function, args, params, version, onMessageReceive, future))
    return future
  }

  suspend fun uploadFile(path: Path): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    actionQueue.send(WorkerAction.CreateFile(path, path.toFile().name, future))
    return future
  }

  suspend fun initFile(name: String): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    actionQueue.send(WorkerAction.InitFile(name, future))
    return future
  }

  suspend fun outData(ref: String, type: String): CompletableFuture<ValueTypePair> {
    val future = CompletableFuture<ValueTypePair>()
    actionQueue.send(WorkerAction.OutData(ref, type, future))
    return future
  }

  suspend fun inData(value: String, type: String): CompletableFuture<ValueTypePair> {
    val future = CompletableFuture<ValueTypePair>()
    actionQueue.send(WorkerAction.InData(ValueTypePair(value, type), future))
    return future
  }

  private fun initHandlers() {
    updateHandler = UpdateHandler(gitConnector, client)
    executionHandler = ExecutionHandler(client)
    convertersHandler = ConvertersHandler(client)
    fileActionsHandler = FileActionsHandler(client)
  }
}
