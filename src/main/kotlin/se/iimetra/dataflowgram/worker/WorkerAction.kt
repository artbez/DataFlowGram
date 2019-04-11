package se.iimetra.dataflowgram.worker

import se.iimetra.dataflowgram.git.FunctionId
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

sealed class WorkerAction {
  object Update: WorkerAction()
  data class CreateFile(val path: Path, val name: String): WorkerAction()
  data class Execute (
    val function: FunctionId,
    val arguments: List<String> = emptyList(),
    val resultRef: CompletableFuture<String?>
  ): WorkerAction()
}