package se.iimetra.dataflowgram

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.nio.file.*

class LibHolder : GitListener {
  private val parser = PythonFileParser()
  private val libLock = Mutex()
  private val libFile = Paths.get("lib/all.py")

  fun getLib() = libFile

  override suspend fun parseUpdate(categories: List<CategoryFileSpace>) {
    libLock.withLock {
      cleanLib()
      createNewLib(categories)
    }
  }

  private fun createNewLib(categories: List<CategoryFileSpace>) {
    val allFunctions = categories.flatMap { processCategory(it) }
    val imports = allFunctions.flatMap { it.imports }.toSet()

    Files.write(libFile, imports.filterNot { it.isBlank() }.joinToString("\n").toByteArray())

    val wholeBody = allFunctions.map { func ->
      val firstLine = "def ${func.name}(${func.args}):"
      listOf(firstLine) + func.lines
    }.filterNot { it.isNullOrEmpty() }.joinToString("\n\n") { it.joinToString("\n") }

    Files.write(libFile, "\n$wholeBody".toByteArray(), StandardOpenOption.APPEND)
  }

  private fun cleanLib() {
    val dir = Paths.get("lib")
    if (!dir.toFile().exists()) {
      Files.createDirectory(dir)
    }
    if (libFile.toFile().exists()) {
      libFile.toFile().delete()
    }
    Files.createFile(libFile)
  }

  private fun processCategory(space: CategoryFileSpace) =
    space.files.flatMap { processFile(it).map { it.copy(name = "${space.name}__${it.name}") } }


  private fun processFile(space: FileSpace) =
    parser.parse(space.lines).map { it.copy(name = "${space.name}__${it.name}") }
}