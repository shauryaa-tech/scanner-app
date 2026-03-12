package com.smartlead.scanner.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartlead.scanner.R
import android.content.Intent
import android.net.Uri
import android.graphics.Bitmap
import android.widget.Toast
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.domain.model.BusinessCardData
import com.smartlead.scanner.domain.model.LeadPriority
import com.smartlead.scanner.presentation.theme.ConfidenceGreen
import com.smartlead.scanner.presentation.theme.ConfidenceRed
import com.smartlead.scanner.presentation.theme.ConfidenceYellow
import androidx.compose.foundation.background
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.smartlead.scanner.presentation.viewmodel.LeadsViewModel
import com.smartlead.scanner.presentation.viewmodel.ScrapViewModel
import com.smartlead.scanner.presentation.viewmodel.ScrapUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadDetailScreen(
    leadId: Int,
    initialTab: Int = 0,
    onNavigateBack: () -> Unit,
    onNavigateToScrap: (Int) -> Unit,
    viewModel: LeadsViewModel = hiltViewModel(),
    scrapViewModel: ScrapViewModel = hiltViewModel()
) {
    val lead: Lead? by viewModel.getLeadById(leadId).collectAsState(initial = null)
    var selectedTab by remember { mutableStateOf(initialTab) } // 0 for Detail, 1 for Scrap

    val scrapUiState by scrapViewModel.uiState.collectAsState()

    LaunchedEffect(selectedTab, leadId) {
        if (selectedTab == 1) {
            scrapViewModel.fetchScrapDetails(leadId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lead Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        ) {
            when (val currentLead = lead) {
                null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                else -> {
                    val isGeneratingMessage by viewModel.isGeneratingMessage.collectAsState()
                    
                    // Auto-generate if missing
                    LaunchedEffect(currentLead.id) {
                        if (currentLead.aiGeneratedMessage.isNullOrBlank() && currentLead.name.isNotBlank() && currentLead.company.isNotBlank()) {
                            viewModel.generateAiMessage(currentLead, currentLead.description)
                        }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        // Custom Tab Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            TabButton(
                                text = "Lead Detail",
                                isSelected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                modifier = Modifier.weight(1f)
                            )
                            TabButton(
                                text = "Scrap Detail",
                                isSelected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            if (selectedTab == 0) {
                                LeadDetailContent(
                                    lead = currentLead, 
                                    isGeneratingMessage = isGeneratingMessage,
                                    onRegenerateAiMessage = {
                                        viewModel.generateAiMessage(currentLead, currentLead.description)
                                    },
                                    onViewScrapDetails = {
                                        selectedTab = 1
                                    }
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when (val state = scrapUiState) {
                                        is ScrapUiState.Loading -> {
                                            CircularProgressIndicator()
                                        }
                                        is ScrapUiState.Success -> {
                                            Box(modifier = Modifier.fillMaxSize()) {
                                                ScrapContent(state)
                                                
                                                // Refresh FAB or IconButton
                                                IconButton(
                                                    onClick = { scrapViewModel.refreshScrapDetails(leadId) },
                                                    modifier = Modifier
                                                        .align(Alignment.TopEnd)
                                                        .padding(8.dp)
                                                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Refresh,
                                                        contentDescription = "Refresh",
                                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                }
                                            }
                                        }
                                        is ScrapUiState.Error -> {
                                            Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                                        }
                                        else -> {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeadDetailContent(
    lead: Lead, 
    isGeneratingMessage: Boolean,
    onRegenerateAiMessage: () -> Unit,
    onViewScrapDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = lead.name.ifBlank { "Unknown Name" },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                if (lead.designation.isNotBlank()) {
                    Text(
                        text = lead.designation,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Badge(
                    containerColor = when(lead.priority) {
                        LeadPriority.WantToBuy -> Color(0xFFFF5252)
                        LeadPriority.Interested -> Color(0xFFFFB74D)
                        LeadPriority.JustLooking -> Color(0xFF64B5F6)
                    },
                    contentColor = Color.White
                ) {
                    Text(
                        text = when(lead.priority) {
                            LeadPriority.WantToBuy -> "WANT TO BUY"
                            LeadPriority.Interested -> "INTERESTED"
                            LeadPriority.JustLooking -> "JUST LOOKING"
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // Info Sections
        DetailSection(title = "Contact Information", icon = Icons.Default.ContactPage) {
            InfoRow(label = "Company", value = lead.company)
            InfoRowWithWhatsApp(label = "Phone", value = lead.phone, lead = lead)
            InfoRow(label = "Email", value = lead.email)
            InfoRow(label = "Website", value = lead.website)
            InfoRow(label = "LinkedIn", value = lead.linkedIn)
        }

        DetailSection(title = "Address", icon = Icons.Default.LocationOn) {
            val address = buildString {
                if (!lead.street.isNullOrBlank()) append(lead.street).append("\n")
                if (!lead.area.isNullOrBlank()) append(lead.area).append("\n")
                if (!lead.city.isNullOrBlank()) append(lead.city).append(", ")
                if (!lead.state.isNullOrBlank()) append(lead.state).append(" ")
                if (!lead.postalCode.isNullOrBlank()) append(lead.postalCode)
                if (!lead.country.isNullOrBlank()) append("\n").append(lead.country)
            }.trim()
            
            if (address.isNotBlank()) {
                Text(text = address, style = MaterialTheme.typography.bodyLarge)
            } else {
                Text(text = "No address available", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            }
        }

        DetailSection(title = "Scraping & AI Analysis", icon = Icons.Default.Business) {
            InfoRow(label = "Company Status", value = lead.scrapedCompanyStatus)
            InfoRow(label = "Company Category", value = lead.companyCategory)
            
            Spacer(modifier = Modifier.height(12.dp))
            Text("AI WhatsApp Draft", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    if (isGeneratingMessage) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp).align(Alignment.Center))
                    }
                    
                    Text(
                        text = lead.aiGeneratedMessage ?: "Generating personalized message...",
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                        color = if (lead.aiGeneratedMessage == null) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            TextButton(
                onClick = onRegenerateAiMessage,
                enabled = !isGeneratingMessage,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Regenerate AI Message", style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onViewScrapDetails,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(imageVector = Icons.Default.Business, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Full Scrap Details")
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            val confidenceColor = when {
                lead.overallConfidence >= 90 -> ConfidenceGreen
                lead.overallConfidence >= 70 -> ConfidenceYellow
                else -> ConfidenceRed
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("AI Confidence: ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text("${lead.overallConfidence}%", color = confidenceColor, fontWeight = FontWeight.Bold)
            }
        }

        DetailSection(title = "Additional Information", icon = Icons.Default.Info) {
            if (lead.description.isNullOrBlank()) {
                Text("No additional description provided.", color = Color.Gray)
            } else {
                Text(text = lead.description)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Captured on: ${java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(lead.timestamp))}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DetailSection(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun InfoRowWithWhatsApp(label: String, value: String?, lead: Lead? = null) {
    val context = LocalContext.current
    var showNumberSelector by remember { mutableStateOf(false) }
    val phoneNumbers = remember(value) {
        value?.split(Regex("[,;/|]+"))
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() } ?: emptyList()
    }

    fun openWhatsApp(number: String) {
        val formattedNumber = number.replace(Regex("[^0-9+]"), "")
        val message = if (!lead?.aiGeneratedMessage.isNullOrBlank()) {
            lead?.aiGeneratedMessage!!
        } else if (lead != null && lead.name.isNotBlank() && lead.company.isNotBlank()) {
            "Hi ${lead.name},\n\nI came across your profile" + 
            (if (lead.designation.isNotBlank()) " as ${lead.designation}" else "") + 
            " at ${lead.company}. I'd love to connect and explore potential synergies between us. Let me know when you have a moment to chat!"
        } else {
            "Hello,"
        }
        val encodedMessage = Uri.encode(message)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://api.whatsapp.com/send?phone=$formattedNumber&text=$encodedMessage")
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    if (showNumberSelector) {
        AlertDialog(
            onDismissRequest = { showNumberSelector = false },
            title = { Text("Select WhatsApp Number") },
            text = {
                Column {
                    phoneNumbers.forEach { number ->
                        TextButton(
                            onClick = {
                                showNumberSelector = false
                                openWhatsApp(number)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(number)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNumberSelector = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (!value.isNullOrBlank()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Text(text = value, style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(
                onClick = {
                    if (phoneNumbers.size > 1) {
                        showNumberSelector = true
                    } else if (phoneNumbers.isNotEmpty()) {
                        openWhatsApp(phoneNumbers.first())
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_whatsapp),
                    contentDescription = "WhatsApp",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}
