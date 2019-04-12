package se.iimetra.dataflowgram.home.diagram.palette

import kotlinx.html.Draggable
import kotlinx.html.draggable
import react.*
import react.dom.div
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class PaletteNode : RComponent<PaletteNode.Props, RState>() {

    override fun RBuilder.render() {
        div("node_item") {
            div("node_item__widget") {
                attrs {
                    draggable = Draggable.htmlTrue
                }
                this.children()
            }
            div("node_item__title ") {
                +props.label
            }
        }
    }

    interface Props : RProps {
        var label: String
    }
}

fun RBuilder.paletteNode(handler: RHandler<PaletteNode.Props>) = child(PaletteNode::class, handler)