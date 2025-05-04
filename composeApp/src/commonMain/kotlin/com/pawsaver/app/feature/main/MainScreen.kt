package com.pawsaver.app.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    onNavigateToAdoptionDetailsScreen: (id: Int) -> Unit,
    onNavigateToAddAdoptionListingScreen: () -> Unit,
    onNavigateToLostDetailsScreen: (id: Int) -> Unit,
) {
    val bottomNavController = rememberNavController()
    val currentBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = { MainBottomNavigationBar(bottomNavController) },
        floatingActionButton = {
            currentDestination?.let { destination ->
                when {
                    destination.hasRoute(BottomNavRouting.Home::class) -> {
                        ExtendedFloatingActionButton(
                            onClick = { onNavigateToAddAdoptionListingScreen() },
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            BottomNavHost(
                bottomNavController,
                onNavigateToAdoptionDetailsScreen,
                onNavigateToLostDetailsScreen
            ) { message ->
                scope.launch {
                    val result = snackbarHostState.showSnackbar(message)
//                    if (result == SnackbarResult.Dismissed) {
//                        viewModel.resetState()
//                    }
                }
            }
        }
    }
}