package se.iimetra.dataflowgram.wrappers.lodash

@JsModule("lodash")
external val lodash: Lodash = definedExternally

external class Lodash {
  fun merge(obj: dynamic, array: dynamic): dynamic
}