package se.iimetra.dataflowgram.home.controllers

import se.iimetra.dataflowgram.home.filesSystemController

class PaletteFileChoseController {
  var choosen: String? = null
  private val listeners = mutableListOf<(String?) -> Unit>()

  fun addListener(listener: (String?) -> Unit) {
    listeners.add(listener)
    choosen?.let { listener.invoke(it) }
  }

  fun newChoose(newId: String?) {
    choosen = filesSystemController.files.files.find { it.filename == newId }?.let { it.filename }
    choosen.let { ch -> listeners.forEach { it.invoke(ch) } }
  }
}