package com.sergstas.debtstracker.domain.usecases.currencies

import com.sergstas.debtstracker.domain.repo.ICurrencyRepo
import javax.inject.Inject

class GetAllCurrenciesUC @Inject constructor(
    private val currenciesRepo: ICurrencyRepo,
) {
    suspend operator fun invoke() =
        currenciesRepo.getAll()
}