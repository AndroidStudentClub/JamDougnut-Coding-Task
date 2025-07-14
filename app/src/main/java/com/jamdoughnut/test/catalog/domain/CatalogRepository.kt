package com.jamdoughnut.test.catalog.domain

import com.jamdoughnut.test.catalog.data.dto.ProductCatalog

interface CatalogRepository {

    suspend fun getProductCatalog(): ProductCatalog
}