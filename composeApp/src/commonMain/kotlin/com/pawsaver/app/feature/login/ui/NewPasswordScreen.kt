package com.pawsaver.app.feature.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.pawsaver.app.core.ui.components.MinimalDialog
import com.pawsaver.app.core.utils.checkFieldErrors
import com.pawsaver.app.core.utils.inputFieldPaddings
import com.pawsaver.app.core.utils.koinViewModel
import com.pawsaver.app.core.utils.toErrorText
import kotlinx.coroutines.launch

@Composable
fun NewPasswordScreen(
    encodedPk: String,
    resetToken: String,
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<NewPasswordScreenViewModel>()
    val newPasswordState by viewModel.newPasswordState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val verifyPassword = remember { mutableStateOf("") }
    val verifyPasswordError = remember { mutableStateOf("") }
    var showVerifyPassword by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text("Icon")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Placeholder for blurred text")
            Text("Additional placeholder for more blurred text")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                    if (verifyPasswordError.value == "Passwords do not match") {
                        verifyPasswordError.value = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().inputFieldPaddings(),
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle password visibility",
                        modifier = Modifier
                            .clickable { showPassword = !showPassword }
                    )
                },
                isError = passwordError.value.isNotBlank(),
                supportingText = { passwordError.value.toErrorText() },
            )

            OutlinedTextField(
                value = verifyPassword.value,
                onValueChange = {
                    verifyPassword.value = it
                    verifyPasswordError.value = if (password.value != it) {
                        "Passwords do not match"
                    } else {
                        ""
                    }
                },
                modifier = Modifier.fillMaxWidth().inputFieldPaddings(),
                label = { Text("Verify Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (showVerifyPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (showVerifyPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle password visibility",
                        modifier = Modifier
                            .clickable { showVerifyPassword = !showVerifyPassword }
                    )
                },
                isError = verifyPasswordError.value.isNotBlank(),
                supportingText = { verifyPasswordError.value.toErrorText() },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (verifyPasswordError.value.isBlank()) {
                        viewModel.setNewPassword(
                            password.value,
                            encodedPk,
                            resetToken
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text("SET NEW PASSWORD")
            }
        }

        when (newPasswordState) {

            NewPasswordScreenViewModel.NewPasswordScreenState.Idle -> Unit
            NewPasswordScreenViewModel.NewPasswordScreenState.Loading -> MinimalDialog(
                "Loading",
                true
            )

            is NewPasswordScreenViewModel.NewPasswordScreenState.Error -> {
                val error = (newPasswordState as NewPasswordScreenViewModel.NewPasswordScreenState.Error).error

                passwordError.value = ""

                error.checkFieldErrors(
                    mapOf(
                        "new_password" to passwordError,
                    )
                ) { message ->
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(message)
                        if (result == SnackbarResult.Dismissed) {
                            viewModel.resetState()
                        }
                    }
                }
            }

            is NewPasswordScreenViewModel.NewPasswordScreenState.Success -> {
                viewModel.resetState()
                onNavigateBack()
            }
        }
    }
}