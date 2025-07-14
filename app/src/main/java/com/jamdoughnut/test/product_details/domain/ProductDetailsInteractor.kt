package com.jamdoughnut.test.product_details.domain

import com.jamdoughnut.test.catalog.data.dto.ProductDto
import javax.inject.Inject

class ProductDetailsInteractor @Inject constructor(val repository: ProductDetailsRepository) {

    fun getProductById(id: Int): ProductDto? {
        return repository.getProductDetailsById(id)
    }
}