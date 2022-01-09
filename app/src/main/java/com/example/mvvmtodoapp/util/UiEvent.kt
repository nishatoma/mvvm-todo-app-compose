package com.example.mvvmtodoapp.util

sealed class UiEvent {
    object PopBackStack: UiEvent()

    // which will be an event we send from
    // the view model to the UI when we want to navigate to
    // the new screen
    data class Navigate(val route: String): UiEvent()

    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
}
