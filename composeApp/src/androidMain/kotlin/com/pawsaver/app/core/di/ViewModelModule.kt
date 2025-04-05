package com.pawsaver.app.core.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.pawsaver.app.feature.login.ui.SignInScreenViewModel
import com.pawsaver.app.feature.login.ui.SignUpScreenViewModel

actual val viewModelModule = module {
    viewModelOf(::SignInScreenViewModel)
    viewModelOf(::SignUpScreenViewModel)
}