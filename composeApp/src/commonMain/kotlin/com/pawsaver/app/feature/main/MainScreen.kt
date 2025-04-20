package com.pawsaver.app.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val bottomNavController = rememberNavController()
    val currentBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    Scaffold(
        bottomBar = { MainBottomNavigationBar(bottomNavController) },
        floatingActionButton = {
            currentDestination?.let { destination ->
                when {
                    destination.hasRoute(BottomNavRouting.Home::class) -> {
                        ExtendedFloatingActionButton(
                            onClick = { },
                            icon = { Icon(Icons.Filled.Add, "Add pet") },
                            text = { Text(text = "Add pet") },
                        )
                    }

                    destination.hasRoute(BottomNavRouting.Lost::class) -> {
                        ExtendedFloatingActionButton(
                            onClick = { },
                            icon = { Icon(Icons.Filled.Add, "Report pet") },
                            text = { Text(text = "Report pet") },
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            BottomNavHost(bottomNavController)
        }
    }
}