package com.example.testappshtr.data

import com.example.testappshtr.domain.ProductUi
import com.example.testappshtr.domain.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

class ProductsRepositoryImpl(
    val networkDataSource: NetworkDataSource
) : ProductsRepository {
    override suspend fun getProducts(): Flow<List<ProductUi>> {
        return networkDataSource.getProducts()
            .map { itemFlow ->
                itemFlow.map { itemData ->
                    ProductUi(
                        name = itemData.name,
                        price = itemData.newPrice
                    )
                }
                    .sortedBy { it.price }
            }

    }
}