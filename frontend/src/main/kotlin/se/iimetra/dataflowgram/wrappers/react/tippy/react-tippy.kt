@file:JsModule("react-tippy")
package se.iimetra.dataflowgram.wrappers.react.tippy

import react.RClass
import react.RProps

external interface TooltipProps : RProps {
    var title: String
    var position: String // top, bottom, left, right
    var trigger: String // mouseenter, focus, click, manual
    var disabled: Boolean // Show or not show tooltip
    var theme: String // dark, light, transparent
    var open: Boolean
    var sticky: Boolean
    var animation: String
    var animateFill: Boolean
    var unmountHTMLWhenHide: Boolean
    var duration: Int
}

@JsName("Tooltip")
external val Tooltip : RClass<TooltipProps>

