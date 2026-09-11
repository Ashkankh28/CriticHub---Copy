package com.example.critichub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.critichub.data.repository.AuthError
import com.example.critichub.data.repository.AuthRepository
import com.example.critichub.data.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val loading: Boolean = false,
    val error: AuthError? = null,
    val registered: Boolean = false
)

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(fullName: String, username: String, email: String, password: String) {
        if (_uiState.value.loading) return
        _uiState.value = RegisterUiState(loading = true)
        viewModelScope.launch {
            val result = authRepository.register(fullName, username, email, password)
            _uiState.value = when (result) {
                is AuthResult.Success -> RegisterUiState(registered = true)
                is AuthResult.Failure -> RegisterUiState(error = result.error)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                RegisterViewModel(app.authRepository)
            }
        }
    }
}
