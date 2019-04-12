@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams.models

import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseModelListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.LinkModelListener

open external class PointModel(link: LinkModel<LinkModelListener>, points: Point) :
    BaseModel<LinkModel<LinkModelListener>, BaseModelListener> {

    fun getLink(): LinkModel<LinkModelListener>
    fun updateLocation(points: Point)
    fun getX(): Double
    fun getY(): Double

    interface Point {
        var x: Double
        var y: Double
    }
}