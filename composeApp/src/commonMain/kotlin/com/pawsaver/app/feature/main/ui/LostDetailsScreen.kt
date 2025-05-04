package com.pawsaver.app.feature.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pawsaver.app.core.data.GpsPosition
import com.pawsaver.app.feature.main.data.ListingsResponse
import com.pawsaver.app.feature.main.ui.components.LocationVisualizer
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
fun LostDetailsScreen(id: Int, onNavigateBack: () -> Unit) {

    val verticalScrollEnableState = remember { mutableStateOf(true) }

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

                val locationShape = RoundedCornerShape(10.dp)
                LocationVisualizer(
                    Modifier
                        .clip(locationShape)
                        .border(1.dp, Color.Gray, locationShape)
                        .fillMaxWidth()
                        .height(200.dp),
                    gps = GpsPosition(
                        latitude = 40.7128,
                        longitude = -74.0060
                    ),
                    title = "Last seen location",
                    parentScrollEnableState = verticalScrollEnableState,
                )
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