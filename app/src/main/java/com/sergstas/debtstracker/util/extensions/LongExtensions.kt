package com.sergstas.debtstracker.util.extensions

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

fun Long.formatAsDate(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ROOT)
    val ldt = LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)
    return ldt.format(formatter)
}