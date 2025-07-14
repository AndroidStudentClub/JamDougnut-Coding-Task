package com.jamdoughnut.test.catalog

import androidx.lifecycle.viewModelScope
import com.jamdoughnut.test.base.BaseViewModel
import com.jamdoughnut.test.catalog.data.dto.Category
import com.jamdoughnut.test.catalog.data.dto.ProductCatalog
import com.jamdoughnut.test.catalog.data.dto.ProductDto
import com.jamdoughnut.test.catalog.domain.CatalogInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCatalogViewModel @Inject constructor(
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel<CatalogState>(CatalogState()) {

    private val allProducts = mutableListOf<ProductDto>()

    private val _tags: MutableStateFlow<MutableList<Category>> =
        MutableStateFlow(mutableListOf<Category>())
    val tags: StateFlow<List<Category>> get() = _tags


    init {
        fetchProducts()
    }

    private fun fetchProducts() {

        // Imagine that this is kind of Network Request
        viewModelScope.launch {
            try {
                val catalogResult = catalogInteractor.getCatalog()
                allProducts.apply {
                    clear()
                    addAll(catalogResult.products)
                }
                setState { copy(isRefreshing = false, products = catalogResult) }
            } catch (e: Exception) {
                setState {
                    copy(
                        isError = true, isRefreshing = false, products = ProductCatalog(
                            emptyList(), emptyList()
                        )
                    )
                }
            }
        }
    }

    private fun onFilter(selectedCategory: Category?) {
        if (selectedCategory == null) {
            setState {
                copy(
                    isRefreshing = false, products = ProductCatalog(
                        categories = state.value.products.categories,
                        products = allProducts
                    )
                )
            }
        } else {
            val filtered = allProducts.filter { it.categoryId == selectedCategory.categoryId }
            setState {
                copy(
                    isRefreshing = false, products = ProductCatalog(
                        categories = state.value.products.categories,
                        products = filtered
                    )
                )
            }
        }
    }

    fun onCategorySelected(selectedCategory: Category) {
        // Show all items, filters are turned off
        if (_tags.value.contains(selectedCategory)) {
            _tags.value.remove(selectedCategory)
            onFilter(null)
        } else {
            _tags.value.apply {
                onFilter(selectedCategory)
                clear()
                add(selectedCategory)
            }

        }

    }
}

data class CatalogState(
    val isError: Boolean = false,
    val isRefreshing: Boolean = true,
    val products: ProductCatalog = ProductCatalog(emptyList(), emptyList())
)
