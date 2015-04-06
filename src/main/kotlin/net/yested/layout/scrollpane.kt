package net.yested.layout

import jquery.jq
import net.yested.*
import kotlin.js.dom.html.HTMLElement

public enum class Overflow(val code: String) {
    NONE : Overflow("")
    AUTO : Overflow("auto")
    HIDDEN : Overflow("hidden")
    SCROLL : Overflow("scroll")
}

public class ScrollPane(horizontal: Overflow = Overflow.NONE,
                        vertical: Overflow = Overflow.NONE,
                        width: Dimension? = null,
                        height: Dimension? = null,
                        init: HTMLComponent.() -> Unit) : HTMLComponent("div") {

    init {
        element.whenAddedToDom {
            val parentWidth = jq(element.parentNode as HTMLElement).width()
            val parentHeight = jq(element.parentNode as HTMLElement).height()
            style = overflow("x", "width", "${parentWidth}px;", horizontal, width) + " " + overflow("y", "height", "${parentHeight}px", vertical, height)
            init()
        }
    }

    private fun overflow(direction: String, dir: String, dim: String, overflow: Overflow, requestedDimension:Dimension?) =
            if (overflow != Overflow.NONE) {
                "overflow-${direction}:${overflow.code}; ${dir}:${dim}"
            } else if (requestedDimension != null) {
                "${dir}: ${requestedDimension.toHtml()}"
            } else {
                ""
            }

}

public fun HTMLComponent.scrollPane(
        horizontal: Overflow = Overflow.NONE,
        vertical: Overflow = Overflow.NONE,
        width: Dimension? = null,
        height: Dimension? = null,
        init: HTMLComponent.() -> Unit) {
    +(ScrollPane(
            horizontal = horizontal,
            vertical = vertical,
            width = width,
            height = height,
            init = init))
}