package se.iimetra.dataflowgram.worker

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import se.iimetra.dataflow.FunctionId
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture


class Worker {
  private val actionQueue = Channel<WorkerAction>()
  private lateinit var client: PythonServerClient
  private val logger = LoggerFactory.getLogger(Worker::class.java)

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
    while (true) {
      val action = actionQueue.poll()
      when (action) {
        is WorkerAction.Update -> {
          client.update()
        }
        is WorkerAction.Execute -> {
          logger.info("Executing ${action.function.name}")
          val ref = client.executeCommand(action.function, action.arguments, action.params, action.onMessageReceive)
          logger.info("${action.function.name} executed")
          action.result.complete(ref)
        }
        is WorkerAction.CreateFile -> {
          val newPath = writeFile(action.path, action.name)
          val fileRef = client.initFile(newPath)
          action.result.complete(fileRef)
        }
        is WorkerAction.GetJson -> {
          val json = client.getJson(action.ref)
          action.result.complete(json)
        }
        is WorkerAction.InitFile -> {
          val fileRef = client.initFile(Paths.get("lib/${action.name}"))
          action.result.complete(fileRef)
        }
      }
    }
  }

  suspend fun update() {
    actionQueue.send(WorkerAction.Update)
  }

  suspend fun execute(function: FunctionId, args: List<String>, params: Map<String, String>, onMessageReceive: (String) -> Unit): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    actionQueue.send(WorkerAction.Execute(function, args, params, onMessageReceive, future))
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

  suspend fun getJson(ref: String): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    actionQueue.send(WorkerAction.GetJson(ref, future))
    return future
  }

  private fun writeFile(path: Path, name: String): Path {
    val file = Paths.get("lib/$name")
    if (file.toFile().exists()) {
      file.toFile().delete()
    }
    Files.copy(path, file)
    return file.toAbsolutePath()
  }

  private fun PythonServerClient.initFile(path: Path): String =
    executeDefaultCommand("init_file", mapOf("path" to path.toAbsolutePath().toString(), "is_default_function" to "true"))
}
