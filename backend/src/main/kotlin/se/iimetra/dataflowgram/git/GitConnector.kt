package se.iimetra.dataflowgram.git

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.eclipse.jgit.api.Git
import se.iimetra.dataflow.*
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.atomic.AtomicLong
import java.util.regex.Pattern
import kotlin.streams.toList

class GitConnector(remoteRepo: String) {
  private val localRepoDirectory = Paths.get("repo")
  private val listeners = mutableListOf<GitListener>()
  private val git: Git
  private val pyParser = PythonFileParser()
  private val jsParser = JsFileParser()
  private val version = AtomicLong(0)

  init {
    removeDirectory(localRepoDirectory)
    git = Git.cloneRepository()
      .setURI(remoteRepo)
      .setDirectory(localRepoDirectory.toFile())
      .call()
  }

  fun start() = GlobalScope.launch {
    listeners.forEach { it.parseUpdate(GitContent(version.get(), lastVersion(true), lastVersion(false))) }

    while (true) {
      delay(3000)
      val updates = git.pull().call().fetchResult.trackingRefUpdates.isNotEmpty()
      if (updates) {
        version.incrementAndGet()
        listeners.forEach { it.parseUpdate(GitContent(version.get(), lastVersion(true), lastVersion(false))) }
      }
    }
  }

  fun addListener(listener: GitListener) {
    listeners.add(listener)
  }

  private fun lastVersion(isPython: Boolean): SpaceContent {
    val dir = if (isPython) Paths.get("repo/python") else Paths.get("repo/js")

    val categories = Files.list(dir)
      .filter { Files.isDirectory(it) && !it.fileName.toString().startsWith(".") }
      .map { directory -> processCategory(directory, isPython) }
      .toList()

    return SpaceContent(categories.flatMap { category ->
      category.files.flatMap { file ->
        file.functions.map {
          FunctionDescription(
            FunctionMeta(it.signature, it.params, it.description, version.get(), if (isPython) "python" else "js"),
            FunctionTextView(FunctionId(category.name, file.name, it.name), it.args, it.lines, it.imports)
          )
        }
      }
    })
  }

  private fun processCategory(path: Path, isPython: Boolean): CategoryContent {
    val data = Files.list(path)
      .filter { Files.isRegularFile(it) }
      .map { leaf -> FileContent(leaf.shortFileName(), processFile(leaf, isPython)) }
      .toList()
    return CategoryContent(path.fileName.toString(), data)
  }

  private fun processFile(path: Path, isPython: Boolean): List<CustomFunction> {
    val lines = Files.readAllLines(path)
    return if (isPython) pyParser.parse(lines) else jsParser.parse(lines)
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