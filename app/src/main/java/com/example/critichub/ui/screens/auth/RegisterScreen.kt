package com.example.critichub.ui.screens.auth

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.critichub.ui.components.CriticHubTopBar
import com.example.critichub.ui.components.FormTextField
import com.example.critichub.ui.components.PasswordField
import com.example.critichub.util.Validation
import com.example.critichub.util.persianMessage
import com.example.critichub.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onGoLogin: () -> Unit,
    onRegistered: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var fullName by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.registered) {
        if (uiState.registered) onRegistered()
    }

    val formValid = remember(fullName, username, email, password, confirmPassword) {
        fullName.isNotBlank() &&
            username.trim().length >= 3 &&
            Validation.isValidEmail(email) &&
            Validation.isValidPassword(password) &&
            password == confirmPassword
    }

    fun submit() {
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
        passwordError = when {
            password.isEmpty() -> "رمز عبور را وارد کنید"
            password.length < 6 -> "رمز عبور باید حداقل ۶ کاراکتر باشد"
            else -> null
        }
        confirmError = when {
            confirmPassword.isEmpty() -> "تکرار رمز عبور را وارد کنید"
            confirmPassword != password -> "تکرار رمز عبور یکسان نیست"
            else -> null
        }
        if (nameError == null && usernameError == null && emailError == null &&
            passwordError == null && confirmError == null
        ) {
            viewModel.register(fullName, username, email, password)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = { CriticHubTopBar(title = "ثبت‌نام", onBack = onGoLogin) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ساخت حساب کاربری",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            Text(
                text = "اطلاعات زیر برای ساخت حساب شما در این دستگاه ذخیره می‌شود",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(28.dp))

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
                keyboardType = KeyboardType.Text,
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
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(14.dp))
            PasswordField(
                value = password,
                onValueChange = { password = it; passwordError = null; if (confirmPassword.isNotEmpty()) confirmError = null },
                label = "رمز عبور",
                isError = passwordError != null,
                errorText = passwordError,
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(14.dp))
            PasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmError = null },
                label = "تکرار رمز عبور",
                isError = confirmError != null,
                errorText = confirmError,
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
                onClick = { submit() },
                enabled = formValid && !uiState.loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("ثبت‌نام", style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(Modifier.height(20.dp))
            TextButton(onClick = onGoLogin) {
                Text("قبلاً ثبت‌نام کرده‌اید؟ ورود", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
