package com.pawsaver.app.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pawsaver.app.feature.main.ui.HomeScreen
import com.pawsaver.app.feature.main.ui.LostScreen
import com.pawsaver.app.feature.main.ui.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavRouting(val label: String) {
    @Serializable
    data object Home : BottomNavRouting("Home")

    @Serializable
    data object Lost : BottomNavRouting("Lost")

    @Serializable
    data object Profile : BottomNavRouting("Profile")
}

@Composable
fun BottomNavHost(
    navController: NavHostController,
    onNavigateToDetailsScreen: (id: Int) -> Unit,
    onNavigateToLostDetailsScreen: (id: Int) -> Unit,
    errorHandler: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavRouting.Home,
    ) {
        composable<BottomNavRouting.Home> {
            HomeScreen(onNavigateToDetailsScreen, errorHandler)
        }
        composable<BottomNavRouting.Lost> {
            LostScreen(onNavigateToLostDetailsScreen, errorHandler)
        }
        composable<BottomNavRouting.Profile> {
            ProfileScreen(errorHandler)
        }
    }
}

@Composable
fun MainBottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavRouting.Home,
        BottomNavRouting.Lost,
        BottomNavRouting.Profile
    )

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(item::class)
                } == true,
                onClick = {
                    navController.navigate(item) {
                        popUpTo(BottomNavRouting.Home) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(text = item.label) },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = when (item) {
                            BottomNavRouting.Home -> Icons.Filled.Home
                            BottomNavRouting.Lost -> Icons.Filled.Search
                            BottomNavRouting.Profile -> Icons.Filled.AccountCircle
                        },
                        contentDescription = item.toString()
                    )
                }
            )
        }
    }
}

