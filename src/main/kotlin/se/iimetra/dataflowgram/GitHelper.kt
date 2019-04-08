package se.iimetra.dataflowgram

import org.eclipse.jgit.api.Git
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

class GitHelper(private val remoteRepo: String) {
  private val localRepoDirectory = Paths.get("repo")

  init {
    removeDirectory(localRepoDirectory)
    Git.cloneRepository()
      .setURI(remoteRepo)
      .setDirectory(localRepoDirectory.toFile())
      .call()
  }

  private fun removeDirectory(directory: Path) {
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