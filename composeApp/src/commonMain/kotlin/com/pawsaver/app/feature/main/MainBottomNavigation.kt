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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pawsaver.app.feature.main.ui.HomeScreen

sealed class BottomNavRoute(val route: String) {
    data object Home : BottomNavRoute("home")
    data object Lost : BottomNavRoute("lost")
    data object Account : BottomNavRoute("account")
}

@Composable
fun BottomNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = BottomNavRoute.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavRoute.Home.route) {
            HomeScreen()
        }
        composable(BottomNavRoute.Lost.route) {

        }
        composable(BottomNavRoute.Account.route) {

        }
    }
}

@Composable
fun MainBottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavRoute.Home,
        BottomNavRoute.Lost,
        BottomNavRoute.Account
    )

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(BottomNavRoute.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(text = item.route.replaceFirstChar(Char::titlecase)) },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = when (item) {
                            BottomNavRoute.Home -> Icons.Filled.Home
                            BottomNavRoute.Lost -> Icons.Filled.Search
                            BottomNavRoute.Account -> Icons.Filled.AccountCircle
                        },
                        contentDescription = item.route
                    )
                }
            )
        }
    }
}

