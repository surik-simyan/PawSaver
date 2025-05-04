package com.pawsaver.app.feature.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawsaver.app.feature.main.data.ListingsResponse
import com.pawsaver.app.feature.main.ui.components.PetDetailsSection
import com.pawsaver.app.feature.main.ui.components.PetImageCarousel

private val tempPet = ListingsResponse.Listing(
    id = 1,
    name = "Buddy",
    type = ListingsResponse.AnimalType.DOG,
    breed = "Golden Retriever",
    gender = ListingsResponse.Gender.MALE,
    birthDate = "2023-02-17",
    weight = 7.0,
    description = "A playful mops with knowledge of a lot of commands in a search of new home",
    listingType = ListingsResponse.ListingType.ADOPTION,
    status = "available",
    lastSeenLocation = null,
    lastSeenDate = null,
    listingDate = "2025-04-27T13:44:06.227608Z",
    age = 2
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdoptionDetailsScreen(id: Int, onNavigateBack: () -> Unit) {

    var bookmarked by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Pet Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    if (bookmarked) {
                        IconButton(onClick = { bookmarked = false }) {
                            Icon(
                                imageVector = Icons.Filled.Bookmark,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        IconButton(onClick = { bookmarked = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Bookmark,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main content with weight to push button to bottom
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()) // Make content scrollable
            ) {
                Spacer(modifier = Modifier.height(16.dp)) // Padding above carousel

                // Image Carousel
                PetImageCarousel(
                    images = listOf(
                        "https://cdn.britannica.com/34/233234-050-1649BFA9/Pug-dog.jpg",
                        "https://cdn.britannica.com/35/233235-050-8DED07E3/Pug-dog.jpg"
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Pet Details
                PetDetailsSection(tempPet)

                Spacer(modifier = Modifier.height(16.dp)) // Extra padding at bottom of content
            }

            // Divider
            HorizontalDivider(
                thickness = 1.dp,
            )

            // Action Button (stuck to bottom, styled like sign-in button)
            Button(
                onClick = { /* Handle adoption/contact action */ },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text("ADOPT ${tempPet.name.uppercase()}")
            }
        }
    }
}