package se.iimetra.dataflowgram.lib

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import se.iimetra.dataflowgram.git.FunctionDescription
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class LibHolder {
  private val libLock = Mutex()
  private val libFile = Paths.get("lib/all.py")

  fun getLib() = libFile

  suspend fun updateLib(functions: List<FunctionDescription>) {
    libLock.withLock {
      createIfNotExist()
      appendToLib(functions)
    }
  }

  private fun createIfNotExist() {
    val dir = Paths.get("lib")
    if (!dir.toFile().exists()) {
      Files.createDirectory(dir)
    }
    if (!libFile.toFile().exists()) {
      Files.createFile(libFile)
    }
  }

  private fun appendToLib(functions: List<FunctionDescription>) {
    val imports = functions.flatMap { it.imports }.toSet()

    Files.write(libFile, imports.filterNot { it.isBlank() }.joinToString("\n").toByteArray(),  StandardOpenOption.APPEND)

    val wholeBody = functions.map { func ->
      val firstLine = "def ${func.fullName()}(${func.args()}):"
      listOf(firstLine) + func.content
    }.filterNot { it.isNullOrEmpty() }.joinToString("\n\n") { it.joinToString("\n") }

    Files.write(libFile, "\n$wholeBody\n".toByteArray(), StandardOpenOption.APPEND)
  }

  private fun FunctionDescription.fullName() = "${category}__${file}__$name"
  private fun FunctionDescription.args() = (0..argsNumber).joinToString(",") { "i$it" }
}