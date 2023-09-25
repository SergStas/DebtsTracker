package com.sergstas.debtstracker.util.extensions

fun<T> MutableList<T>.replaceFirst(oldValue: T, newValue: T): Int {
    val index = indexOf(oldValue).takeIf { it >= 0 } ?: return -1
    removeAt(index)
    add(index, newValue)
    return index
}