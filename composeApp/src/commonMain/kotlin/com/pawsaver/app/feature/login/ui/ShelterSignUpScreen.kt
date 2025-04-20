package com.pawsaver.app.feature.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelterSignUpScreen(
    onNavigateBack: () -> Unit,
    onNavigateVerifyEmail: (email: String) -> Unit,
) {
    val viewModel = koinViewModel<ShelterSignUpScreenViewModel>()
    val signUpState by viewModel.shelterSignUpState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val name = remember { mutableStateOf("") }
    val nameError = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val registrationNumber = remember { mutableStateOf("") }
    val registrationNumberError = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val phoneError = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val verifyPassword = remember { mutableStateOf("") }
    val verifyPasswordError = remember { mutableStateOf("") }
    var showVerifyPassword by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Sign Up",
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
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
            Text("Placeholder for blurred text")
            Text("Additional placeholder for more blurred text")

            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                modifier = Modifier.fillMaxWidth().inputFieldPaddings(),
                label = { Text("Shelter name") },
                leadingIcon = { Icon(Icons.Default.Store, contentDescription = "Shelter name") },
                singleLine = true,
                isError = nameError.value.isNotBlank(),
                supportingText = { nameError.value.toErrorText() },
            )

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                modifier = Modifier.fillMaxWidth().inputFieldPaddings(),
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                singleLine = true,
                isError = emailError.value.isNotBlank(),
                supportingText = { emailError.value.toErrorText() },
            )

            OutlinedTextField(
                value = registrationNumber.value,
                onValueChange = { registrationNumber.value = it },
                modifier = Modifier.fillMaxWidth().inputFieldPaddings(),
                label = { Text("Registration number") },
                leadingIcon = { Icon(Icons.Default.Pin, contentDescription = "Registration number") },
                singleLine = true,
                isError = registrationNumberError.value.isNotBlank(),
                supportingText = { registrationNumberError.value.toErrorText() },
            )

            OutlinedTextField(
                value = "+${phone.value}",
                onValueChange = { newInput ->
                    // Keep only digits and update the state
                    phone.value = newInput.filter { it.isDigit() }
                },
                modifier = Modifier.fillMaxWidth().inputFieldPaddings(),
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                singleLine = true,
                isError = phoneError.value.isNotBlank(),
                supportingText = { phoneError.value.toErrorText() },
            )

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

            Button(
                onClick = {
                    if (verifyPasswordError.value.isBlank()) {
                        viewModel.shelterSignUp(
                            email.value,
                            name.value,
                            registrationNumber.value,
                            phone.value,
                            password.value
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("SIGN UP")
            }
        }

        when (signUpState) {
            ShelterSignUpScreenViewModel.ShelterSignUpScreenState.Idle -> Unit
            ShelterSignUpScreenViewModel.ShelterSignUpScreenState.Loading -> MinimalDialog(
                "Loading",
                true
            )

            is ShelterSignUpScreenViewModel.ShelterSignUpScreenState.Error -> {
                val error = (signUpState as ShelterSignUpScreenViewModel.ShelterSignUpScreenState.Error).error

                nameError.value = ""
                emailError.value = ""
                registrationNumberError.value = ""
                phoneError.value = ""
                passwordError.value = ""

                error.checkFieldErrors(
                    mapOf(
                        "name" to nameError,
                        "email" to emailError,
                        "registration_number" to registrationNumberError,
                        "phone" to phoneError,
                        "password" to passwordError
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

            is ShelterSignUpScreenViewModel.ShelterSignUpScreenState.Success -> {
                val message = (signUpState as ShelterSignUpScreenViewModel.ShelterSignUpScreenState.Success).user.message
                scope.launch {
                    val result = snackbarHostState.showSnackbar(message)
                    if (result == SnackbarResult.Dismissed) {
                        viewModel.resetState()
                    }
                }
                onNavigateVerifyEmail(email.value)
                viewModel.resetState()
            }
        }
    }
}