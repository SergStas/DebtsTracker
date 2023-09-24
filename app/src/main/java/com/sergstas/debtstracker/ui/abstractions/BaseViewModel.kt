package com.sergstas.debtstracker.ui.abstractions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sergstas.debtstracker.DebtsRecorderApplication

abstract class BaseViewModel(app: Application): AndroidViewModel(app) {
    protected val context
        get() = getApplication<DebtsRecorderApplication>()
}