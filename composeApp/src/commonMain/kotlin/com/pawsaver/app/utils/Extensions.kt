package com.pawsaver.app.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.inputFieldPaddings(): Modifier {
    return this.padding(PaddingValues(
        start = 16.dp,
        top = 0.dp,
        end = 16.dp,
        bottom = 16.dp
    ))
}