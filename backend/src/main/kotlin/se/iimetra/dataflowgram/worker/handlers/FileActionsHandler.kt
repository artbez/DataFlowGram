package se.iimetra.dataflowgram.worker.handlers

import se.iimetra.dataflowgram.worker.PythonServerClient
import se.iimetra.dataflowgram.worker.WorkerAction
import java.lang.IllegalStateException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FileActionsHandler(private val client: PythonServerClient) {

  fun processFileAction(action: WorkerAction) {
    when (action) {
      is WorkerAction.CreateFile -> {
        val newPath = writeFile(action.path, action.name)
        val fileRef = client.initFile(newPath)
        action.result.complete(fileRef)
      }

      is WorkerAction.InitFile -> {
        val fileRef = client.initFile(Paths.get("lib/${action.name}"))
        action.result.complete(fileRef)
      }

      else -> throw IllegalStateException("Action must be a file action")
    }
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