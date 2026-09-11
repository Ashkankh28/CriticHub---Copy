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

data class EditProfileUiState(
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: AuthError? = null
)

class EditProfileViewModel(private val authRepository: AuthRepository) : ViewModel() {

    /** کاربرِ واردشده برای پیش‌پر کردن فرم. */
    val currentUser: StateFlow<com.example.critichub.model.User?> = authRepository.currentUser

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    fun save(userId: String, fullName: String, username: String, email: String) {
        if (_uiState.value.saving) return
        _uiState.value = EditProfileUiState(saving = true)
        viewModelScope.launch {
            val result = authRepository.updateProfile(userId, fullName, username, email)
            _uiState.value = when (result) {
                is AuthResult.Success -> EditProfileUiState(saved = true)
                is AuthResult.Failure -> EditProfileUiState(error = result.error)
            }
        }
    }

    fun clearSavedFlag() {
        _uiState.value = EditProfileUiState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                EditProfileViewModel(app.authRepository)
            }
        }
    }
}
