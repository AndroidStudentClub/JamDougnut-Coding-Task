package com.jamdoughnut.test.product_details.data

import com.jamdoughnut.test.catalog.data.dto.ProductDto
import com.jamdoughnut.test.catalog.data.dto.ProductCatalog
import com.jamdoughnut.test.product_details.domain.ProductDetailsRepository
import com.jamdoughnut.test.utils.mockResponse
import kotlinx.serialization.json.Json

class MockProductDetailsRepository : ProductDetailsRepository {

    override fun getProductDetailsById(id: Int): ProductDto? {
        val jsonString: String = mockResponse
        return Json.decodeFromString<ProductCatalog>(jsonString).products.find { it.id == id }
    }
}