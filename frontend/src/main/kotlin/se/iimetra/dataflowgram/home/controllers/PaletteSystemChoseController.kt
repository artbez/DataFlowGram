package se.iimetra.dataflowgram.home.controllers

import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.SystemFunction
import se.iimetra.dataflow.fullId
import se.iimetra.dataflowgram.home.configController


class PaletteSystemChoseController {
  var choosen: SystemFunction? = null
  private val listeners = mutableListOf<(SystemFunction) -> Unit>()

  fun addListener(listener: (SystemFunction) -> Unit) {
    listeners.add(listener)
    choosen?.let { listener.invoke(it) }
  }

  fun newChoose(newId: String) {
    choosen = configController.gitContent.connectors.firstOrNull { it.id == newId }
    choosen?.let { ch -> listeners.forEach { it.invoke(ch) } }
  }
}