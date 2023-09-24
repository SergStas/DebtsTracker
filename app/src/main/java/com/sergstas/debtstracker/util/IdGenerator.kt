package com.sergstas.debtstracker.util

import javax.inject.Inject

class IdGenerator @Inject constructor() {
    private var index = 0L

    operator fun invoke() = ++index
}