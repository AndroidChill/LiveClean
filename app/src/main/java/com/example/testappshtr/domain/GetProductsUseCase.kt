package com.example.testappshtr.domain

import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    val productsRepository: ProductsRepository
) {

    suspend fun invoke(): Flow<List<ProductUi>> {
        return productsRepository.getProducts()
    }

}