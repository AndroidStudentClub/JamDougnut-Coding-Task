package com.jamdoughnut.test.di

import com.jamdoughnut.test.catalog.data.repository.MockCatalogRepository
import com.jamdoughnut.test.catalog.domain.CatalogInteractor
import com.jamdoughnut.test.catalog.domain.CatalogRepository
import com.jamdoughnut.test.product_details.data.MockProductDetailsRepository
import com.jamdoughnut.test.product_details.domain.ProductDetailsInteractor
import com.jamdoughnut.test.product_details.domain.ProductDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCatalogRepository(): CatalogRepository {
        return MockCatalogRepository()
    }

    @Provides
    @Singleton
    fun provideDetailsRepository(): ProductDetailsRepository {
        return MockProductDetailsRepository()
    }

    @Provides
    @Singleton
    fun provideCatalogInteractor(repository: CatalogRepository): CatalogInteractor {
        return CatalogInteractor(repository)
    }

    @Provides
    @Singleton
    fun provideDetailsInteractor(repository: ProductDetailsRepository): ProductDetailsInteractor {
        return ProductDetailsInteractor(repository)
    }
}