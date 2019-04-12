package se.iimetra.dataflowgram.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import se.iimetra.dataflowgram.git.GitContent
import se.iimetra.dataflowgram.git.GitListener

@RestController
@RequestMapping("/api/config")
class ConfigController: GitListener {

  @Volatile
  private var currentConfig: GitContent = GitContent(-1, emptyList())

  override suspend fun parseUpdate(newContent: GitContent) {
    currentConfig = newContent
  }

  @GetMapping("all")
  fun getConfig() {

  }
}