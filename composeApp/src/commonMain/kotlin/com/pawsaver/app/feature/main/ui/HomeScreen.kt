package com.pawsaver.app.feature.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pawsaver.app.core.ui.components.MinimalDialog
import com.pawsaver.app.core.utils.koinViewModel
import com.pawsaver.app.feature.main.data.ListingsResponse
import com.pawsaver.app.feature.main.ui.components.PetCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetailsScreen: (id: Int) -> Unit,
    errorHandler: (String) -> Unit
) {

    val viewModel = koinViewModel<HomeScreenViewModel>()
    val homeScreenState by viewModel.homeScreenState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var listingItems by remember { mutableStateOf(emptyList<ListingsResponse.Listing>()) }

    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedBreed by remember { mutableStateOf<String?>(null) }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var weightRange by remember { mutableStateOf(0f..100f) }
    var ageRange by remember { mutableStateOf(0f..20f) }

    var showTypeDialog by remember { mutableStateOf(false) }
    var showBreedDialog by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var showAgeDialog by remember { mutableStateOf(false) }

    val breedsByType = mapOf(
        "dog" to listOf("Labrador", "German Shepherd", "Bulldog"),
        "cat" to listOf("Persian", "Siamese", "Maine Coon"),
        "parrot" to listOf("African Grey", "Macaw", "Cockatoo"),
        "turtle" to listOf("Red-Eared Slider", "Box Turtle"),
        "rabbit" to listOf("Holland Lop", "Dutch", "Lionhead"),
        "fish" to listOf("Goldfish", "Betta", "Guppy"),
        "hamster" to listOf("Syrian", "Dwarf", "Roborovski"),
        "other" to listOf("Unknown")
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isEmpty()) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search icon"
                        )
                    } else {
                        IconButton(
                            onClick = { searchQuery = "" }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear search text"
                            )
                        }
                    }
                }
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedType != null,
                        onClick = { showTypeDialog = true },
                        label = { Text("Type${selectedType?.let { ": $it" } ?: ""}") }
                    )
                }

                if (selectedType != null) {
                    item {
                        FilterChip(
                            selected = selectedBreed != null,
                            onClick = { showBreedDialog = true },
                            label = { Text("Breed${selectedBreed?.let { ": $it" } ?: ""}") }
                        )
                    }
                }

                item {
                    FilterChip(
                        selected = selectedGender != null,
                        onClick = { showGenderDialog = true },
                        label = { Text("Gender${selectedGender?.let { ": $it" } ?: ""}") }
                    )
                }

                item {
                    FilterChip(
                        selected = weightRange != 0f..100f,
                        onClick = { showWeightDialog = true },
                        label = { Text("Weight: ${weightRange.start.toInt()}-${weightRange.endInclusive.toInt()} kg") }
                    )
                }

                item {
                    FilterChip(
                        selected = ageRange != 0f..20f,
                        onClick = { showAgeDialog = true },
                        label = { Text("Age: ${ageRange.start.toInt()}-${ageRange.endInclusive.toInt()} yrs") }
                    )
                }
            }
        }

        if (listingItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(listingItems) {
                    PetCard(it) {
                        onNavigateToDetailsScreen(it.id)
                    }
                }
            }
        } else if (
            selectedType != null ||
            selectedBreed != null ||
            selectedGender != null ||
            weightRange != 0f..100f ||
            ageRange != 0f..20f
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No pets found for your search criteria. Try adjusting your filters.")
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No pets available for adoption.")
            }
        }
    }

    if (showTypeDialog) {
        Dialog(onDismissRequest = { showTypeDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Select Animal Type", style = MaterialTheme.typography.titleMedium)
                    val types = listOf(
                        "dog",
                        "cat",
                        "parrot",
                        "turtle",
                        "rabbit",
                        "fish",
                        "hamster",
                        "other"
                    )
                    types.forEach { type ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedType == type,
                                onClick = {
                                    selectedType = type
                                    selectedBreed = null
                                    showTypeDialog = false
                                }
                            )
                            Text(type.capitalize())
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { showTypeDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }

    if (showBreedDialog && selectedType != null) {
        Dialog(onDismissRequest = { showBreedDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Select Breed", style = MaterialTheme.typography.titleMedium)

                    var expanded by remember { mutableStateOf(false) }
                    var selectedBreedText by remember { mutableStateOf(selectedBreed ?: "") }

                    val allBreeds = breedsByType[selectedType] ?: emptyList()
                    val filteredBreeds = allBreeds.filter {
                        it.contains(selectedBreedText, ignoreCase = true)
                    }

                    LaunchedEffect(selectedBreedText, selectedType) {
                        selectedBreed =
                            if (selectedBreedText.isNotBlank() && selectedBreedText !in allBreeds) {
                                "Other"
                            } else {
                                selectedBreedText
                            }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedBreedText,
                            onValueChange = {
                                selectedBreedText = it
                                expanded = true
                            },
                            label = { Text("Breed") },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryEditable)
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded && filteredBreeds.isNotEmpty(),
                            onDismissRequest = { expanded = false }
                        ) {
                            filteredBreeds.forEach { breed ->
                                DropdownMenuItem(
                                    text = { Text(breed) },
                                    onClick = {
                                        selectedBreed = breed
                                        selectedBreedText = breed
                                        expanded = false
                                        showBreedDialog = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { showBreedDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }

    if (showGenderDialog) {
        Dialog(onDismissRequest = { showGenderDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Select Gender", style = MaterialTheme.typography.titleMedium)
                    val genders = listOf("male", "female")
                    genders.forEach { gender ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedGender == gender,
                                onClick = {
                                    selectedGender = gender
                                    showGenderDialog = false
                                }
                            )
                            Text(gender.capitalize())
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { showGenderDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }

    if (showWeightDialog) {
        Dialog(onDismissRequest = { showWeightDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Select Weight Range (kg)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    var tempWeightRange by remember { mutableStateOf(weightRange) }
                    RangeSlider(
                        value = tempWeightRange,
                        onValueChange = { tempWeightRange = it },
                        valueRange = 0f..100f,
                        steps = 99,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Selected: ${tempWeightRange.start.toInt()} - ${tempWeightRange.endInclusive.toInt()} kg")
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showWeightDialog = false }) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                weightRange = tempWeightRange
                                showWeightDialog = false
                            }
                        ) {
                            Text("Apply")
                        }
                    }
                }
            }
        }
    }

    if (showAgeDialog) {
        Dialog(onDismissRequest = { showAgeDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Select Age Range (years)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    var tempAgeRange by remember { mutableStateOf(ageRange) }
                    RangeSlider(
                        value = tempAgeRange,
                        onValueChange = { tempAgeRange = it },
                        valueRange = 0f..20f,
                        steps = 19, // For 0.1 increments
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Selected: ${tempAgeRange.start.toInt()} - ${tempAgeRange.endInclusive.toInt()} years")
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showAgeDialog = false }) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                ageRange = tempAgeRange
                                showAgeDialog = false
                            }
                        ) {
                            Text("Apply")
                        }
                    }
                }
            }
        }
    }

    when (homeScreenState) {
        HomeScreenViewModel.HomeScreenState.Idle -> Unit
        HomeScreenViewModel.HomeScreenState.Loading -> MinimalDialog("Loading", true)
        is HomeScreenViewModel.HomeScreenState.Success -> {
            listingItems =
                (homeScreenState as HomeScreenViewModel.HomeScreenState.Success).response.results
        }

        is HomeScreenViewModel.HomeScreenState.Error -> {
            val errorMessage =
                (homeScreenState as HomeScreenViewModel.HomeScreenState.Error).error.message
            errorHandler.invoke(errorMessage)
        }
    }
}