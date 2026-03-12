package com.smartlead.scanner.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartlead.scanner.domain.model.BusinessCardData
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.domain.model.GeminiResult
import com.smartlead.scanner.domain.repository.GeminiRepository

import com.smartlead.scanner.domain.repository.LeadRepository
import com.smartlead.scanner.domain.repository.ScrapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ScannerUiState {
    object Idle : ScannerUiState()
    object Processing : ScannerUiState()
    data class Success(val data: BusinessCardData) : ScannerUiState()
    data class Error(val message: String) : ScannerUiState()
}

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val geminiRepository: GeminiRepository,
    private val leadRepository: LeadRepository,
    private val scrapRepository: ScrapRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScannerUiState>(ScannerUiState.Idle)
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _isGeneratingMessage = MutableStateFlow(false)
    val isGeneratingMessage: StateFlow<Boolean> = _isGeneratingMessage.asStateFlow()

    private var currentBitmap: Bitmap? = null

    fun processImage(bitmap: Bitmap) {
        currentBitmap = bitmap
        _uiState.value = ScannerUiState.Processing
        viewModelScope.launch {
            try {
                when (val result = geminiRepository.extractBusinessCardData(bitmap)) {
                    is GeminiResult.Success -> {
                        _uiState.value = ScannerUiState.Success(result.data)
                    }
                    is GeminiResult.Error -> {
                        _uiState.value = ScannerUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("EXTRACTION_ERROR", e.stackTraceToString())
                _uiState.value = ScannerUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun saveLead(lead: Lead, onSuccess: () -> Unit, onDuplicate: () -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val existingLead = leadRepository.getLeadByPhone(lead.phone)
                if (existingLead != null) {
                    _isSaving.value = false
                    onDuplicate()
                } else {
                    leadRepository.insertLead(lead)
                    _isSaving.value = false
                    resetState()
                    onSuccess()
                }
            } catch (e: Exception) {
                _isSaving.value = false
                android.util.Log.e("SAVE_ERROR", "Failed to save lead: ${e.message}", e)
                _uiState.value = ScannerUiState.Error("Database Error: ${e.localizedMessage ?: "Could not save lead"}")
            }
        }
    }

    fun resetState() {
        currentBitmap = null
        _uiState.value = ScannerUiState.Idle
    }

    fun generateAiMessage(lead: Lead, scrapData: String?, onResult: (String) -> Unit) {
        viewModelScope.launch {
            _isGeneratingMessage.value = true
            when (val result = geminiRepository.generateAiMessage(lead, scrapData)) {
                is GeminiResult.Success -> {
                    onResult(result.data)
                }
                is GeminiResult.Error -> {
                    // We could handle error or just skip
                }
            }
            _isGeneratingMessage.value = false
        }
    }
}
