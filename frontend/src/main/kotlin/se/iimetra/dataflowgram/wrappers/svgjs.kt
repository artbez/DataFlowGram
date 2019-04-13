package se.iimetra.dataflowgram.wrappers

import kotlinx.serialization.Serializable

@JsModule("svgjs")
external fun svgjs(id: String): SvgElem = definedExternally

@Serializable
class StrokeParams(var width: Int? = null, var color: String?)

external class SvgElem {
  fun fill(color: String?): SvgElem
  fun rect(height: Int, width: Int): SvgElem
  fun stroke(params: dynamic): SvgElem
  fun text(text: String): SvgElem
}