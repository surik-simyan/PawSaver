package com.pawsaver.app.feature.login.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pawsaver.app.core.ui.components.MinimalDialog
import com.pawsaver.app.core.utils.inputFieldPaddings
import com.pawsaver.app.core.utils.koinViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResetPassword: (encodedPk: String) -> Unit,
) {
    val viewModel = koinViewModel<ForgotPasswordScreenViewModel>()
    val forgotPasswordState by viewModel.forgotPasswordState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val email = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Forgot password",
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
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
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.forgotPassword(email.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Get reset code")
            }
        }

        when (forgotPasswordState) {
            ForgotPasswordScreenViewModel.ForgotPasswordScreenState.Idle -> Unit
            ForgotPasswordScreenViewModel.ForgotPasswordScreenState.Loading -> MinimalDialog(
                "Loading",
                true
            )

            is ForgotPasswordScreenViewModel.ForgotPasswordScreenState.Error -> {
                val error =
                    (forgotPasswordState as ForgotPasswordScreenViewModel.ForgotPasswordScreenState.Error).error
                scope.launch {
                    val result = snackbarHostState.showSnackbar(error.apiErrors.first().message)
                    if (result == SnackbarResult.Dismissed) {
                        viewModel.resetState()
                    }
                }
            }

            is ForgotPasswordScreenViewModel.ForgotPasswordScreenState.Success -> {
                val encodedPk =
                    (forgotPasswordState as ForgotPasswordScreenViewModel.ForgotPasswordScreenState.Success).response.identifier
                onNavigateToResetPassword(encodedPk)
                viewModel.resetState()
            }
        }
    }
}