package com.pawsaver.app.feature.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.pawsaver.app.core.ui.components.MinimalDialog
import com.pawsaver.app.core.utils.koinViewModel

@Composable
fun ProfileScreen(
    errorHandler: (String) -> Unit
) {

    val viewModel = koinViewModel<ProfileScreenViewModel>()
    val profileScreenState by viewModel.profileScreenState.collectAsState()
    val fullname = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    Text(
                        text = "MK",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = fullname.value, style = MaterialTheme.typography.titleLarge)
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "My Account",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ProfileOption(text = "Edit Profile", icon = Icons.Default.Edit, onClick = {})
                HorizontalDivider()
                ProfileOption(text = "My Ads", icon = Icons.AutoMirrored.Filled.List, onClick = {})
                HorizontalDivider()
                ProfileOption(text = "Bookmarked Pets", icon = Icons.Default.Bookmark, onClick = {})

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ProfileOption(
                    text = "Notifications",
                    icon = Icons.Default.Notifications,
                    onClick = {})
                HorizontalDivider()
                ProfileOption(text = "Language", icon = Icons.Default.Language, onClick = {})
                HorizontalDivider()
                ProfileOption(
                    text = "Sign Out",
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    onClick = {})
            }

        }

        when (profileScreenState) {
            ProfileScreenViewModel.ProfileScreenState.Idle -> Unit
            ProfileScreenViewModel.ProfileScreenState.Loading -> MinimalDialog("Loading", true)
            is ProfileScreenViewModel.ProfileScreenState.Success -> {
                val user =
                    (profileScreenState as ProfileScreenViewModel.ProfileScreenState.Success).response
                fullname.value = "${user.firstName} ${user.lastName}"


            }

            is ProfileScreenViewModel.ProfileScreenState.Error -> {
                val errorMessage =
                    (profileScreenState as ProfileScreenViewModel.ProfileScreenState.Error).error.message
                errorHandler.invoke(errorMessage)
            }

        }
    }
}

@Composable
fun ProfileOption(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}