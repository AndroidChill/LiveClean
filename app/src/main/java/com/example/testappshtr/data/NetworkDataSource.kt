package com.example.testappshtr.data

import com.example.testappshtr.CategoryServer
import com.example.testappshtr.ListServer
import com.example.testappshtr.MenuProductServer
import com.example.testappshtr.di.NetworkModule
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkDataSource {

    suspend fun getProducts(): Flow<List<MenuProductServer>> {
        return flow<List<MenuProductServer>> {
            val response = NetworkModule.serviceProduct()
                .get("https://food-delivery-api-bunbeauty.herokuapp.com/menu_product") {
                    parameter("companyUuid", "7416dba5-2825-4fe3-abfb-1494a5e2bf99")
                }.body<ListServer<MenuProductServer>>().results
            emit(response)
        }

    }

}