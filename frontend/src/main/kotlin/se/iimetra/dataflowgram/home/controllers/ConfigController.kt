package se.iimetra.dataflowgram.home.controllers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import se.iimetra.dataflow.AllFunctions
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflow.SpaceContent
import se.iimetra.dataflowgram.utils.get

interface ConfigUpdateListener {
  fun onConfigUpdated(newConfig: GitContent)
}

class ConfigController {

  var gitContent = AllFunctions(GitContent(-1, SpaceContent(emptyList()), SpaceContent(emptyList())), emptyList())
  private val listeners = mutableListOf<(AllFunctions) -> Unit>()

  fun push(all: AllFunctions) {
    gitContent = all
    listeners.forEach { it.invoke(all) }
  }

  fun addListener(listener: (AllFunctions) -> Unit) {
    listeners.add(listener)
    listener(gitContent)
  }
}