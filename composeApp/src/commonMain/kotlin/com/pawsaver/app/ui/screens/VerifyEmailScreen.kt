package com.pawsaver.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(navController: NavHostController) {
    val verificationCode = remember { mutableStateOf("") }
    val focusRequesters = remember { List(6) { FocusRequester() } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Verify Email",
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
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
            Spacer(modifier = Modifier.height(64.dp))

            // Profile icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("Icon")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Placeholder for text that appears to be blurred in the image
            Text("Placeholder for blurred text")
            Text("Additional placeholder for more blurred text")

            Spacer(modifier = Modifier.height(80.dp))

            // 6-digit verification code input
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    val boxSize = (maxWidth - 32.dp) / 6 // account for padding and 6 fields
                    val focusManager = LocalFocusManager.current

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0 until 6) {
                            val char = if (verificationCode.value.length > i) {
                                verificationCode.value[i].toString()
                            } else ""

                            val isFocused = remember { mutableStateOf(false) }

                            BasicTextField(
                                value = char,
                                onValueChange = { newValue ->
                                    if (newValue.length == 6) {
                                        verificationCode.value = newValue.take(6)
                                    } else if (newValue.length <= 1) {
                                        val newCode = verificationCode.value.toMutableList()
                                        if (newValue.isEmpty()) {
                                            if (i < newCode.size) newCode.removeAt(i)
                                            if (i > 0) focusRequesters[i - 1].requestFocus()
                                        } else {
                                            if (i < newCode.size) {
                                                newCode[i] = newValue[0]
                                            } else {
                                                newCode.add(newValue[0])
                                            }
                                            if (i < 5) focusRequesters[i + 1].requestFocus()
                                        }
                                        verificationCode.value = newCode.joinToString("")
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                modifier = Modifier
                                    .size(boxSize)
                                    .focusRequester(focusRequesters[i])
                                    .onFocusChanged { focusState ->
                                        isFocused.value = focusState.isFocused
                                    }
                                    .pointerInput(Unit) {
                                        detectTapGestures { focusRequesters[i].requestFocus() }
                                    }
                                    .onKeyEvent { keyEvent ->
                                        if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Backspace) {
                                            if (char.isEmpty() && i > 0) {
                                                focusRequesters[i - 1].requestFocus() // Move focus to the previous box
                                            } else if (i == 0 && char.isNotEmpty()) {
                                                verificationCode.value = verificationCode.value.dropLast(1)
                                            }
                                            true
                                        } else {
                                            false
                                        }
                                    },
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                ),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .border(
                                                width = 1.dp,
                                                color = if (isFocused.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        innerTextField()
                                    }
                                }
                            )
                        }
                    }
                }
            }




            Spacer(modifier = Modifier.height(24.dp))

            // Verify email button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("VERIFY EMAIL")
            }
        }
    }
}