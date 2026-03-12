package com.smartlead.scanner.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.domain.model.LeadPriority
import com.smartlead.scanner.presentation.viewmodel.LeadsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLeadScreen(
    leadId: Int,
    onNavigateBack: () -> Unit,
    viewModel: LeadsViewModel = hiltViewModel()
) {
    val leads by viewModel.leads.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    
    val lead = remember(leads, leadId) { 
        leads.find { it.id == leadId } 
    } as? com.smartlead.scanner.domain.model.Lead

    // Form states
    var street by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var aiGeneratedMessage by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(LeadPriority.JustLooking) }
    var expanded by remember { mutableStateOf(false) }

    // Initialize form values when lead is Loaded
    LaunchedEffect(lead) {
        lead?.let {
            street = it.street ?: ""
            area = it.area ?: ""
            city = it.city ?: ""
            state = it.state ?: ""
            postalCode = it.postalCode ?: ""
            country = it.country ?: ""
            description = it.description ?: ""
            aiGeneratedMessage = it.aiGeneratedMessage ?: ""
            priority = it.priority
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (lead == null) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Edit Lead") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Edit Lead") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text("Lead Details", style = MaterialTheme.typography.titleLarge)
                    Text("Name: ${lead.name}", style = MaterialTheme.typography.bodyLarge)
                    Text("Company: ${lead.company}", style = MaterialTheme.typography.bodyMedium)
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Address Information", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Street") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = area, onValueChange = { area = it }, label = { Text("Area") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = postalCode, onValueChange = { postalCode = it }, label = { Text("Postal Code") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("Country") }, modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Additional Details", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("AI WhatsApp Draft", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    OutlinedTextField(
                        value = aiGeneratedMessage,
                        onValueChange = { aiGeneratedMessage = it },
                        label = { Text("AI Message") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = when(priority) {
                                LeadPriority.WantToBuy -> "Want to Buy"
                                LeadPriority.Interested -> "Interested"
                                LeadPriority.JustLooking -> "Just Looking"
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Priority") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            LeadPriority.entries.forEach { p ->
                                DropdownMenuItem(
                                    text = { 
                                        Text(when(p) {
                                            LeadPriority.WantToBuy -> "Want to Buy"
                                            LeadPriority.Interested -> "Interested"
                                            LeadPriority.JustLooking -> "Just Looking"
                                        })
                                    },
                                    onClick = {
                                        priority = p
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            // Ensure ID is strictly preserved by using lead.copy
                            viewModel.updateLead(lead.copy(
                                street = street,
                                area = area,
                                city = city,
                                state = state,
                                postalCode = postalCode,
                                country = country,
                                description = description,
                                aiGeneratedMessage = aiGeneratedMessage,
                                priority = priority
                            ))
                            onNavigateBack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Changes")
                    }
                }
            }
        }

        if (isSaving) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
