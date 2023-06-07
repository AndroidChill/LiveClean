package com.example.testappshtr.domain

import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getProducts() : Flow<List<ProductUi>>

}