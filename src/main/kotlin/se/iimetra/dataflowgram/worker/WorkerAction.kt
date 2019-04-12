package se.iimetra.dataflowgram.worker

import se.iimetra.dataflowgram.git.FunctionId
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

interface CompletableAction<T> {
  val result: CompletableFuture<T>
}

sealed class WorkerAction {
  object Update: WorkerAction()

  data class InitFile(
    val name: String,
    override val result: CompletableFuture<String>
  ): CompletableAction<String>, WorkerAction()

  data class CreateFile(
    val path: Path, val name: String,
    override val result: CompletableFuture<String>
  ): CompletableAction<String>, WorkerAction()

  data class GetJson(
    val ref: String,
    override val result: CompletableFuture<String>
  ): CompletableAction<String>, WorkerAction()

  data class Execute(
    val function: FunctionId,
    val arguments: List<String> = emptyList(),
    val params: Map<String, String> = emptyMap(),
    override val result: CompletableFuture<String>
  ): CompletableAction<String>, WorkerAction()
}