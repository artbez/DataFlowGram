package se.iimetra.dataflowgram.header

import se.iimetra.dataflowgram.wrappers.reactRouter
import kotlinext.js.invoke
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.li
import react.dom.nav
import react.dom.ul

class HeaderComponent : RComponent<RProps, RState>() {

    companion object {
        init {
            kotlinext.js.require("styles/header.scss")
        }
    }

    override fun RBuilder.render() {
        nav("navbar navbar-default header-nav navbar-fixed-top") {
            div("container-fluid") {
                div("navbar-header") {
                    homeLink("navbar-brand")
                }
                ul("nav navbar-nav") {
                    li("active") {
                        homeLink()
                    }
                }
            }
        }
    }
}

val RBuilder.header
    get() = child(HeaderComponent::class) {}

private fun RBuilder.homeLink(className: String = "") = reactRouter.Link {
    attrs {
        to = "/"
        this.className = className
    }
    +"Home"
}
