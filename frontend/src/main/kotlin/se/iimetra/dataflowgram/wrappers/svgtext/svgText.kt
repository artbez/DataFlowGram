package se.iimetra.dataflowgram.wrappers.svgtext

import react.RProps


//external val svgText: dynamic = definedExternally

external interface SvgTextProps: RProps {
  var text: String
  var maxWidth: Int
  var maxHeight: Int
  var rect: dynamic
}

@JsModule("svg-text")
external val SvgText: SVT = definedExternally

external class SST(props: dynamic)

external class SVT {
  val default: dynamic = definedExternally
}