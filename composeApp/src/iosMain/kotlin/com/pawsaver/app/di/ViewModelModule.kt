package com.pawsaver.app.di

import com.pawsaver.app.ui.screens.SignInScreenViewModel
import com.pawsaver.app.ui.screens.SignUpScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val viewModelModule = module {
    singleOf(::SignInScreenViewModel)
    singleOf(::SignUpScreenViewModel)
}