package com.jamdoughnut.test.catalog.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Category(
    @SerialName("category_id")
    val categoryId: Int,
    val label: String
)

