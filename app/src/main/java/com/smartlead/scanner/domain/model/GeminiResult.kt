package com.smartlead.scanner.domain.model

sealed class GeminiResult<out T> {
    data class Success<T>(val data: T) : GeminiResult<T>()
    data class Error(val message: String) : GeminiResult<Nothing>()
}
