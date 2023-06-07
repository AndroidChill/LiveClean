package com.example.testappshtr

sealed class Result<T> {

    class Success <T>(val data:  T) : Result<T>()

    class Empty<T>() : Result<T>()

    class Error<T>(val exc: Throwable) : Result<T>()

    class Loading<T> : Result<T>()

}