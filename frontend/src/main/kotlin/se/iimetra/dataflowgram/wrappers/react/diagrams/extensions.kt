package se.iimetra.dataflowgram.wrappers.react.diagrams

import se.iimetra.dataflowgram.wrappers.react.diagrams.defaults.DefaultLinkModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.BaseModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.LinkModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel
import kotlinext.js.toPlainObjectStripNull
import react.RBuilder
import react.RHandler


@JsName("DefaultLinkModelListener")
abstract class DefaultLinkModelListener : LinkModelListener() {

    abstract fun <T> colorChanged(event: T) where T : BaseEvent<DefaultLinkModel>, T : ColorHave

    abstract fun <T> widthChanged(event: T) where T : BaseEvent<DefaultLinkModel>, T : WidthHave

    interface ColorHave {
        var color: String?
    }

    interface WidthHave {
        var width: Double
    }
}

@JsName("LinkModelListener")
abstract class LinkModelListener : BaseModelListener() {
    abstract fun <T> sourcePortChanged(event: T) where T : BaseEvent<LinkModel<LinkModelListener>>, T : Portable
    abstract fun <T> targetPortChanged(event: T) where T : BaseEvent<LinkModel<LinkModelListener>>, T : Portable

    interface Portable {
        var port: PortModel?
    }
}

@JsName("BaseModelListener")
open class BaseModelListener : BaseListener {
    var selectionChanged: ((event: BaseModelListener.SelectedEvent) -> Unit)? = null
    override var lockChanged: ((event: BaseEvent<dynamic>, BaseListener.Locked) -> Unit)? = null
    var entityRemoved: ((event: BaseEvent<BaseModel<dynamic, dynamic>>) -> Unit)? = null

    interface Selected {
        var isSelected: Boolean
    }

    interface SelectedEvent : BaseEvent<BaseModel<dynamic, dynamic>>, Selected

    fun events(builder: BaseModelListener.() -> Unit): BaseModelListener {
        this.builder()
        return toPlainObjectStripNull(this)
    }
}

fun RBuilder.diagramWidget(handler: RHandler<DiagramWidgetProps>) = child(DiagramWidget::class, handler)