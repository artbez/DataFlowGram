package se.iimetra.dataflowgram.worker

import se.iimetra.dataflow.FunctionId
import se.iimetra.dataflowgram.root.ValueTypePair
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

  data class OutData(
    val ref: String,
    val type: String,
    override val result: CompletableFuture<ValueTypePair>
  ): CompletableAction<ValueTypePair>, WorkerAction()

  data class InData(
    val data: ValueTypePair,
    override val result: CompletableFuture<ValueTypePair>
  ): CompletableAction<ValueTypePair>, WorkerAction()

  data class Execute(
    val function: FunctionId,
    val arguments: List<String> = emptyList(),
    val params: Map<String, String> = emptyMap(),
    val onMessageReceive: (String) -> Unit = {},
    override val result: CompletableFuture<String>
  ): CompletableAction<String>, WorkerAction()
}