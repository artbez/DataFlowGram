package se.iimetra.dataflowgram.worker

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import se.iimetra.dataflowgram.git.FunctionDescription
import se.iimetra.dataflowgram.root.FunctionMeta
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

class Worker {
  private val actionQueue = Channel<WorkerAction>(20)

  fun start() = GlobalScope.launch {
    while (true) {
      val action = actionQueue.poll()
      when (action) {
        is WorkerAction.Update -> {
          println("Update!!!")
        }
        is WorkerAction.Execute -> {
          println("Execute $action")
          action.resultRef.complete("123!")
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