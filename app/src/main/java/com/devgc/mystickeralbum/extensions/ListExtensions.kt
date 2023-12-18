package com.devgc.mystickeralbum.extensions

fun <T> List<T>.toGrid(columns: Int): List<List<T>> {
    val grid = ArrayList<ArrayList<T>>()
    val row = ArrayList<T>()
    this.forEach { element ->
        row.add(element)
        if (row.size == columns) {
            grid.add(ArrayList(row))
            row.clear()
        }
    }

    if (row.isNotEmpty()) {
        grid.add(row)
    }

    return grid
}