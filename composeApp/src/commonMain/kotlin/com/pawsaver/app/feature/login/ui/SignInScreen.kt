package com.pawsaver.app.feature.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.pawsaver.app.core.utils.firstOrBlank
import com.pawsaver.app.core.utils.inputFieldPaddings
import com.pawsaver.app.core.utils.isNotNullOrEmpty
import com.pawsaver.app.core.utils.koinViewModel
import com.pawsaver.app.core.utils.toErrorText
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHomeScreen: () -> Unit,
) {
    val viewModel = koinViewModel<SignInScreenViewModel>()
    val signInState by viewModel.signInState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val rememberMe = remember { mutableStateOf(false) }

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
                value = password.value,
                onValueChange = { password.value = it },
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

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe.value,
                        onCheckedChange = { rememberMe.value = it }
                    )
                    Text("Remember me?")
                }

                TextButton(onClick = onNavigateToForgotPassword) {
                    Text("Forgot password?")
                }
            }

            Button(
                onClick = { viewModel.signIn(email.value, password.value) },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("SIGN IN")
            }

            Spacer(modifier = Modifier.weight(1.0f))

            Button(
                onClick = onNavigateToSignUp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text("SIGN UP")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text("SIGN UP AS SHELTER")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToHomeScreen,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text("CONTINUE AS GUEST")
            }
        }

        when (signInState) {
            SignInScreenViewModel.SignInScreenState.Idle -> Unit
            SignInScreenViewModel.SignInScreenState.Loading -> MinimalDialog("Loading", true)
            is SignInScreenViewModel.SignInScreenState.Error -> {
                val error = (signInState as SignInScreenViewModel.SignInScreenState.Error).error
                if (error.email.isNotNullOrEmpty() || error.password.isNotNullOrEmpty()) {
                    emailError.value = error.email.firstOrBlank()
                    passwordError.value = error.password.firstOrBlank()
                } else {
                    emailError.value = ""
                    passwordError.value = ""
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(error.value)
                        if (result == SnackbarResult.Dismissed) {
                            viewModel.resetState()
                        }
                    }
                }
            }

            is SignInScreenViewModel.SignInScreenState.Success -> {
                viewModel.resetState()
                onNavigateToHomeScreen()
            }
        }
    }
}