package com.pawsaver.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.pawsaver.app.ui.screens.SignInScreenViewModel
import com.pawsaver.app.ui.screens.SignUpScreenViewModel

actual val viewModelModule = module {
    viewModelOf(::SignInScreenViewModel)
    viewModelOf(::SignUpScreenViewModel)
}