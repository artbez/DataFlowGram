@file:JsModule("devextreme-react")
package se.iimetra.dataflowgram.wrappers.react.devextreme

import react.RClass
import react.RProps

external interface TreeViewProps: RProps {
  var id: String
  var items: dynamic
  var searchEnabled: Boolean
}

@JsName("TreeView")
external val TreeView: RClass<TreeViewProps>

