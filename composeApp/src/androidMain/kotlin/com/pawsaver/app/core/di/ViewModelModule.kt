package com.pawsaver.app.core.di

import com.pawsaver.app.feature.login.ui.ForgotPasswordScreenViewModel
import com.pawsaver.app.feature.login.ui.NewPasswordScreenViewModel
import com.pawsaver.app.feature.login.ui.ResetPasswordScreenViewModel
import com.pawsaver.app.feature.login.ui.ShelterSignUpScreenViewModel
import com.pawsaver.app.feature.login.ui.SignInScreenViewModel
import com.pawsaver.app.feature.login.ui.UserSignUpScreenViewModel
import com.pawsaver.app.feature.login.ui.VerifyEmailScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val viewModelModule = module {
    viewModelOf(::SignInScreenViewModel)
    viewModelOf(::UserSignUpScreenViewModel)
    viewModelOf(::ShelterSignUpScreenViewModel)
    viewModelOf(::VerifyEmailScreenViewModel)
    viewModelOf(::ForgotPasswordScreenViewModel)
    viewModelOf(::ResetPasswordScreenViewModel)
    viewModelOf(::NewPasswordScreenViewModel)
}