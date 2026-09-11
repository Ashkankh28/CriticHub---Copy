package com.example.critichub.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.critichub.ui.components.CriticHubTopBar
import com.example.critichub.ui.components.FormTextField
import com.example.critichub.util.Validation
import com.example.critichub.util.persianMessage
import com.example.critichub.viewmodel.EditProfileViewModel

/** ویرایش پروفایل (نام، نام کاربری و ایمیل). */
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val viewModel: EditProfileViewModel = viewModel(factory = EditProfileViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user by viewModel.currentUser.collectAsStateWithLifecycle()

    var fullName by rememberSaveable { mutableStateOf(user?.fullName ?: "") }
    var username by rememberSaveable { mutableStateOf(user?.username ?: "") }
    var email by rememberSaveable { mutableStateOf(user?.email ?: "") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(user?.id) {
        if (user != null && fullName.isBlank() && username.isBlank() && email.isBlank()) {
            fullName = user!!.fullName
            username = user!!.username
            email = user!!.email
        }
    }

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) {
            viewModel.clearSavedFlag()
            onSaved()
        }
    }

    val formValid = remember(fullName, username, email) {
        fullName.isNotBlank() &&
            Validation.isValidUsername(username) &&
            Validation.isValidEmail(email)
    }

    fun save() {
        nameError = if (fullName.isBlank()) "نام و نام خانوادگی را وارد کنید" else null
        usernameError = when {
            username.isBlank() -> "نام کاربری را وارد کنید"
            username.trim().length < 3 -> "نام کاربری باید حداقل ۳ کاراکتر باشد"
            else -> null
        }
        emailError = when {
            email.isBlank() -> "ایمیل را وارد کنید"
            !Validation.isValidEmail(email) -> "ایمیل معتبر وارد کنید"
            else -> null
        }
        val userId = user?.id
        if (userId != null && nameError == null && usernameError == null && emailError == null) {
            viewModel.save(userId, fullName, username, email)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = { CriticHubTopBar(title = "ویرایش پروفایل", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "اطلاعات حساب خود را ویرایش کنید",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp, bottom = 20.dp)
            )

            FormTextField(
                value = fullName,
                onValueChange = { fullName = it; nameError = null },
                label = "نام و نام خانوادگی",
                isError = nameError != null,
                errorText = nameError,
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(14.dp))
            FormTextField(
                value = username,
                onValueChange = { username = it; usernameError = null },
                label = "نام کاربری",
                isError = usernameError != null,
                errorText = usernameError,
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(14.dp))
            FormTextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = "ایمیل",
                isError = emailError != null,
                errorText = emailError,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )

            if (uiState.error != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = uiState.error!!.persianMessage(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(26.dp))
            Button(
                onClick = { save() },
                enabled = formValid && !uiState.saving && user != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.saving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("ذخیره تغییرات")
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
