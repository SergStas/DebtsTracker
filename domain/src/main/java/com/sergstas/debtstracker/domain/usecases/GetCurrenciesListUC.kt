package com.sergstas.debtstracker.domain.usecases

import javax.inject.Inject

class GetCurrenciesListUC @Inject constructor(

) {
    suspend operator fun invoke() =
        listOf("rub")
}