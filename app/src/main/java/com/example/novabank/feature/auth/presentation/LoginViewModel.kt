package com.example.novabank.feature.auth.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onDigit(digit: Int) {
        val current = _uiState.value
        if (current.pin.length >= PIN_LENGTH || current.isAuthenticated) return

        val newPin = current.pin + digit
        _uiState.update { it.copy(pin = newPin, isError = false) }
        if (newPin.length == PIN_LENGTH) verify(newPin)
    }

    fun onDelete() {
        _uiState.update { it.copy(pin = it.pin.dropLast(1), isError = false) }
    }

    private fun verify(pin: String) {
        if (pin == DEMO_PIN) {
            _uiState.update { it.copy(isAuthenticated = true) }
        } else {
            _uiState.update { it.copy(pin = "", isError = true) }
        }
    }

    private companion object {
        const val DEMO_PIN = "1234" // yalnız demo üçün
    }
}