package com.pawsaver.app.core.di

import com.pawsaver.app.feature.login.ui.SignInScreenViewModel
import com.pawsaver.app.feature.login.ui.UserSignUpScreenViewModel
import com.pawsaver.app.feature.login.ui.VerifyEmailScreenViewModel
import com.pawsaver.app.feature.login.ui.ShelterSignUpScreenViewModel
import com.pawsaver.app.feature.login.ui.ForgotPasswordScreenViewModel
import com.pawsaver.app.feature.login.ui.ResetPasswordScreenViewModel
import com.pawsaver.app.feature.login.ui.NewPasswordScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val viewModelModule = module {
    singleOf(::SignInScreenViewModel)
    singleOf(::UserSignUpScreenViewModel)
    singleOf(::ShelterSignUpScreenViewModel)
    singleOf(::VerifyEmailScreenViewModel)
    singleOf(::ForgotPasswordScreenViewModel)
    singleOf(::ResetPasswordScreenViewModel)
    singleOf(::NewPasswordScreenViewModel)
}