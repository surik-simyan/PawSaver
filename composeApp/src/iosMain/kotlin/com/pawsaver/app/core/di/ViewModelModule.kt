package com.pawsaver.app.core.di

import com.pawsaver.app.feature.login.ui.SignInScreenViewModel
import com.pawsaver.app.feature.login.ui.SignUpScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val viewModelModule = module {
    singleOf(::SignInScreenViewModel)
    singleOf(::SignUpScreenViewModel)
}