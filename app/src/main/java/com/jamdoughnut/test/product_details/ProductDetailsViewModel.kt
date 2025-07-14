package com.jamdoughnut.test.product_details

import com.jamdoughnut.test.base.BaseViewModel
import com.jamdoughnut.test.catalog.data.dto.ProductDto
import com.jamdoughnut.test.product_details.domain.ProductDetailsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val interactor: ProductDetailsInteractor
) : BaseViewModel<DetailsState>(DetailsState()) {

    private val _quantity: MutableStateFlow<Int> =MutableStateFlow(1)
    val quantity: StateFlow<Int> get() = _quantity

    fun setQuantity(newQuantity: Int) {
        _quantity.value = newQuantity
    }

    private val _price: MutableStateFlow<Int> =MutableStateFlow(1)
    val price: StateFlow<Int> get() = _price

    fun updatePrice(pricePerItem: Int) {
        _price.value = pricePerItem * _quantity.value
    }


    fun getProductDetailsById(productId: Int) {

        setState { copy(isRefreshing = true) }

        try {
            val result = interactor.getProductById(productId)
            result?.let {
                setState {
                    copy(
                        isRefreshing = false,
                        result = result
                    )
                }

                updatePrice(it.price)
            }

        } catch (e: Exception) {

            setState {
                copy(
                    isError = true,
                    isRefreshing = false
                )

            }
        }
    }
}

data class DetailsState(
    val isError: Boolean = false,
    val isRefreshing: Boolean = true,
    val result: ProductDto? = null
)


