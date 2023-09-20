package com.sergstas.debtstracker.util.extensions

import androidx.fragment.app.Fragment

fun Fragment.toast(msg: String) =
    requireContext().toast(msg)

fun Fragment.colorFromId(id: Int) =
    requireContext().colorFromId(id)

fun Fragment.drawableFromId(id: Int) =
    requireContext().drawableFromId(id)