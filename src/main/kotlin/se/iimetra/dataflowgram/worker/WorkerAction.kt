package se.iimetra.dataflowgram.worker

import se.iimetra.dataflowgram.root.FunctionMeta
import java.util.concurrent.CompletableFuture

sealed class WorkerAction {
  object Update: WorkerAction()
  data class Execute (
    val function: FunctionMeta,
    val arguments: List<String> = emptyList(),
    val resultRef: CompletableFuture<String?>
  ): WorkerAction()
}