package se.iimetra.dataflowgram.worker

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import se.iimetra.dataflowgram.root.FunctionMeta
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.CompletableFuture


class Worker {
  private val actionQueue = Channel<WorkerAction>()
  private lateinit var client: PythonServerClient
  private val logger = LoggerFactory.getLogger(Worker::class.java)

  init {
    GlobalScope.launch {
      try {
        val process = ProcessBuilder("python3", "python/server.py").start()
        BufferedReader(InputStreamReader(process.inputStream)).lines().forEach {
          println(it)
        }
      } finally {
        client.shutdown()
      }
    }
  }

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
          println("Execute $action")
          client.executeCommand(action.function, action.arguments)
          action.resultRef.complete("answer: 123")
        }
      }
    }
  }

  suspend fun update() {
    actionQueue.send(WorkerAction.Update)
  }

  suspend fun execute(function: FunctionMeta, args: List<String>): CompletableFuture<String?> {
    val future = CompletableFuture<String?>()
    actionQueue.send(WorkerAction.Execute(function, args, future))
    return future
  }
}