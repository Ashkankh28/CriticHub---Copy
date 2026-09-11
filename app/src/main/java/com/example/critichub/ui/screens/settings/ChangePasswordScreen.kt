package com.example.critichub.ui.screens.settings

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.critichub.viewmodel.ChangePasswordViewModel

/**
 * بازیابی/تغییر رمز عبور — جریان نمونهٔ محلی:
 * ایمیل ← یافتن حساب ← رمز جدید و تکرار آن ← ذخیره.
 *
 * ارسال ایمیل تأیید واقعی در نسخهٔ نهایی توسط API پشتیبان انجام می‌شود و این
 * صفحه صرفاً مسیر محلی را شبیه‌سازی می‌کند.
 */
@Composable
fun ChangePasswordScreen(onBack: () -> Unit) {
    val viewModel: ChangePasswordViewModel = viewModel(factory = ChangePasswordViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var email by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = { CriticHubTopBar(title = "تغییر رمز عبور", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp)
        ) {
            when {
                uiState.success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(Modifier.height(18.dp))
                        Text(
                            text = "رمز عبور با موفقیت تغییر کرد",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "از این پس با رمز عبور جدید می‌توانید وارد شوید.\n" +
                                "در نسخهٔ واقعی، تأیید ایمیل و اطلاع‌رسانی توسط API انجام می‌شود.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = onBack) {
                            Text("بازگشت")
                        }
                    }
                }

                !uiState.accountFound -> {
                    // مرحلهٔ ۱: جستجوی حساب با ایمیل
                    Text(
                        text = "برای بازیابی رمز عبور، ایمیل حساب خود را وارد کنید. " +
                            "در نسخهٔ نهایی، تأیید هویت از طریق ایمیل و توسط API پشتیبان انجام خواهد شد.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 16.dp, bottom = 20.dp)
                    )
                    FormTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                            if (!uiState.checkingEmail) viewModel.reset()
                        },
                        label = "ایمیل",
                        isError = emailError != null || uiState.notFound,
                        errorText = emailError
                            ?: if (uiState.notFound) uiState.error?.persianMessage() else null,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = {
                            emailError = when {
                                email.isBlank() -> "ایمیل را وارد کنید"
                                !Validation.isValidEmail(email) -> "ایمیل معتبر وارد کنید"
                                else -> null
                            }
                            if (emailError == null) {
                                viewModel.checkEmail(email)
                            }
                        },
                        enabled = !uiState.checkingEmail,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        if (uiState.checkingEmail) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("ادامه")
                        }
                    }
                }

                else -> {
                    // مرحلهٔ ۲: ثبت رمز جدید
                    Text(
                        text = "حساب یافت شد؛ رمز عبور جدید را وارد کنید.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                    )
                    Text(
                        text = "ایمیل: ${email.trim()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(16.dp))
                    PasswordField(
                        value = newPassword,
                        onValueChange = { newPassword = it; passwordError = null },
                        label = "رمز عبور جدید",
                        isError = passwordError != null,
                        errorText = passwordError,
                        imeAction = ImeAction.Next
                    )
                    Spacer(Modifier.height(14.dp))
                    PasswordField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; confirmError = null },
                        label = "تکرار رمز عبور جدید",
                        isError = confirmError != null,
                        errorText = confirmError,
                        imeAction = ImeAction.Done
                    )

                    if (uiState.error != null) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = uiState.error!!.persianMessage(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(Modifier.height(22.dp))
                    Button(
                        onClick = {
                            passwordError = when {
                                newPassword.isEmpty() -> "رمز عبور را وارد کنید"
                                newPassword.length < 6 -> "رمز عبور باید حداقل ۶ کاراکتر باشد"
                                else -> null
                            }
                            confirmError = when {
                                confirmPassword.isEmpty() -> "تکرار رمز عبور را وارد کنید"
                                confirmPassword != newPassword -> "تکرار رمز عبور یکسان نیست"
                                else -> null
                            }
                            if (passwordError == null && confirmError == null) {
                                viewModel.submitNewPassword(email, newPassword)
                            }
                        },
                        enabled = !uiState.saving,
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
                            Text("ثبت رمز جدید")
                        }
                    }
                }
            }
            Spacer(Modifier.height(28.dp))
        }
    }
}
