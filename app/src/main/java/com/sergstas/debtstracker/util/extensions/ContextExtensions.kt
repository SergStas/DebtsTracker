package com.sergstas.debtstracker.util.extensions

import android.content.Context
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat

fun Context.toast(msg: String) =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context.colorFromId(colorId: Int) =
    ResourcesCompat.getColor(resources, colorId, null)

fun Context.drawableFromId(id: Int) =
    ResourcesCompat.getDrawable(resources, id, null)