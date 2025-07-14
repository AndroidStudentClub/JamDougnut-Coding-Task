package com.jamdoughnut.test.catalog.domain

import com.jamdoughnut.test.catalog.data.dto.ProductCatalog
import javax.inject.Inject

class CatalogInteractor @Inject constructor(val repository: CatalogRepository) {

    suspend fun getCatalog(): ProductCatalog {
        return repository.getProductCatalog()
    }
}