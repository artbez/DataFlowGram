@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams.models

import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseModelListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.LinkModelListener

open external class LabelModel(offsetX: Double, offsetY: Double) : BaseModel<LinkModel<LinkModelListener>, BaseModelListener>
