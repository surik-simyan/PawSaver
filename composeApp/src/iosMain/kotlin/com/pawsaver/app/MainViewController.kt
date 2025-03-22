package com.pawsaver.app

import androidx.compose.ui.window.ComposeUIViewController
import com.pawsaver.app.di.KoinInitializer

fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInitializer().init()
    }
) { App() }