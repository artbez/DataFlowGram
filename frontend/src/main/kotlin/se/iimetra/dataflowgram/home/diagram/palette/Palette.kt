package se.iimetra.dataflowgram.home.diagram.palette

import kotlinext.js.invoke
import react.*
import react.dom.div
import se.iimetra.dataflowgram.home.diagram.palette.blocks.categoryBlock
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
                        +"Server"
                    }
                    Tab {
                        +"Client"
                    }
                }
                TabPanel {
                    allCategories.forEach {
                        categoryBlock {
                            attrs {
                                name = it.name
                                content = it.files
                            }
                        }
                    }
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

val allCategories = listOf<FunctionCategory>(
    FunctionCategory("data",
        listOf(
            FunctionFile("pandas",
                listOf(
                    CustomFunction("read_csv", "(File)->DataFrame"),
                    CustomFunction("to_json", "(DataFrame)->Json")
                )
            ),
            FunctionFile("np", listOf(
                CustomFunction("read_csv", "(File)->DataFrame"),
                CustomFunction("to_json", "(DataFrame)->Json")
            ))
        )
    ),
    FunctionCategory("models",
        listOf(
            FunctionFile("xgboost",
                listOf(
                    CustomFunction("create_model", "(DataFrame)->Model"),
                    CustomFunction("to_json", "(Model)->Json")
                )
            ),
            FunctionFile("catboost",
                listOf(
                    CustomFunction("create_model", "(DataFrame)->Model"),
                    CustomFunction("to_json", "(Model)->Json")
                )
            )
        )
    )
)