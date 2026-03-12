package com.smartlead.scanner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartlead.scanner.domain.model.ScrapResponse
import com.smartlead.scanner.domain.repository.LeadRepository
import com.smartlead.scanner.domain.repository.ScrapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ScrapViewModel @Inject constructor(
    private val scrapRepository: ScrapRepository,
    private val leadRepository: LeadRepository,
    private val json: Json
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScrapUiState>(ScrapUiState.Initial)
    val uiState: StateFlow<ScrapUiState> = _uiState.asStateFlow()

    fun fetchScrapDetails(leadId: Int, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            val lead = leadRepository.getLeadById(leadId) ?: run {
                _uiState.value = ScrapUiState.Error("Lead not found")
                return@launch
            }

            // Check for cached data
            if (!forceRefresh && !lead.scrapedDataJson.isNullOrBlank()) {
                try {
                    val cachedResponse = json.decodeFromString<ScrapResponse>(lead.scrapedDataJson)
                    _uiState.value = ScrapUiState.Success(
                        personName = lead.name,
                        companyName = lead.company,
                        response = cachedResponse
                    )
                    return@launch
                } catch (e: Exception) {
                    // If decoding fails, proceed to fetch fresh data
                }
            }

            _uiState.value = ScrapUiState.Loading
            
            val result = scrapRepository.getScrapDetails(lead.name, lead.company)
            result.onSuccess { response ->
                // Save to database
                if (response != null) {
                    val updatedLead = lead.copy(
                        scrapedDataJson = json.encodeToString(ScrapResponse.serializer(), response),
                        scrapedCompanyStatus = response.status,
                        companyCategory = response.headline // Or other relevant mapping
                    )
                    leadRepository.updateLead(updatedLead)
                }

                _uiState.value = ScrapUiState.Success(
                    personName = lead.name,
                    companyName = lead.company,
                    response = response
                )
            }.onFailure { error ->
                _uiState.value = ScrapUiState.Error(error.message ?: "Unknown error occurred")
            }
        }
    }

    fun refreshScrapDetails(leadId: Int) {
        fetchScrapDetails(leadId, forceRefresh = true)
    }
}

sealed class ScrapUiState {
    object Initial : ScrapUiState()
    object Loading : ScrapUiState()
    data class Success(
        val personName: String,
        val companyName: String,
        val response: ScrapResponse?
    ) : ScrapUiState()
    data class Error(val message: String) : ScrapUiState()
}
