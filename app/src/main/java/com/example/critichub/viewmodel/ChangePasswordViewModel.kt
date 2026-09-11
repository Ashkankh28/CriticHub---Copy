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

data class ChangePasswordUiState(
    val checkingEmail: Boolean = false,
    val accountFound: Boolean = false,
    val notFound: Boolean = false,
    val saving: Boolean = false,
    val success: Boolean = false,
    val error: AuthError? = null
)

class ChangePasswordViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState

    fun reset() {
        _uiState.value = ChangePasswordUiState()
    }

    /** مرحلهٔ اول: بررسی وجود حساب با ایمیل. */
    fun checkEmail(email: String) {
        if (_uiState.value.checkingEmail) return
        _uiState.value = _uiState.value.copy(checkingEmail = true, notFound = false, error = null)
        viewModelScope.launch {
            val found = try {
                authRepository.findAccountByEmail(email.trim())
            } catch (_: Exception) {
                false
            }
            _uiState.value = if (found) {
                _uiState.value.copy(checkingEmail = false, accountFound = true)
            } else {
                _uiState.value.copy(
                    checkingEmail = false,
                    notFound = true,
                    error = AuthError.ACCOUNT_NOT_FOUND
                )
            }
        }
    }

    /** مرحلهٔ دوم: ذخیرهٔ رمز عبور جدید. */
    fun submitNewPassword(email: String, newPassword: String) {
        if (_uiState.value.saving) return
        _uiState.value = _uiState.value.copy(saving = true, error = null)
        viewModelScope.launch {
            val result = authRepository.changePassword(email.trim(), newPassword)
            _uiState.value = when (result) {
                is AuthResult.Success -> _uiState.value.copy(saving = false, success = true)
                is AuthResult.Failure -> _uiState.value.copy(saving = false, error = result.error)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                ChangePasswordViewModel(app.authRepository)
            }
        }
    }
}
