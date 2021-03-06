@file:JsModule("react-bootstrap")
package se.iimetra.dataflowgram.wrappers.react.bootstrap

import react.*

external interface PopoverProps : RProps {
  var id : String
  var placement: String // top, bottom, left, right
  var title: String
  var container: dynamic
}

@JsName("Popover")
external val Popover : RClass<PopoverProps>

external interface OverlayTriggerProps : RProps {
  var trigger : dynamic // click
  var placement: String // top, bottom, left, right
  var overlay: ReactElement
}

@JsName("OverlayTrigger")
external val OverlayTrigger: RClass<OverlayTriggerProps>

