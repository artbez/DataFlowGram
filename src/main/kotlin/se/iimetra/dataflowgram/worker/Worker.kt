package se.iimetra.dataflowgram.worker

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import se.iimetra.dataflowgram.root.FunctionMeta
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.CompletableFuture


class Worker {
  private val actionQueue = Channel<WorkerAction>()
  private lateinit var client: HelloWorldClient

  init {
    GlobalScope.launch {
      try {
        val process = ProcessBuilder("python", "python/server.py").start()
        BufferedReader(InputStreamReader(process.inputStream)).use {
          val tt = it.lines()
          tt.forEach {
            println(it)
          }
        }
      } finally {
        client.shutdown()
      }
    }
  }

  fun start() = GlobalScope.launch {
    delay(3000)
    client = HelloWorldClient("localhost", 50051)
    while (true) {
      val action = actionQueue.poll()
      when (action) {
        is WorkerAction.Update -> {
          println("Update!!!")
        }
        is WorkerAction.Execute -> {
          println("Execute $action")
          val response = client.greet(action.function.toString())
          action.resultRef.complete("answer: $response")
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