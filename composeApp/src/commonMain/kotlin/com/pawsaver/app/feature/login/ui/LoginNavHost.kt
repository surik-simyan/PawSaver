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
    data object SignUp : LoginRouting
    @Serializable
    data object ForgotPassword : LoginRouting
    @Serializable
    data class VerifyEmail(val email: String) : LoginRouting
}

fun NavGraphBuilder.loginRouting(navController: NavHostController) {
    composable<LoginRouting.SignIn> {
        SignInScreen(
            onNavigateToForgotPassword = { navController.navigate(LoginRouting.ForgotPassword) },
            onNavigateToSignUp = { navController.navigate(LoginRouting.SignUp) },
            onNavigateToHomeScreen = {
                navController.navigate("main") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<LoginRouting.SignUp> {
        SignUpScreen(
            onNavigateBack = { navController.navigateUp() },
            onNavigateVerifyEmail = { email ->
                navController.navigate(LoginRouting.VerifyEmail(email))
            }
        )
    }

    composable<LoginRouting.ForgotPassword> {
        ForgotPasswordScreen(
            onNavigateBack = { navController.navigateUp() }
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
}