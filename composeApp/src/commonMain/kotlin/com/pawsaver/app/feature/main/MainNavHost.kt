package com.pawsaver.app.feature.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pawsaver.app.feature.main.ui.AddAnimalListingScreen
import com.pawsaver.app.feature.main.ui.AdoptionDetailsScreen
import com.pawsaver.app.feature.main.ui.LostDetailsScreen
import kotlinx.serialization.Serializable

sealed interface MainRouting {
    @Serializable
    data object Main : MainRouting

    @Serializable
    data class AdoptionDetailsScreen(val id: Int) : MainRouting

    @Serializable
    data object AddAnimalListingScreen : MainRouting

    @Serializable
    data class LostDetailsScreen(val id: Int) : MainRouting
}

fun NavGraphBuilder.mainRouting(navController: NavHostController) {

    composable<MainRouting.Main> {
        MainScreen(
            onNavigateToAdoptionDetailsScreen = { id ->
                navController.navigate(MainRouting.AdoptionDetailsScreen(id))
            },
            onNavigateToAddAdoptionListingScreen = {
                navController.navigate(MainRouting.AddAnimalListingScreen)
            },
            onNavigateToLostDetailsScreen = { id ->
                navController.navigate(MainRouting.LostDetailsScreen(id))
            }
        )
    }

    composable<MainRouting.AdoptionDetailsScreen> { backStackEntry ->
        val id = backStackEntry.toRoute<MainRouting.AdoptionDetailsScreen>().id
        AdoptionDetailsScreen(
            id = id,
            onNavigateBack = { navController.navigateUp() }
        )
    }

    composable<MainRouting.AddAnimalListingScreen> {
        AddAnimalListingScreen(
            onNavigateBack = { navController.navigateUp() }
        )
    }

    composable<MainRouting.LostDetailsScreen> { backStackEntry ->
        val id = backStackEntry.toRoute<MainRouting.LostDetailsScreen>().id
        LostDetailsScreen(
            id = id,
            onNavigateBack = { navController.navigateUp() },
        )
    }
}