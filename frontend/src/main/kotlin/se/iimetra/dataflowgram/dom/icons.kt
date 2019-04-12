package se.iimetra.dataflowgram.dom

import kotlinx.html.SVG
import react.RBuilder
import react.dom.RDOMBuilder
import react.dom.title

fun RBuilder.netIcon(classes: String? = null, block: RDOMBuilder<SVG>.() -> Unit) {
    svg(classes, "0 0 1024 1024") {
        title("Net")
        path("M789.984 513.184c73.376 79.136 106.24 156.992 77.728 206.912-28.448 49.792-111.392 60.064-215.616 35.648-31.2 104.288-81.728 172.384-138.912 172.384-57.248 0-107.744-68.32-138.88-172.896-105.376 25.12-189.312 15.040-217.984-35.136-28.512-49.92 4.384-127.872 77.76-207.104-74.784-79.872-108.448-158.72-79.68-209.024 28.8-50.432 113.536-60.384 219.648-34.784 31.168-104.8 81.856-173.312 139.168-173.312 57.28 0 107.808 68.288 138.944 172.832 105.12-24.96 188.928-14.848 217.568 35.264 28.672 50.336-5.024 129.248-79.744 209.216zM184.064 321.312c-22.016 38.528 8.352 101.696 72.64 168.48 26.208-25.568 56.384-50.944 89.984-75.328 4.096-40.512 10.336-78.944 18.88-113.92-89.824-23.072-159.392-17.92-181.504 20.768zM343.072 459.552c-23.040 17.536-44.064 35.328-62.912 53.088 19.104 17.856 39.712 35.776 63.040 53.44-0.96-17.76-1.632-35.712-1.632-54.080 0-17.792 0.576-35.264 1.504-52.448zM346.656 611.008c-33.632-24.288-63.712-49.6-90.016-75.072-63.136 66.176-92.448 128.608-70.624 166.816 21.984 38.432 90.816 43.776 179.84 21.184-8.544-34.784-15.136-72.608-19.2-112.928zM642.848 634.72c-14.272 9.376-28.864 18.592-44.064 27.456-15.424 8.992-30.848 17.248-46.208 25.12 26.336 11.2 52.096 20.96 76.544 28.448 5.728-25.088 10.176-52.448 13.728-81.024zM513.152 893.44c43.808 0 82.688-57.12 107.84-145.824-34.72-9.984-71.392-23.872-108.768-40.768-36.704 16.672-72.608 30.080-106.784 40.096 25.088 88.928 63.904 146.496 107.712 146.496zM397.536 714.944c23.744-7.392 48.576-16.576 74.112-27.456-15.424-7.904-30.944-16.256-46.432-25.28-14.368-8.416-28.192-17.056-41.728-25.92 3.488 28.064 8.448 53.952 14.048 78.656zM383.552 388.992c14.112-9.248 28.576-18.336 43.584-27.136 14.816-8.672 29.632-16.64 44.416-24.256-25.632-10.944-50.72-20.608-74.592-28.064-5.568 24.64-9.888 51.488-13.408 79.456zM513.152 130.56c-43.936 0-83.008 57.664-108.128 147.008 34.144 10.048 70.112 23.84 106.752 40.544 37.504-17.184 74.176-30.912 109.088-41.088-25.088-88.864-63.872-146.464-107.712-146.464zM628.832 308.96c-24.576 7.648-50.24 17.12-76.736 28.512 14.88 7.648 29.824 15.68 44.736 24.416 16.064 9.376 31.424 19.136 46.432 29.056-3.52-29.28-8.608-56.32-14.432-81.984zM647.68 435.328c-21.44-14.912-44.096-29.472-68.032-43.392-22.912-13.376-45.632-25.056-68.096-35.968-22.176 10.816-44.672 22.816-67.232 35.968-22.816 13.28-44.608 27.168-65.12 41.312-1.984 25.504-3.328 51.616-3.328 78.784 0 27.456 1.12 54.176 3.104 79.968 20.064 13.792 41.248 27.264 63.392 40.192 23.552 13.728 46.912 25.728 70.016 36.864 22.816-11.072 45.984-23.328 69.184-36.864 23.008-13.408 44.864-27.36 65.568-41.664 1.952-25.408 3.328-51.424 3.328-78.496 0-26.304-0.96-51.936-2.784-76.704zM837.984 702.752c21.824-38.144-7.744-100.48-70.752-166.496-25.632 24.768-54.976 49.344-87.552 72.992-4.096 41.024-10.432 79.968-19.136 115.328 87.872 21.92 155.68 16.256 177.44-21.824zM683.296 564.16c22.048-16.832 42.272-33.92 60.512-50.912-18.304-17.28-38.048-34.656-60.384-51.776 0.8 16.608 1.376 33.408 1.376 50.528-0.032 17.728-0.608 35.072-1.504 52.16zM839.904 321.312c-21.952-38.368-90.56-43.808-179.36-21.312 8.736 35.776 15.488 74.848 19.52 116.512 32.608 23.84 61.76 48.64 87.296 73.568 64.416-66.912 94.624-130.176 72.544-168.768zM518.4 577.984c-37.664 0-68.192-30.816-68.192-68.864 0-38.016 30.528-68.864 68.192-68.864 37.632 0 68.16 30.848 68.16 68.864 0 38.080-30.528 68.864-68.16 68.864z")
        block()
    }
}

fun RBuilder.netTrainIcon(classes: String? = null) {
    svg(classes, "0 0 1024 1024") {
        title("Train")
        path("M1024 256c0-44.992-30.624-83.456-74.432-93.568l-416.416-96.096c-6.976-1.568-14.080-2.336-21.152-2.336s-14.176 0.768-21.6 2.432l-415.968 96c-43.84 10.112-74.432 48.576-74.432 93.568s30.624 83.456 74.4 93.568l85.6 19.744v270.688c0 84.928 97.216 160 352 160s352-75.072 352-160v-270.688l85.568-19.744c43.808-10.112 74.432-48.576 74.432-93.568zM800 640c0 35.36-96 96-288 96s-288-60.64-288-96v-255.904l266.816 61.568c6.976 1.568 14.112 2.336 21.184 2.336s14.208-0.768 21.568-2.432l266.432-61.472v255.904zM519.2 383.2c-2.4 0.512-4.8 0.8-7.2 0.8s-4.832-0.288-7.2-0.8l-416-96c-14.528-3.36-24.8-16.288-24.8-31.2s10.272-27.84 24.8-31.2l416-96c2.368-0.512 4.768-0.8 7.2-0.8s4.832 0.288 7.2 0.8l416 96c14.496 3.36 24.8 16.288 24.8 31.2s-10.304 27.84-24.8 31.2l-416 96zM928 416c0-17.696 14.304-32 32-32 17.664 0 32 14.304 32 32v288c0 17.696-14.336 32-32 32-17.696 0-32-14.304-32-32v-288zM960 768c35.328 0 64 92.672 64 128s-28.672 64-64 64c-35.36 0-64-28.672-64-64s28.64-128 64-128z")
    }
}

fun RBuilder.netEvalIcon(classes: String? = null, block: RDOMBuilder<SVG>.() -> Unit) {
    svg(classes, "0 0 1024 1024") {
        title("Eval")
        path("M927.936 272.992l-68.288-68.288c-12.608-12.576-32.96-12.576-45.536 0l-409.44 409.44-194.752-196.16c-12.576-12.576-32.928-12.576-45.536 0l-68.288 68.288c-12.576 12.608-12.576 32.96 0 45.536l285.568 287.488c12.576 12.576 32.96 12.576 45.536 0l500.736-500.768c12.576-12.544 12.576-32.96 0-45.536z")
        block()
    }
}

fun RBuilder.uploadDatasetIcon(classes: String? = null, block: RDOMBuilder<SVG>.() -> Unit) {
    svg(classes, "0 0 1024 1024") {
        title("Upload datasetMeta")
        path("M480 832h-384.225c-17.499 0-31.775-14.209-31.775-31.738v-352.262h864v351.972c0 17.8-14.226 32.028-31.775 32.028h-384.225v-288l104 104 24-24-144-144-144 144 24 24 104-104v288zM64 416v-192.262c0-17.168 14.208-31.738 31.736-31.738h333.065l62.719 128h404.385c17.573 0 32.095 14.339 32.095 32.028v63.972h-864zM512 288l-64-128h-351.912c-35.395 0-64.088 28.47-64.088 63.717v576.566c0 35.19 28.791 63.717 63.785 63.717h800.43c35.228 0 63.785-28.564 63.785-63.843v-448.314c0-35.259-28.706-63.843-64.187-63.843h-383.813z")
        block()
    }
}

fun RBuilder.alertIcon(classes: String? = null, block: RDOMBuilder<SVG>.() -> Unit) {
    svg(classes, "0 0 768 768") {
        title("Alert")
        path("M608 64h-448c-54.4 0-96 41.6-96 96v512c0 12.8 6.4 25.6 19.2 28.8 3.2 3.2 9.6 3.2 12.8 3.2 9.6 0 16-3.2 22.4-9.6l118.4-118.4h371.2c54.4 0 96-41.6 96-96v-320c0-54.4-41.6-96-96-96zM640 480c0 19.2-12.8 32-32 32h-384c-9.6 0-16 3.2-22.4 9.6l-73.6 73.6v-435.2c0-19.2 12.8-32 32-32h448c19.2 0 32 12.8 32 32v320z")
        block()
    }
}

fun RBuilder.cabinetIcon(classes: String? = null, block: RDOMBuilder<SVG>.() -> Unit) {
    svg(classes, "0 0 1024 1024") {
        title("DatasetType")
        path("M160 960v-768l133.344-128h405.344l133.312 128v768h-672zM800 704v-480h-608v704h608v-224zM768 448h-544v-192h544v192zM576 320h-160v32h160v-32zM768 672h-544v-192h544v192zM576 544h-160v32h160v-32zM768 896h-544v-192h544v192zM576 768h-160v32h160v-32z")
        block()
    }
}

fun RBuilder.settingsIcon(classes: String? = null, block: RDOMBuilder<SVG>.() -> Unit) {
  svg(classes, "0 0 32 32") {
    title("Settings")
    path("M11.366 22.564l1.291-1.807-1.414-1.414-1.807 1.291c-0.335-0.187-0.694-0.337-1.071-0.444l-0.365-2.19h-2l-0.365 2.19c-0.377 0.107-0.736 0.256-1.071 0.444l-1.807-1.291-1.414 1.414 1.291 1.807c-0.187 0.335-0.337 0.694-0.443 1.071l-2.19 0.365v2l2.19 0.365c0.107 0.377 0.256 0.736 0.444 1.071l-1.291 1.807 1.414 1.414 1.807-1.291c0.335 0.187 0.694 0.337 1.071 0.444l0.365 2.19h2l0.365-2.19c0.377-0.107 0.736-0.256 1.071-0.444l1.807 1.291 1.414-1.414-1.291-1.807c0.187-0.335 0.337-0.694 0.444-1.071l2.19-0.365v-2l-2.19-0.365c-0.107-0.377-0.256-0.736-0.444-1.071zM7 27c-1.105 0-2-0.895-2-2s0.895-2 2-2 2 0.895 2 2-0.895 2-2 2zM32 12v-2l-2.106-0.383c-0.039-0.251-0.088-0.499-0.148-0.743l1.799-1.159-0.765-1.848-2.092 0.452c-0.132-0.216-0.273-0.426-0.422-0.629l1.219-1.761-1.414-1.414-1.761 1.219c-0.203-0.149-0.413-0.29-0.629-0.422l0.452-2.092-1.848-0.765-1.159 1.799c-0.244-0.059-0.492-0.109-0.743-0.148l-0.383-2.106h-2l-0.383 2.106c-0.251 0.039-0.499 0.088-0.743 0.148l-1.159-1.799-1.848 0.765 0.452 2.092c-0.216 0.132-0.426 0.273-0.629 0.422l-1.761-1.219-1.414 1.414 1.219 1.761c-0.149 0.203-0.29 0.413-0.422 0.629l-2.092-0.452-0.765 1.848 1.799 1.159c-0.059 0.244-0.109 0.492-0.148 0.743l-2.106 0.383v2l2.106 0.383c0.039 0.251 0.088 0.499 0.148 0.743l-1.799 1.159 0.765 1.848 2.092-0.452c0.132 0.216 0.273 0.426 0.422 0.629l-1.219 1.761 1.414 1.414 1.761-1.219c0.203 0.149 0.413 0.29 0.629 0.422l-0.452 2.092 1.848 0.765 1.159-1.799c0.244 0.059 0.492 0.109 0.743 0.148l0.383 2.106h2l0.383-2.106c0.251-0.039 0.499-0.088 0.743-0.148l1.159 1.799 1.848-0.765-0.452-2.092c0.216-0.132 0.426-0.273 0.629-0.422l1.761 1.219 1.414-1.414-1.219-1.761c0.149-0.203 0.29-0.413 0.422-0.629l2.092 0.452 0.765-1.848-1.799-1.159c0.059-0.244 0.109-0.492 0.148-0.743l2.106-0.383zM21 15.35c-2.402 0-4.35-1.948-4.35-4.35s1.948-4.35 4.35-4.35 4.35 1.948 4.35 4.35c0 2.402-1.948 4.35-4.35 4.35z")
    block()
  }
}

fun RBuilder.logIcon(classes: String? = null, block: RDOMBuilder<SVG>.() -> Unit) {
  svg(classes, "0 0 32 32") {
    title("Logs")
    path("M12 20l4-2 14-14-2-2-14 14-2 4zM9.041 27.097c-0.989-2.085-2.052-3.149-4.137-4.137l3.097-8.525 4-2.435 12-12h-6l-12 12-6 20 20-6 12-12v-6l-12 12-2.435 4z")
    block()
  }
}