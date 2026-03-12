package com.smartlead.scanner.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartlead.scanner.presentation.viewmodel.ScrapUiState
import com.smartlead.scanner.presentation.viewmodel.ScrapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapDetailScreen(
    leadId: Int,
    viewModel: ScrapViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(leadId) {
        viewModel.fetchScrapDetails(leadId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scrap Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is ScrapUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is ScrapUiState.Success -> {
                    ScrapContent(state)
                }
                is ScrapUiState.Error -> {
                    Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ScrapContent(state: ScrapUiState.Success) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InfoItem("Person Name", state.personName)
        InfoItem("Company Name", state.companyName)
        
        HorizontalDivider()
        
        Text(
            text = "Scrapped Data",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        state.response?.let { response ->
            InfoItem("Status", response.status ?: "N/A")
            response.name?.let { InfoItem("Name", it) }
            response.headline?.let { InfoItem("Headline", it) }
            response.companySize?.let { InfoItem("Company Size", it) }
            response.teamSize?.let { InfoItem("Team Size", it) }
            
            response.companyWebsite?.let { url -> 
                InfoItem("Company Website", url, isLink = true) { 
                    try { uriHandler.openUri(url) } catch (e: Exception) {} 
                } 
            }
            
            response.companyLinkedinUrl?.let { url -> 
                InfoItem("Company LinkedIn URL", url, isLink = true) { 
                    try { uriHandler.openUri(url) } catch (e: Exception) {} 
                } 
            }
            
            response.linkedinUrl?.let { url -> 
                InfoItem("LinkedIn URL", url, isLink = true) { 
                    try { uriHandler.openUri(url) } catch (e: Exception) {} 
                } 
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Details:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    response.aboutSummary?.let {
                        Text(text = "About:\n$it\n", style = MaterialTheme.typography.bodyMedium)
                    }
                    response.currentRole?.let { role ->
                        Text(text = "Current Role:\n${role.designation} at ${role.companyName}\nTenure: ${role.tenureDuration ?: "N/A"}\n", style = MaterialTheme.typography.bodyMedium)
                    }
                    response.previousRole?.let { role ->
                        Text(text = "Previous Role:\n${role.designation} at ${role.companyName}\nTenure: ${role.tenureDuration ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        } ?: Text("No scrap data available")
    }
}

@Composable
fun InfoItem(
    label: String, 
    value: String, 
    isLink: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val color = if (isLink) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    
    Column(
        modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (isLink) androidx.compose.ui.text.style.TextDecoration.Underline else null
            ),
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}
