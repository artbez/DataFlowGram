@file:JsModule("react-tabs")
package se.iimetra.dataflowgram.wrappers.react.tabs

import react.RClass
import react.RProps

external interface TabsProps: RProps {
  var className: String
  var disabled: Boolean
  var selectedIndex: Int
  val onSelect: (Int) -> Unit
}

@JsName("TabList")
external val TabList: RClass<RProps>

@JsName("TabPanel")
external val TabPanel: RClass<RProps>

@JsName("Tab")
external val Tab: RClass<RProps>

@JsName("Tabs")
external val Tabs: RClass<TabsProps>

