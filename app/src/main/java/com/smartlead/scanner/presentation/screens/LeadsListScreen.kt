package com.smartlead.scanner.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.presentation.viewmodel.LeadsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadsListScreen(
    viewModel: LeadsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onLeadClick: (Int) -> Unit,
    onScrapClick: (Int) -> Unit
) {
    val leads by viewModel.leads.collectAsState()
    val scrapingIds by viewModel.scrapingIds.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Leads") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (leads.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No leads saved yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(leads) { lead ->
                    val isScraping = scrapingIds.contains(lead.id)
                    LeadCard(
                        lead = lead,
                        isScraping = isScraping,
                        onDelete = { viewModel.deleteLead(lead) },
                        onEdit = { onNavigateToEdit(lead.id) },
                        onScrap = { 
                            if (lead.scrapedDataJson.isNullOrBlank()) {
                                viewModel.scrapLead(lead.id)
                            } else {
                                onScrapClick(lead.id)
                            }
                        },
                        onClick = {
                            onLeadClick(lead.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LeadCard(
    lead: Lead,
    isScraping: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onScrap: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lead.name.ifEmpty { "Unknown Name" },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                if (lead.designation.isNotEmpty()) {
                    Text(
                        text = lead.designation,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
                if (lead.company.isNotEmpty()) {
                    Text(
                        text = lead.company,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when {
                            isScraping -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            !lead.scrapedDataJson.isNullOrBlank() -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = CircleShape
                    )
            ) {
                IconButton(
                    onClick = onScrap,
                    enabled = !isScraping && lead.scrapedDataJson.isNullOrBlank(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (isScraping) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        val isScraped = !lead.scrapedDataJson.isNullOrBlank()
                        Icon(
                            imageVector = if (isScraped) Icons.Default.CheckCircle else Icons.Default.AutoFixHigh,
                            contentDescription = "Scrap Details",
                            tint = if (isScraped) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
