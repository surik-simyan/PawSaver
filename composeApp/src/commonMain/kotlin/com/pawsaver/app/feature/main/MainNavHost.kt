package com.pawsaver.app.feature.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pawsaver.app.feature.main.ui.HomeScreen

fun NavGraphBuilder.mainRouting(navController: NavHostController) {
    composable("main") {
        MainScreen()
    }
}