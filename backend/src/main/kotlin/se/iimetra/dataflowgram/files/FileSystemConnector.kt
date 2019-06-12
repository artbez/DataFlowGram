package se.iimetra.dataflowgram.files

import se.iimetra.dataflow.FileInfo
import se.iimetra.dataflow.UserFilesInfo
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

class FileSystemConnector {
  val userDir = Paths.get("userDir")
  private var lastInfo: UserFilesInfo? = null

  init {
    if (!userDir.toFile().exists()) {
      Files.createDirectory(userDir)
    }
  }

  fun getAllFiles(): UserFilesInfo? {
    val files = Files.list(userDir).use { it.toList().filter { Files.isRegularFile(it) } }
    val newInfo = UserFilesInfo(files.map { FileInfo(it.fileName.toString(), false) })
    lastInfo = newInfo
    return newInfo
  }

  fun deleteFile(path: Path) {
    Files.delete(path)
  }
}