package com.jamdoughnut.test.catalog.data.repository

import com.jamdoughnut.test.catalog.data.dto.ProductCatalog
import com.jamdoughnut.test.catalog.domain.CatalogRepository
import com.jamdoughnut.test.utils.mockResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class MockCatalogRepository : CatalogRepository {

    override suspend fun getProductCatalog(): ProductCatalog {
        val jsonString: String = mockResponse
        // Imagine that this is network call, that's why use suspend
        return withContext(Dispatchers.IO) {
            Json.decodeFromString<ProductCatalog>(jsonString)
        }
    }
}