package com.jamdoughnut.test.catalog.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    @SerialName("category_id")
    val categoryId: Int,
    val name: String,
    val price: Int,
    @SerialName("cashback_percentage")
    val cashbackPercentage: Int? = 0,
    @SerialName("lead_image_urls")
    val leadImageUrls: List<String>
)