package com.example.testappshtr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappshtr.Result
import com.example.testappshtr.data.NetworkDataSource
import com.example.testappshtr.data.ProductsRepositoryImpl
import com.example.testappshtr.domain.GetProductsUseCase
import com.example.testappshtr.domain.ProductUi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


/*
* оптравка запроса на получение всех продуктов
* хранение состояния которое говорит о статусе запрос - загрузка успех ошибка
*                       + информация о продуктах
* */
class MainViewModel : ViewModel() {

    val productState: MutableSharedFlow<Result<List<ProductUi>>> = MutableSharedFlow()

    val useCase = GetProductsUseCase(ProductsRepositoryImpl(NetworkDataSource()))

    val coroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        viewModelScope.launch {
            productState.emit(Result.Error<List<ProductUi>>(throwable))
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            useCase.invoke()
                .onStart {
                    productState.emit(Result.Loading<List<ProductUi>>())
                }
                .collect { data ->
                    if (data.isNotEmpty()) {
                        productState.emit(Result.Success<List<ProductUi>>(data))
                    } else {
                        productState.emit(Result.Empty<List<ProductUi>>())
                    }
                }
        }

    }

//    suspend fun getProducts() {
//        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
//            useCase.invoke()
//                .onStart {
//                    productState.emit(Result.Loading<List<ProductUi>>())
//                }
//                .collect { data ->
//                if (data.isNotEmpty()) {
//                    productState.emit(Result.Success<List<ProductUi>>(data))
//                } else {
//                    productState.emit(Result.Empty<List<ProductUi>>())
//                }
//            }
//        }
//    }

}

