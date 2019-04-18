package se.iimetra.dataflowgram.dom

import kotlinx.html.*
import react.RBuilder
import react.ReactElement
import react.dom.RDOMBuilder
import react.dom.tag

/**
 * Custom SVG tag for inlining all svg icons (sets default attributes like version, xmlns, viewBox)
 */
inline fun RBuilder.svg(
    classes: String? = null,
    viewBox: String = "0 0 20 20",
    block: RDOMBuilder<SVG>.() -> Unit
): ReactElement =
    tag(block) {
        SVG(
            attributesMapOf(
                "class",
                classes,
                "version",
                "1.1",
                "xmlns",
                "http://www.w3.org/2000/svg",
                "viewBox",
                viewBox
            ), it
        )
    }

/**
 * Custom PATH tag to inline svg icons in SVG tags
 */
class PATH(initialAttributes: Map<String, String>, consumer: TagConsumer<*>) :
    HTMLTag(
        "path", consumer, initialAttributes,
        inlineTag = true,
        emptyTag = false
    ), HtmlInlineTag

fun RDOMBuilder<SVG>.path(path: dynamic): ReactElement = tag({}) { PATH(attributesMapOf("d", path), it) }
fun RDOMBuilder<SVG>.path(style: dynamic, path: String): ReactElement = tag({}) { PATH(attributesMapOf("style", style, "d", path), it) }