package com.jamdoughnut.test

import com.jamdoughnut.test.catalog.data.dto.ProductDto
import com.jamdoughnut.test.product_details.domain.ProductDetailsInteractor
import com.jamdoughnut.test.product_details.domain.ProductDetailsRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever


class ProductDetailsInteractorTest {

    @Mock
    private lateinit var repository: ProductDetailsRepository

    private lateinit var interactor: ProductDetailsInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        interactor = ProductDetailsInteractor(repository)
    }

    @Test
    fun `getProductById should return product`() {
        // Arrange
        val productId = 1
        val expectedProduct = ProductDto(
            id = productId,
            categoryId = 2,
            name = "Test Product",
            price = 1000,
            cashbackPercentage = 5,
            leadImageUrls = listOf("url1", "url2")
        )

        whenever(repository.getProductDetailsById(productId)).thenReturn(expectedProduct)

        // Act
        val result = interactor.getProductById(productId)

        // Assert
        assertEquals(expectedProduct, result)
    }
}