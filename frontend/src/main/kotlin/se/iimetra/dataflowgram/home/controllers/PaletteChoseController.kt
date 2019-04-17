package se.iimetra.dataflowgram.home.controllers

import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflow.fullId
import se.iimetra.dataflowgram.home.configController

class PaletteChoseController {
  var choosen: FunctionDescription? = null
  private val listeners = mutableListOf<(FunctionDescription) -> Unit>()

  fun addListener(listener: (FunctionDescription) -> Unit) {
    listeners.add(listener)
    choosen?.let { listener.invoke(it) }
  }

  fun newChoose(newId: String) {
    choosen = configController.gitContent.serverSpace.functions.firstOrNull {
      it.fullId() == newId
    }
    choosen = configController.gitContent.clientSpace.functions.firstOrNull {
      it.fullId() == newId
    }
    choosen?.let { ch -> listeners.forEach { it.invoke(ch) } }
  }
}