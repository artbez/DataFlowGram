package se.iimetra.dataflowgram.home.diagram.palette

import kotlinext.js.invoke
import react.*
import react.dom.div
import se.iimetra.dataflowgram.home.diagram.palette.blocks.dataManipulationBlock
import se.iimetra.dataflowgram.wrappers.react.tabs.Tab
import se.iimetra.dataflowgram.wrappers.react.tabs.TabList
import se.iimetra.dataflowgram.wrappers.react.tabs.TabPanel
import se.iimetra.dataflowgram.wrappers.react.tabs.Tabs

class Palette : RComponent<Palette.Props, RState>() {

    companion object {
        init {
            kotlinext.js.require("styles/palette-styles.scss")
        }
    }

    override fun RBuilder.render() {
        div("palette") {
            Tabs {
                TabList {
                    Tab {
                        +"t1"
                    }
                    Tab {
                        +"t2"
                    }
                }
                TabPanel {
                    +"b1"
                }
                TabPanel {
                    +"b2"
                }
            }
        }
    }

    interface Props : RProps {
    }
}

fun RBuilder.palette(handler: RHandler<Palette.Props>) = child(Palette::class, handler)