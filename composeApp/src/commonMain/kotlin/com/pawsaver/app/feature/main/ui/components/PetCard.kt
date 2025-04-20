package com.pawsaver.app.feature.main.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pawsaver.composeapp.generated.resources.Res
import pawsaver.composeapp.generated.resources.compose_multiplatform

@Composable
fun PetCard() {
    Card(
        modifier = Modifier
            .heightIn(min = 100.dp)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column {
            Image(
                painterResource(Res.drawable.compose_multiplatform),
                null,
                modifier = Modifier
                    .heightIn(min = 100.dp, max = 200.dp),
                contentScale = ContentScale.FillWidth
            )
            Column {
                Text("Pet name")
                Text("Breed")
                Text("Supporting or descriptive text for the card goes here like a pro.")
            }
        }
    }
}