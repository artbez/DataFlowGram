package se.iimetra.dataflowgram.home.diagram.node.ports

import se.iimetra.dataflowgram.wrappers.react.diagrams.AbstractPortFactory

class InitPortModelFactory(type: String) : AbstractPortFactory<InitialPortModel>(type) {

  companion object {
    val instance = InitPortModelFactory("default")
  }

  override fun getNewInstance(): InitialPortModel {
    return InitialPortModel()
  }
}