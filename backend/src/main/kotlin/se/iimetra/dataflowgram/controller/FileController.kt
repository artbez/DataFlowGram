package se.iimetra.dataflowgram.controller

import kotlinx.html.body
import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import se.iimetra.dataflowgram.files.FileSystemConnector
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


@Controller
@RequestMapping("/api/file")
class FileController(val fileSystemConnector: FileSystemConnector) {

  @RequestMapping(path = ["/download"], method = [RequestMethod.GET])
  @Throws(IOException::class)
  fun download(@RequestParam("fileName") fileName: String): ResponseEntity<Resource> {
    val path = Paths.get(fileSystemConnector.userDir.toAbsolutePath().toString() + "/" + fileName)
    val resource = ByteArrayResource(Files.readAllBytes(path))

    return ResponseEntity.ok()
      //.headers(headers)
      .contentLength(path.toFile().length())
      .contentType(MediaType.parseMediaType("application/octet-stream"))
      .body(resource)
  }

  @RequestMapping(path = ["/delete"], method = [RequestMethod.GET])
  fun delete(@RequestParam("fileName") fileName: String): ResponseEntity<Unit> {
    val path = Paths.get(fileSystemConnector.userDir.toAbsolutePath().toString() + "/" + fileName)
    fileSystemConnector.deleteFile(path)
    return ResponseEntity.ok(Unit)
  }
}