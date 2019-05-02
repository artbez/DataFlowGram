package se.iimetra.dataflowgram.worker

import se.iimetra.dataflow.*
import se.iimetra.dataflowgram.root.ValueTypePair
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

interface CompletableAction<T> {
  val result: CompletableFuture<T>
}

sealed class WorkerAction {
  data class Update(
    val reqVersion: Long,
    override val result: CompletableFuture<GitContent?>
  ): CompletableAction<GitContent?>, WorkerAction()

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
    val version: Long = 0,
    val onMessageReceive: (String) -> Unit = {},
    override val result: CompletableFuture<String>
  ): CompletableAction<String>, WorkerAction()

//  data class AllFiles(
//    override val result: CompletableFuture<UserFilesInfo?>
//  ): CompletableAction<UserFilesInfo?>, WorkerAction()
}