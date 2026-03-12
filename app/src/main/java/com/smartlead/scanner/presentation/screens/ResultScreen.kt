@file:OptIn(ExperimentalMaterial3Api::class)
package com.smartlead.scanner.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartlead.scanner.domain.model.BusinessCardData
import com.smartlead.scanner.R
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.domain.model.LeadPriority
import com.smartlead.scanner.presentation.theme.ConfidenceGreen
import com.smartlead.scanner.presentation.theme.ConfidenceRed
import com.smartlead.scanner.presentation.theme.ConfidenceYellow
import com.smartlead.scanner.presentation.viewmodel.ScannerUiState
import com.smartlead.scanner.presentation.viewmodel.ScannerViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState

@Composable
fun ResultScreen(
    viewModel: ScannerViewModel,
    onSaveSuccess: () -> Unit,
    onRescan: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Scan Result") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (val state = uiState) {
                    is ScannerUiState.Idle -> {
                        // Do nothing
                    }
                    is ScannerUiState.Processing -> {
                        LoadingShimmer()
                    }
                    is ScannerUiState.Success -> {
                        ResultEditor(
                            viewModel = viewModel,
                            data = state.data,
                            onSave = { lead, onSuccess, onDuplicate ->
                                viewModel.saveLead(lead, onSuccess, onDuplicate)
                            },
                            onSaveSuccess = onSaveSuccess,
                            onRescan = {
                                viewModel.resetState()
                                onRescan()
                            }
                        )
                    }
                    is ScannerUiState.Error -> {
                        ErrorState(
                            message = state.message,
                            onRetry = onRescan
                        )
                    }
                }
            }
        }

        if (isSaving) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun LoadingShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text("AI is extracting data...", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Extraction Failed", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Text(message, textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text("Try Again")
        }
    }
}

@Composable
fun ResultEditor(
    viewModel: ScannerViewModel,
    data: BusinessCardData,
    onSave: (Lead, () -> Unit, () -> Unit) -> Unit,
    onSaveSuccess: () -> Unit,
    onRescan: () -> Unit
) {
    var name : String by remember { mutableStateOf(data.name ?: "") }
    var designation : String by remember { mutableStateOf(data.designation ?: "") }
    var company : String by remember { mutableStateOf(data.company ?: "") }
    var phone : String by remember { mutableStateOf(data.phone ?: "") }
    var email : String by remember { mutableStateOf(data.email ?: "") }
    var website : String by remember { mutableStateOf(data.website ?: "") }
    
    var street : String by remember { mutableStateOf(data.street ?: "") }
    var area : String by remember { mutableStateOf(data.area ?: "") }
    var city : String by remember { mutableStateOf(data.city ?: "") }
    var state : String by remember { mutableStateOf(data.state ?: "") }
    var postalCode : String by remember { mutableStateOf(data.postalCode ?: "") }
    var country : String by remember { mutableStateOf(data.country ?: "") }
    
    var description : String by remember { mutableStateOf(data.description ?: "") }
    var aiGeneratedMessage : String by remember { mutableStateOf("") }
    var priority : LeadPriority by remember { mutableStateOf(data.priority) }

    val isGeneratingMessage by viewModel.isGeneratingMessage.collectAsState()

    // Auto-generate AI message when key data changes
    LaunchedEffect(name, company, description) {
        if (name.isNotBlank() && company.isNotBlank() && aiGeneratedMessage.isBlank()) {
            val tempLead = Lead(
                name = name,
                designation = designation,
                company = company,
                phone = phone,
                email = email,
                website = website,
                overallConfidence = data.getOverallConfidence(),
            )
            viewModel.generateAiMessage(tempLead, description) { generated ->
                aiGeneratedMessage = generated
            }
        }
    }

    val overallConfidence = data.getOverallConfidence()

    var showDuplicateDialog by remember { mutableStateOf(false) }

    if (showDuplicateDialog) {
        AlertDialog(
            onDismissRequest = { showDuplicateDialog = false },
            title = { Text("Duplicate Entry") },
            text = { Text("A lead with this phone number already exists.") },
            confirmButton = {
                Button(onClick = { showDuplicateDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ConfidenceBadge(confidence = overallConfidence)
        
        Spacer(modifier = Modifier.height(24.dp))

        EditableField("Name", name) { name = it }
        EditableField("Designation", designation) { designation = it }
        EditableField("Company", company) { company = it }
        EditablePhoneField(
            label = "Phone", 
            value = phone, 
            name = name, 
            designation = designation, 
            company = company,
            aiMessage = aiGeneratedMessage
        ) { phone = it }
        EditableField("Email", email) { email = it }
        EditableField("Website", website) { website = it }
        
        Text("Address Details", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        EditableField("Street", street) { street = it }
        EditableField("Area", area) { area = it }
        EditableField("City", city) { city = it }
        EditableField("State", state) { state = it }
        EditableField("Postal Code", postalCode) { postalCode = it }
        EditableField("Country", country) { country = it }

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
        
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = aiGeneratedMessage,
                onValueChange = { aiGeneratedMessage = it },
                label = { Text("AI Message") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                minLines = 3,
                placeholder = { Text("Generate a personalized message...") }
            )
            
            if (isGeneratingMessage) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).size(32.dp),
                    strokeWidth = 2.dp
                )
            }
        }
        
        TextButton(
            onClick = {
                val tempLead = Lead(
                    name = name,
                    designation = designation,
                    company = company,
                    phone = phone,
                    email = email,
                    website = website,
                    overallConfidence = overallConfidence,
                )
                viewModel.generateAiMessage(tempLead, description) { generated ->
                    aiGeneratedMessage = generated
                }
            },
            enabled = !isGeneratingMessage && name.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_whatsapp), contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Regenerate with AI")
        }

        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = onRescan,
                modifier = Modifier.weight(1f)
            ) {
                Text("Rescan")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    val leadToSave = Lead(
                        name = name,
                        designation = designation,
                        company = company,
                        phone = phone,
                        email = email,
                        website = website,
                        street = street,
                        area = area,
                        city = city,
                        state = state,
                        postalCode = postalCode,
                        country = country,
                        overallConfidence = overallConfidence,
                        description = description,
                        priority = priority,
                        aiGeneratedMessage = aiGeneratedMessage
                    )
                    onSave(
                        leadToSave, 
                        onSaveSuccess, 
                        { showDuplicateDialog = true }
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save Lead")
            }
        }
    }
}

@Composable
fun EditableField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        singleLine = label != "Address"
    )
}

@Composable
fun ConfidenceBadge(confidence: Int) {
    val color = when {
        confidence >= 90 -> ConfidenceGreen
        confidence >= 70 -> ConfidenceYellow
        else -> ConfidenceRed
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color.copy(alpha = 0.2f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "AI Confidence: $confidence%",
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EditablePhoneField(
    label: String, 
    value: String, 
    name: String = "", 
    designation: String = "", 
    company: String = "", 
    aiMessage: String = "",
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    var showNumberSelector by remember { mutableStateOf(false) }
    val phoneNumbers = remember(value) {
        value.split(Regex("[,;/|]+"))
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

    fun openWhatsApp(number: String) {
        val formattedNumber = number.replace(Regex("[^0-9+]"), "")
        val message = if (aiMessage.isNotBlank()) {
            aiMessage
        } else if (name.isNotBlank() && company.isNotBlank()) {
            "Hi $name,\n\nI came across your profile" + 
            (if (designation.isNotBlank()) " as $designation" else "") + 
            " at $company. I'd love to connect and explore potential synergies between us. Let me know when you have a moment to chat!"
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        if (value.isNotBlank()) {
            IconButton(
                onClick = {
                    if (phoneNumbers.size > 1) {
                        showNumberSelector = true
                    } else if (phoneNumbers.isNotEmpty()) {
                        openWhatsApp(phoneNumbers.first())
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_whatsapp),
                    contentDescription = "WhatsApp",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
