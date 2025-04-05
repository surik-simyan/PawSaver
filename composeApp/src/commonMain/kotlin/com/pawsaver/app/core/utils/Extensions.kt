package com.pawsaver.app.core.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.currentKoinScope

@Composable
fun Modifier.inputFieldPaddings(isSupportingTextEnabled: Boolean = true): Modifier {
    return this.padding(PaddingValues(
        start = 16.dp,
        top = 0.dp,
        end = 16.dp,
        bottom = if (isSupportingTextEnabled) 0.dp else 16.dp
    ))
}

@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}

fun Throwable.errorOrNull() : List<String>? {
    return this.message?.let { listOf(it) }
}

fun <T> List<T>?.isNotNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

fun List<String>?.firstOrBlank(): String {
    return this?.firstOrNull() ?: ""
}

@Composable
fun String?.toErrorText() {
    if (!this.isNullOrBlank()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = this,
            color = MaterialTheme.colorScheme.error
        )
    }
}