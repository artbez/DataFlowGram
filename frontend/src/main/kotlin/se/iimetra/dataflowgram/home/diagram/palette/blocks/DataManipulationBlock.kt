package se.iimetra.dataflowgram.home.diagram.palette.blocks


import kotlinext.js.JsObject
import kotlinext.js.invoke
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.JSON
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import react.*
import react.dom.div
import react.dom.h4
import react.dom.hr
import se.iimetra.dataflowgram.home.diagram.palette.FunctionFile
import se.iimetra.dataflowgram.wrappers.react.devextreme.TreeView

@UnstableDefault
class CategoryBlock : RComponent<CategoryBlock.Props, RState>() {

  companion object {
    init {
      kotlinext.js.require("styles/category-block-styles.scss")
    }
  }


  override fun RBuilder.render() {
    div("palette__block") {
      h4("palette__block__title") {
        +props.name
      }

      hr("palette__block__line") {}

      TreeView {
        attrs {
          id = "treeview"
          items = kotlin.js.JSON.parse(test)
          searchEnabled = true
        }
      }
    }
  }

  interface Props: RProps {
    var name: String
    var content: List<FunctionFile>
  }
}

@UnstableDefault
fun RBuilder.categoryBlock(handler: RHandler<CategoryBlock.Props>) =
  child(CategoryBlock::class, handler)
val test = """
  [{
  "id": "1",
  "text": "Stores",
  "expanded": true,
  "items": [{
    "id": "1_1",
    "text": "Super Mart of the West"
    }]
  }]
""".trimIndent()
val products = """
  [{
  id: '1',
  text: 'Stores',
  expanded: true,
  items: [{
    id: '1_1',
    text: 'Super Mart of the West',
    expanded: true,
    items: [{
      id: '1_1_1',
      text: 'Video Players',
      items: [{
        id: '1_1_1_1',
        text: 'HD Video Player',
        price: 220
      }, {
        id: '1_1_1_2',
        text: 'SuperHD Video Player',
        price: 270
      }]
    }, {
      id: '1_1_2',
      text: 'Televisions',
      items: [{
        id: '1_1_2_1',
        text: 'SuperLCD 42',
        price: 1200
      }, {
        id: '1_1_2_2',
        text: 'SuperLED 42',
        price: 1450
      }, {
        id: '1_1_2_3',
        text: 'SuperLED 50',
        price: 1600
      }, {
        id: '1_1_2_4',
        text: 'SuperLCD 55',
        price: 1350
      }, {
        id: '1_1_2_5',
        text: 'SuperLCD 70',
        price: 4000
      }]
    }, {
      id: '1_1_3',
      text: 'Monitors',
      items: [{
        id: '1_1_3_1',
        text: '19"',
        items: [{
          id: '1_1_3_1_1',
          text: 'DesktopLCD 19',
          price: 160
        }]
      }, {
        id: '1_1_3_2',
        text: '21"',
        items: [{
          id: '1_1_3_2_1',
          text: 'DesktopLCD 21',
          price: 170
        }, {
          id: '1_1_3_2_2',
          text: 'DesktopLED 21',
          price: 175
        }]
      }]
    }, {
      id: '1_1_4',
      text: 'Projectors',
      items: [{
        id: '1_1_4_1',
        text: 'Projector Plus',
        price: 550
      }, {
        id: '1_1_4_2',
        text: 'Projector PlusHD',
        price: 750
      }]
    }]

  }, {
    id: '1_2',
    text: 'Braeburn',
    items: [{
      id: '1_2_1',
      text: 'Video Players',
      items: [{
        id: '1_2_1_1',
        text: 'HD Video Player',
        price: 240
      }, {
        id: '1_2_1_2',
        text: 'SuperHD Video Player',
        price: 300
      }]
    }, {
      id: '1_2_2',
      text: 'Televisions',
      items: [{
        id: '1_2_2_1',
        text: 'SuperPlasma 50',
        price: 1800
      }, {
        id: '1_2_2_2',
        text: 'SuperPlasma 65',
        price: 3500
      }]
    }, {
      id: '1_2_3',
      text: 'Monitors',
      items: [{
        id: '1_2_3_1',
        text: '19"',
        items: [{
          id: '1_2_3_1_1',
          text: 'DesktopLCD 19',
          price: 170
        }]
      }, {
        id: '1_2_3_2',
        text: '21"',
        items: [{
          id: '1_2_3_2_1',
          text: 'DesktopLCD 21',
          price: 180
        }, {
          id: '1_2_3_2_2',
          text: 'DesktopLED 21',
          price: 190
        }]
      }]
    }]

  }, {
    id: '1_3',
    text: 'E-Mart',
    items: [{
      id: '1_3_1',
      text: 'Video Players',
      items: [{
        id: '1_3_1_1',
        text: 'HD Video Player',
        price: 220
      }, {
        id: '1_3_1_2',
        text: 'SuperHD Video Player',
        price: 275
      }]
    }, {
      id: '1_3_3',
      text: 'Monitors',
      items: [{
        id: '1_3_3_1',
        text: '19"',
        items: [{
          id: '1_3_3_1_1',
          text: 'DesktopLCD 19',
          price: 165
        }]
      }, {
        id: '1_3_3_2',
        text: '21"',
        items: [{
          id: '1_3_3_2_1',
          text: 'DesktopLCD 21',
          price: 175
        }]
      }]
    }]
  }, {
    id: '1_4',
    text: 'Walters',
    items: [{
      id: '1_4_1',
      text: 'Video Players',
      items: [{
        id: '1_4_1_1',
        text: 'HD Video Player',
        price: 210
      }, {
        id: '1_4_1_2',
        text: 'SuperHD Video Player',
        price: 250
      }]
    }, {
      id: '1_4_2',
      text: 'Televisions',
      items: [{
        id: '1_4_2_1',
        text: 'SuperLCD 42',
        price: 1100
      }, {
        id: '1_4_2_2',
        text: 'SuperLED 42',
        price: 1400
      }, {
        id: '1_4_2_3',
        text: 'SuperLED 50',
        price: 1500
      }, {
        id: '1_4_2_4',
        text: 'SuperLCD 55',
        price: 1300
      }, {
        id: '1_4_2_5',
        text: 'SuperLCD 70',
        price: 4000
      }, {
        id: '1_4_2_6',
        text: 'SuperPlasma 50',
        price: 1700
      }]
    }, {
      id: '1_4_3',
      text: 'Monitors',
      items: [{
        id: '1_4_3_1',
        text: '19"',
        items: [{
          id: '1_4_3_1_1',
          text: 'DesktopLCD 19',
          price: 160
        }]
      }, {
        id: '1_4_3_2',
        text: '21"',
        items: [{
          id: '1_4_3_2_1',
          text: 'DesktopLCD 21',
          price: 170
        }, {
          id: '1_4_3_2_2',
          text: 'DesktopLED 21',
          price: 180
        }]
      }]
    }, {
      id: '1_4_4',
      text: 'Projectors',
      items: [{
        id: '1_4_4_1',
        text: 'Projector Plus',
        price: 550
      }, {
        id: '1_4_4_2',
        text: 'Projector PlusHD',
        price: 750
      }]
    }]

  }]
}]
""".trimIndent()