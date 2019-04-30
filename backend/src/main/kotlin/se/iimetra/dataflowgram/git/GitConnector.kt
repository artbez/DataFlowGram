package se.iimetra.dataflowgram.git

import org.eclipse.jgit.api.Git
import se.iimetra.dataflow.*
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.atomic.AtomicLong
import java.util.regex.Pattern
import kotlin.streams.toList

class GitConnector(remoteRepo: String) {
  val localRepoDirectory = Paths.get("repo")

  private val git: Git
  private val pureParser = PythonFileParser("pure")
  private val renderParser = PythonFileParser("render")
  private val version = AtomicLong(0)

  init {
    removeDirectory(localRepoDirectory)
    git = Git.cloneRepository()
      .setURI(remoteRepo)
      .setDirectory(localRepoDirectory.toFile())
      .call()

    version.incrementAndGet()
  }

  fun update(reqVersion: Long): GitContent? {
    val updates = git.pull().call().fetchResult.trackingRefUpdates.isNotEmpty()
    if (updates) {
      version.incrementAndGet()
    }
    if (reqVersion < version.get()) {
      return GitContent(version.get(), lastVersion(true), lastVersion(false))
    }
    return null
  }

  private fun lastVersion(isPython: Boolean): SpaceContent {
    val dir = if (isPython) Paths.get("repo/pure") else Paths.get("repo/render")

    val categories = Files.list(dir)
      .filter { Files.isDirectory(it) && !it.fileName.toString().startsWith(".") && !it.fileName.toString().contains("node modules") }
      .map { directory -> processCategory(directory, isPython) }
      .toList()

    return SpaceContent(categories.flatMap { category ->
      category.files.flatMap { file ->
        file.functions.map {
          FunctionDescription(
            it.meta,
            FunctionTextView(FunctionId(it.meta.language, category.name, file.name, it.name), it.args, it.lines)
          )
        }
      }
    })
  }

  private fun processCategory(path: Path, isPure: Boolean): CategoryContent {
    val data = Files.list(path).toList()
      .filter { Files.isRegularFile(it) }
      .mapNotNull { leaf -> processFile(leaf, isPure)?.let { FileContent(leaf.shortFileName(), it) } }
      .toList()
    return CategoryContent(path.fileName.toString(), data)
  }

  private fun processFile(path: Path, isPure: Boolean): List<CustomFunction>? {
    val lines = Files.readAllLines(path)
    return when {
      lines.size == 0 -> null
      isPure && !pureParser.check(lines[0]) -> null
      !isPure && !renderParser.check(lines[0]) -> null
      isPure -> pureParser.parse(version.get(), lines)
      else -> renderParser.parse(version.get(), lines)
    }
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