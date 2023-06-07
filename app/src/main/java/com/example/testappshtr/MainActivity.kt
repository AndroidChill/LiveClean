package com.example.testappshtr

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.testappshtr.domain.ProductUi
import com.example.testappshtr.ui.theme.TestAppShtrTheme
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/*
* 1. Необходимо получить список продуктов (ниже пример получения списка категорий)
* 2. Отсоритровать его по возрастанию поля newPrice
* 3. Отобразить список "Name NewPrice"
*
* Доп задания
* 1. Вывеести значение "Name Цена:NewPrice"
* 2. Написать тест, если список пустой, ошибка загрузки продуктов
* 3. Если список пустой, выбрасывать ошибку загрузки продуктов
*
* Request
* {{baseUrl}}/menu_product?companyUuid={{companyUuid}}
*
* base url https://food-delivery-api-bunbeauty.herokuapp.com/
* companyUuid 7416dba5-2825-4fe3-abfb-1494a5e2bf99
*
* Json
*  {
    "count": 82,
    "results": [
        {
            "uuid": "486af622-f0f9-4d16-ae8b-b052fedfd61a",
            "name": "Люля-кебаб из свинины",
            "newPrice": 200
        }
      ]
    }
* Response
* "uuid": "486af622-f0f9-4d16-ae8b-b052fedfd61a",
* "name": "Люля-кебаб из свинины",
* "newPrice": 200,
*
*
* */

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAppShtrTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val data = viewModel.productState.collectAsState(initial = Result.Loading<List<ProductUi>>()).value

                    val temp = data as Result<List<ProductUi>>
                    when(temp) {
                        is Result.Loading -> {}
                        is Result.Error -> {}
                        is Result.Empty -> {
                            ContentName(name = "empty list error")
                        }
                        is Result.Success -> {
                            Content(data = temp.data.map { "${it.name}: Цена-${it.price}" } )
                        }
                    }
                }
            }
        }
    }
}


@Serializable
data class ListServer<T>(

    @SerialName("count")
    val count: Int,

    @SerialName("results")
    val results: List<T>
)


@Serializable
data class MenuProductServer(

    @SerialName("uuid")
    val uuid: String,

    @SerialName("name")
    val name: String,

    @SerialName("newPrice")
    val newPrice: Int
)

@Serializable
class CategoryServer(

    @SerialName("uuid")
    val uuid: String,

    @SerialName("name")
    val name: String,

    @SerialName("priority")
    val priority: Int,
)

@Composable
fun Content(data: List< String>) {
    Column {
        data.forEach { 
            ContentName(name = (it))
        }
    }
}


@Composable
fun ContentName(name:String) {
    Text(text = name)
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TestAppShtrTheme {
        Greeting("Android")
    }
}