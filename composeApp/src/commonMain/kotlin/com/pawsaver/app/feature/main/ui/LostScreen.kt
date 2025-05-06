package com.pawsaver.app.feature.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawsaver.app.core.ui.components.MinimalDialog
import com.pawsaver.app.core.utils.koinViewModel
import com.pawsaver.app.feature.main.data.ListingsResponse
import com.pawsaver.app.feature.main.ui.components.PetCard

@Composable
fun LostScreen(
    onNavigateToLostDetailsScreen: (id: Int) -> Unit,
    errorHandler: (String) -> Unit
) {

    val viewModel = koinViewModel<LostScreenViewModel>()
    val lostScreenState by viewModel.lostScreenState.collectAsState()
    var listingItems by remember { mutableStateOf(emptyList<ListingsResponse.Listing>()) }

    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (listingItems.isNotEmpty()) {
            LazyColumn {
                items(listingItems) {
                    PetCard(it) {
                        onNavigateToLostDetailsScreen(it.id)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No lost pets are reported :)")
            }
        }

        when (lostScreenState) {
            LostScreenViewModel.LostScreenState.Idle -> Unit
            LostScreenViewModel.LostScreenState.Loading -> MinimalDialog("Loading", true)
            is LostScreenViewModel.LostScreenState.Success -> {
                listingItems =
                    (lostScreenState as LostScreenViewModel.LostScreenState.Success).response.results
            }

            is LostScreenViewModel.LostScreenState.Error -> {
                val errorMessage =
                    (lostScreenState as LostScreenViewModel.LostScreenState.Error).error.message
                errorHandler.invoke(errorMessage)
            }

        }
    }
}