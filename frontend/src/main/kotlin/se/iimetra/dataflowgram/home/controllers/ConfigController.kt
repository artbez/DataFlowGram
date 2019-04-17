package se.iimetra.dataflowgram.home.controllers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflow.SpaceContent
import se.iimetra.dataflowgram.utils.get

interface ConfigUpdateListener {
  fun onConfigUpdated(newConfig: GitContent)
}

class ConfigController {

  var gitContent = GitContent(-1, SpaceContent(emptyList()), SpaceContent(emptyList()))
  private val listeners = mutableListOf<(GitContent) -> Unit>()

  fun push(git: GitContent) {
    gitContent = git
    listeners.forEach { it.invoke(git) }
  }

  fun addListener(listener: (GitContent) -> Unit) {
    listeners.add(listener)
    listener(gitContent)
  }
}