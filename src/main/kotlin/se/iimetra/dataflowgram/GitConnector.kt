package se.iimetra.dataflowgram

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.eclipse.jgit.api.Git
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.regex.Pattern
import kotlin.streams.toList

data class CategoryFileSpace(val name: String, val files: List<FileSpace>)
data class FileSpace(val name: String, val lines: List<String>)

interface GitListener {
  suspend fun parseUpdate(categories: List<CategoryFileSpace>)
}

class GitConnector(remoteRepo: String) {
  private val localRepoDirectory = Paths.get("repo")
  private val listeners = mutableListOf<GitListener>()
  private val git: Git

  init {
    removeDirectory(localRepoDirectory)
    git = Git.cloneRepository()
      .setURI(remoteRepo)
      .setDirectory(localRepoDirectory.toFile())
      .call()
  }

  fun start() = GlobalScope.launch {
    while (true) {
      delay(3000)
      val updates = git.pull().call().fetchResult.trackingRefUpdates.isNotEmpty()
      if (updates) {
        listeners.forEach { it.parseUpdate(lastVersion()) }
      }
    }
  }

  fun addListener(listener: GitListener) {
    listeners.add(listener)
  }

  private fun lastVersion(): List<CategoryFileSpace> {
    return Files.list(localRepoDirectory)
      .filter { Files.isDirectory(it) && !it.fileName.toString().startsWith(".") }
      .map { directory -> processCategory(directory) }
      .toList()
  }

  private fun processCategory(path: Path): CategoryFileSpace {
    val data = Files.list(path)
      .filter { Files.isRegularFile(it) }
      .map { leaf -> FileSpace(leaf.shortFileName(), Files.readAllLines(leaf)) }
      .toList()
    return CategoryFileSpace(path.fileName.toString(), data)
  }

  private fun removeDirectory(directory: Path) {
    if (directory.toFile().exists()) {
      Files.walkFileTree(directory, object : SimpleFileVisitor<Path>() {
        override fun visitFile(file: Path?, attributes: BasicFileAttributes?): FileVisitResult {
          Files.delete(file)
          return FileVisitResult.CONTINUE
        }

        override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult {
          Files.delete(dir)
          return FileVisitResult.CONTINUE
        }
      })
    }
  }

  private fun Path.shortFileName(): String {
    val matcher = Pattern.compile("(.*?)\\.").matcher(fileName.toString())
    matcher.find()
    return matcher.group(1)
  }
}