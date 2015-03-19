/**
 * Created by jean on 20.11.2014.
 */
package net.yested.bootstrap

import net.yested.HTMLComponent
import java.util.ArrayList
import net.yested.Span
import net.yested.with
import net.yested.Component
import net.yested.createElement
import net.yested.appendComponent
import net.yested.THead
import net.yested.TBody
import net.yested.removeChildByName
import kotlin.js.dom.html.HTMLElement
import net.yested.div

public data class Column<T>(
        val label: HTMLComponent.() -> Unit,
        val render: HTMLComponent.(T) -> Unit,
        val sortFunction:((T, T) -> Int)? = null,
        val align:Align = Align.LEFT,
        val defaultSort:Boolean = false,
        val defaultSortOrderAsc:Boolean = true)

public class ColumnHeader<T>(val column:Column<T>, sortingSupported:Boolean, sortFunction:((Column<T>) -> Unit)?) : HTMLComponent("span") {

    var sortOrderAsc:Boolean = column.defaultSortOrderAsc
    var arrowPlaceholder = Span()

    init {
        if (sortingSupported) {
            a(href = null, onclick = { sortFunction!!(column)} ) {
                "style".."cursor: pointer;"
                column.label()
            }
            +arrowPlaceholder
        } else {
            column.label()
        }
    }

    fun updateSorting(sortedByColumn:Column<T>?, sortAscending:Boolean) {
        if (sortedByColumn != column) {
            arrowPlaceholder.setContent("")
        } else {
            arrowPlaceholder.setChild(Glyphicon("arrow-${if (sortAscending) "up" else "down"}"))
        }
    }

}

public class Grid<T>(responsive: Boolean = false, val columns:Array<Column<T>>) : Component {

    private val tableElement = createElement("table")

    override val element: HTMLElement = if (responsive) createResponsiveWrapper() else tableElement

    private var sortColumn:Column<T>? = null
    private var asc:Boolean = true;
    private val arrowsPlaceholders = ArrayList<Span>();
    private var columnHeaders:List<ColumnHeader<T>>? = null

    private fun createResponsiveWrapper():HTMLElement {
        val div = createElement("div")
        div.setAttribute("class", "table-responsive")
        div.appendChild(tableElement)
        return div
    }

    init {
        tableElement.className = "table table-striped table-hover table-condensed"
        columnHeaders = columns.map {
            ColumnHeader(
                    column = it,
                    sortingSupported = it.sortFunction != null,
                    sortFunction = { sortByColumn(it) } )}

        renderHeader()
        sortColumn = (columns.filter { it.defaultSort } : Iterable<Column<T>>).firstOrNull()
        asc = sortColumn?.defaultSortOrderAsc ?: true
        setSortingArrow()
    }

    private var dataList: Iterable<T>? = null

    public var list: Iterable<T>?
        get() = dataList
        set(value) {
            dataList = value
            displayData()
        }

    private fun setSortingArrow() {
        columnHeaders!!.forEach { it.updateSorting(sortColumn, asc) }
    }

    private fun sortByColumn(column:Column<T>):Unit {
        if (column == sortColumn) {
            asc = !asc;
        } else {
            asc = true;
            sortColumn = column
        }
        displayData()
        setSortingArrow()
    }

    private fun renderHeader() {
        tableElement.appendComponent(THead() with
             {
                tr {
                    columnHeaders!!.forEach { columnHeader ->
                        th {
                            "class" .. "text-${columnHeader.column.align.code}";
                            +columnHeader
                        }
                    }
                }
            })
    }

    private fun sortData(toSort:Iterable<T>):Iterable<T> {
        if (sortColumn?.sortFunction == null) {
            return toSort
        }
        return toSort.sortBy(object: java.util.Comparator<T> {
            override fun compare(obj1: T, obj2: T): Int {
                return (sortColumn!!.sortFunction!!(obj1, obj2)) * (if (asc) 1 else -1)
            }
        })
    }

    public fun displayData() {
        tableElement.removeChildByName("tbody")
        dataList?.let {

            val values = if (sortColumn != null) sortData(dataList!!) else dataList!!

            tableElement.appendComponent(TBody() with {
                    values.forEach { item ->
                        tr {
                            columns.forEach { column ->
                                td {
                                    "class" .. "text-${column.align.code}";
                                    column.render(item)
								}
							}
                        }
                    }
            })

        }
    }

}