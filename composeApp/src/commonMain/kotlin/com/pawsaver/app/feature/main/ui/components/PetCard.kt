package com.pawsaver.app.feature.main.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pawsaver.app.feature.main.data.ListingsResponse
import org.jetbrains.compose.resources.painterResource
import pawsaver.composeapp.generated.resources.Res
import pawsaver.composeapp.generated.resources.compose_multiplatform

@Composable
fun PetCard(
    listing: ListingsResponse.Listing,
    onNavigateToDetailsScreen: () -> Unit
) {
    Card(
        modifier = Modifier
            .heightIn(min = 100.dp)
            .fillMaxWidth()
            .clickable {
                onNavigateToDetailsScreen()
            }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Fixed height for consistency
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = "https://cdn.britannica.com/34/233234-050-1649BFA9/Pug-dog.jpg", // Placeholder
                    contentDescription = "${listing.name} image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.compose_multiplatform), // Fallback
                    error = painterResource(Res.drawable.compose_multiplatform) // Fallback
                )
            }

            // Text Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // Padding between image and text
                verticalArrangement = Arrangement.spacedBy(4.dp) // Spacing between text elements
            ) {
                // Name
                Text(
                    text = listing.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Breed and Type
                Text(
                    text = "${listing.breed} (${listing.type})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                // Description
                Text(
                    text = listing.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}