package com.pawsaver.app.feature.login.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

sealed interface LoginRouting {
    @Serializable
    data object SignIn : LoginRouting

    @Serializable
    data object UserSignUp : LoginRouting

    @Serializable
    data object ShelterSignUp : LoginRouting

    @Serializable
    data object ForgotPassword : LoginRouting

    @Serializable
    data class ResetPassword(val encodedPk: String) : LoginRouting

    @Serializable
    data class NewPassword(val encodedPk: String, val resetToken: String) : LoginRouting

    @Serializable
    data class VerifyEmail(val email: String) : LoginRouting
}

fun NavGraphBuilder.loginRouting(navController: NavHostController) {
    composable<LoginRouting.SignIn> {
        SignInScreen(
            onNavigateToForgotPassword = { navController.navigate(LoginRouting.ForgotPassword) },
            onNavigateToUserSignUp = { navController.navigate(LoginRouting.UserSignUp) },
            onNavigateToShelterSignUp = { navController.navigate(LoginRouting.ShelterSignUp) },
            onNavigateToHomeScreen = {
                navController.navigate("main") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<LoginRouting.UserSignUp> {
        UserSignUpScreen(
            onNavigateBack = { navController.navigateUp() },
            onNavigateVerifyEmail = { email ->
                navController.navigate(LoginRouting.VerifyEmail(email))
            }
        )
    }

    composable<LoginRouting.ShelterSignUp> {
        ShelterSignUpScreen(
            onNavigateBack = { navController.navigateUp() },
            onNavigateVerifyEmail = { email ->
                navController.navigate(LoginRouting.VerifyEmail(email))
            }
        )
    }

    composable<LoginRouting.VerifyEmail> { backStackEntry ->
        val email = backStackEntry.toRoute<LoginRouting.VerifyEmail>().email
        VerifyEmailScreen(
            email = email,
            onNavigateBack = { navController.navigateUp() },
            onNavigateToHomeScreen = {
                navController.navigate("main") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<LoginRouting.ForgotPassword> {
        ForgotPasswordScreen(
            onNavigateBack = { navController.navigateUp() },
            onNavigateToResetPassword = { encodedPk ->
                navController.navigate(LoginRouting.ResetPassword(encodedPk))
            }
        )
    }

    composable<LoginRouting.ResetPassword> { backStackEntry ->
        val encodedPk = backStackEntry.toRoute<LoginRouting.ResetPassword>().encodedPk
        ResetPasswordScreen(
            encodedPk = encodedPk,
            onNavigateBack = { navController.navigateUp() },
            onNavigateToNewPassword = { resetToken ->
                navController.navigate(LoginRouting.NewPassword(encodedPk, resetToken))
            }
        )
    }

    composable<LoginRouting.NewPassword> { backStackEntry ->
        val encodedPk = backStackEntry.toRoute<LoginRouting.NewPassword>().encodedPk
        val resetToken = backStackEntry.toRoute<LoginRouting.NewPassword>().resetToken
        NewPasswordScreen(
            encodedPk = encodedPk,
            resetToken = resetToken,
            onNavigateBack = {
                navController.popBackStack(LoginRouting.SignIn, inclusive = false)
            }
        )
    }
}