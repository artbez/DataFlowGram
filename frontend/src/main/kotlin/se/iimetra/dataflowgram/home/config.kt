package se.iimetra.dataflowgram.home

import kotlinx.serialization.ImplicitReflectionSerializer
import se.iimetra.dataflowgram.home.controllers.*
import se.iimetra.dataflowgram.home.diagram.executor.ExecutionService
import se.iimetra.dataflowgram.home.diagram.node.ConverterNodeFactory
import se.iimetra.dataflowgram.home.diagram.node.DefaultNodeFactory
import se.iimetra.dataflowgram.home.diagram.node.ports.InitPortModelFactory
import se.iimetra.dataflowgram.home.diagram.node.ports.InitialPortModel
import se.iimetra.dataflowgram.home.diagram.node.ports.PortType
import se.iimetra.dataflowgram.utils.toMap
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.DiagramModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.LinkModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

fun DiagramEngine.setup(): DiagramEngine {

  installDefaultFactories()

  registerNodeFactory(DefaultNodeFactory.instance)
  registerNodeFactory(ConverterNodeFactory.instance)
  registerPortFactory(InitPortModelFactory.instance)

  val model = DiagramModel()
  setDiagramModel(model)
  return this
}

val paletteSystemChoseController = PaletteSystemChoseController()
val filesSystemController = FilesSystemController()
val serverEventController = ServerEventController()
val configController = ConfigController()
val paletteDefaultChoseController = PaletteDefaultChoseController()
val paletteFileChoseController = PaletteFileChoseController()
val executionService = ExecutionService()
@ImplicitReflectionSerializer
val eventController = EventController()
val converterController = ConverterEventController()

fun NodeModel.neighbors() = inPorts()
  .flatMap { port -> port.getLinks().toMap().values.flatMap {
    kotlin.collections.listOf(
      it.getSourcePort(),
      it.getTargetPort()
    )
  } }
  .map { it as InitialPortModel }
  .map { it.getNode() }
  .filter { it.getID() != this.getID() }

fun NodeModel.neighborsAll() = getPorts().toMap().values.map { it as InitialPortModel }
  .flatMap { it.getLinks().toMap().values.flatMap {
    kotlin.collections.listOf(
      it.getSourcePort(),
      it.getTargetPort()
    )
  } }
  .map { it as InitialPortModel }
  .map { it.getNode() }
  .filter { it.getID() != this.getID() }

fun NodeModel.selectAll2(): List<NodeModel> {
  val nearNodes = neighbors()
    .filterNot { it.isSelected() }
    .map { it.also { it.setSelected(true) } }

  return nearNodes.plus(this).plus(nearNodes.flatMap { it.selectAllNodes2() }).distinctBy { it.getID() }
}

fun NodeModel.selectAllNodes(): List<NodeModel> {
  val nearNodes = neighbors()
    .filterNot { it.isSelected() }
    .map { it.also { it.setSelected(true) } }

  return nearNodes.plus(this).plus(nearNodes.flatMap { it.selectAllNodes() }).distinctBy { it.getID() }
}

fun NodeModel.selectAllNodes2(): List<NodeModel> {
  val nearNodes = neighborsAll()
    .filterNot { it.isSelected() }
    .map { it.also { it.setSelected(true) } }

  return nearNodes.plus(this).plus(nearNodes.flatMap { it.selectAllNodes2() }).distinctBy { it.getID() }
}


fun NodeModel.inPorts() = getPorts().toMap().values.map { it as InitialPortModel }.filter { it.portType == PortType.In }

fun NodeModel.outPorts() = getPorts().toMap().values.map { it as InitialPortModel }.filter { it.portType == PortType.Out }

fun NodeModel.outLinks() = outPorts().flatMap { it.getLinks().toMap().values }

fun LinkModel<*>.inPort(): InitialPortModel {
  val initialSource = getSourcePort() as InitialPortModel
  val initialTarget = getTargetPort() as InitialPortModel

  return if (initialSource.portType == PortType.In) initialSource else initialTarget
}

fun LinkModel<*>.outPort(): InitialPortModel {
  val initialSource = getSourcePort() as InitialPortModel
  val initialTarget = getTargetPort() as InitialPortModel

  return if (initialSource.portType == PortType.Out) initialSource else initialTarget
}