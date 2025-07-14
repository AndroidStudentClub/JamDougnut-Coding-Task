package com.jamdoughnut.test.product_details.domain

import com.jamdoughnut.test.catalog.data.dto.ProductDto

interface ProductDetailsRepository {

    fun getProductDetailsById(id: Int): ProductDto?
}