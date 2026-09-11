package com.example.critichub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.critichub.data.repository.AuthError
import com.example.critichub.data.repository.AuthRepository
import com.example.critichub.data.repository.AuthResult
import com.example.critichub.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val loading: Boolean = false,
    val error: AuthError? = null,
    val user: User? = null
)

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        if (_uiState.value.loading) return
        _uiState.value = LoginUiState(loading = true)
        viewModelScope.launch {
            val result = authRepository.login(username.trim(), password)
            _uiState.value = when (result) {
                is AuthResult.Success -> LoginUiState(user = result.data)
                is AuthResult.Failure -> LoginUiState(error = result.error)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                LoginViewModel(app.authRepository)
            }
        }
    }
}
