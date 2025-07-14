package com.jamdoughnut.test.catalog.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductCatalog(
    val categories: List<Category>,
    val products: List<ProductDto>
)