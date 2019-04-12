package se.iimetra.dataflowgram.home.diagram.palette

import kotlinx.html.Draggable
import kotlinx.html.draggable
import react.*
import react.dom.div

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


data class FunctionCategory(val name: String, val files: List<FunctionFile>)

data class FunctionFile(val name: String, val functions: List<CustomFunction>)

data class CustomFunction(val name: String, val signature: String)