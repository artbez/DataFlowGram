package se.iimetra.dataflowgram.home.diagram

import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel


class SceneTransferObject {
    private var transferNode: NodeModel? = null

    fun putDto(node: NodeModel) {
        transferNode = node
    }

    fun getDto(): NodeModel = transferNode ?: throw IllegalStateException("There are no dto to transfer")

    fun cleanDto() {
        transferNode = null
    }
}