package se.iimetra.dataflowgram.home.controllers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflowgram.utils.get

interface ConfigUpdateListener {
  fun onConfigUpdated(newConfig: GitContent)
}

class ConfigController {

  var gitContent = GitContent(-1, emptyList())
  private val listeners = mutableListOf<(GitContent) -> Unit>()

  init {
    GlobalScope.launch {
      val config = get("/api/config/all")
      gitContent = Json.parse(GitContent.serializer(), config)
      listeners.forEach { it.invoke(gitContent) }
    }
  }

  fun addListener(listener: (GitContent) -> Unit) {
    listeners.add(listener)
    listener(gitContent)
  }
}