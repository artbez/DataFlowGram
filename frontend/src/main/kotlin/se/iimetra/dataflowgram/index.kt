import se.iimetra.dataflowgram.wrappers.reactRouter
import kotlinext.js.invoke
import react.dom.render
import se.iimetra.dataflowgram.home.HomeComponent
import kotlin.browser.document

fun main(args: Array<String>) {

    kotlinext.js.require.invoke("storm-react-diagrams/dist/style.min.css")
    kotlinext.js.require.invoke("styles/home-diagram-styles.scss")
    kotlinext.js.require.invoke("react-tippy/dist/tippy.css")
    kotlinext.js.require.invoke("react-tabs/style/react-tabs.scss")
    kotlinext.js.require.invoke("devextreme/dist/css/dx.common.css")
    kotlinext.js.require.invoke("devextreme/dist/css/dx.light.compact.css")

    render(document.getElementById("root")) {
        reactRouter.BrowserRouter {
            reactRouter.Route {
                attrs {
                    path = "/"
                    component = ::HomeComponent
                }
            }
        }
    }
}
