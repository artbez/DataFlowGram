package se.iimetra.dataflowgram.home.diagram.palette

import kotlinext.js.invoke
import react.*
import react.dom.div
import se.iimetra.dataflowgram.home.diagram.palette.blocks.dataManipulationBlock

class Palette : RComponent<Palette.Props, RState>() {

    companion object {
        init {
            kotlinext.js.require("styles/palette-styles.scss")
        }
    }

    override fun RBuilder.render() {
        div("palette") {
            dataManipulationBlock {  }
        }
    }

    interface Props : RProps {
    }
}

fun RBuilder.palette(handler: RHandler<Palette.Props>) = child(Palette::class, handler)