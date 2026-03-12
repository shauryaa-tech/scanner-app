package com.smartlead.scanner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.domain.model.GeminiResult
import com.smartlead.scanner.domain.model.ScrapResponse
import com.smartlead.scanner.domain.repository.GeminiRepository
import com.smartlead.scanner.domain.repository.LeadRepository
import com.smartlead.scanner.domain.repository.ScrapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LeadsViewModel @Inject constructor(
    private val repository: LeadRepository,
    private val geminiRepository: GeminiRepository,
    private val scrapRepository: ScrapRepository,
    private val json: Json
) : ViewModel() {

    private val _scrapingIds = MutableStateFlow<Set<Int>>(emptySet())
    val scrapingIds: StateFlow<Set<Int>> = _scrapingIds.asStateFlow()

    fun scrapLead(leadId: Int) {
        viewModelScope.launch {
            if (_scrapingIds.value.contains(leadId)) return@launch
            _scrapingIds.value = _scrapingIds.value + leadId
            
            val lead = repository.getLeadById(leadId)
            if (lead != null) {
                scrapRepository.getScrapDetails(lead.name, lead.company).onSuccess { response ->
                    if (response != null) {
                        val updatedLead = lead.copy(
                            scrapedDataJson = json.encodeToString(ScrapResponse.serializer(), response),
                            scrapedCompanyStatus = response.status,
                            companyCategory = response.headline
                        )
                        repository.updateLead(updatedLead)
                    }
                }
            }
            
            _scrapingIds.value = _scrapingIds.value - leadId
        }
    }

    val leads: StateFlow<List<Lead>> = repository.getAllLeads()
        .catch { e ->
            android.util.Log.e("LEADS_FLOW_ERROR", "Error in leads flow", e)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun deleteLead(lead: Lead) {
        viewModelScope.launch {
            try {
                repository.deleteLead(lead)
            } catch (e: Exception) {
                android.util.Log.e("DELETE_ERROR", "Failed to delete lead", e)
            }
        }
    }

    fun updateLead(lead: Lead) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                repository.updateLead(lead)
            } catch (e: Exception) {
                android.util.Log.e("UPDATE_ERROR", "Failed to update lead", e)
            }
            _isSaving.value = false
        }
    }

    fun getLeadById(id: Int): Flow<Lead?> {
        return repository.getLeadByIdFlow(id)
    }

    private val _isGeneratingMessage = MutableStateFlow(false)
    val isGeneratingMessage: StateFlow<Boolean> = _isGeneratingMessage.asStateFlow()

    fun generateAiMessage(lead: Lead, scrapData: String?) {
        viewModelScope.launch {
            _isGeneratingMessage.value = true
            when (val result = geminiRepository.generateAiMessage(lead, scrapData)) {
                is GeminiResult.Success -> {
                    updateLead(lead.copy(aiGeneratedMessage = result.data))
                }
                is GeminiResult.Error -> {
                    // Handle error if needed
                }
            }
            _isGeneratingMessage.value = false
        }
    }
}
