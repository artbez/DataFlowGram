@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams.models

import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseEntity
import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseModelListener

external interface SelectionModel {
    var model: BaseModel<BaseEntity<BaseListener>, BaseModelListener>
    var initialX: Double
    var initialY: Double
}