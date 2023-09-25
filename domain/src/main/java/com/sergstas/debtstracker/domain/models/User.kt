package com.sergstas.debtstracker.domain.models

import java.io.Serializable

data class User(
    val guid: String,
    val username: String,
    val isReal: Boolean,
): Serializable
