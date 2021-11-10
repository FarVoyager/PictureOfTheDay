package com.example.pictureoftheday.ui.main.viewmodel

import com.example.pictureoftheday.ui.main.model.PODServerResponseData

sealed class AppState {
    data class Success(val serverResponseData: PODServerResponseData) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}