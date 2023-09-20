package com.sergstas.debtstracker.domain.usecases.currencies

import javax.inject.Inject

class GetCurrenciesListUseCase @Inject constructor(

) {
    suspend operator fun invoke() =
        listOf("rub")
}