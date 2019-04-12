package se.iimetra.dataflowgram.home

import se.iimetra.dataflowgram.home.controllers.ConfigController
import se.iimetra.dataflowgram.home.controllers.PaletteChoseController
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.DiagramModel

fun DiagramEngine.setup(): DiagramEngine {

  installDefaultFactories()

  val model = DiagramModel()
  setDiagramModel(model)

  return this
}

val configController = ConfigController()
val paletteChoseController = PaletteChoseController()