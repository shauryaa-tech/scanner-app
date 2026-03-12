package com.smartlead.scanner.domain.repository

import android.graphics.Bitmap
import com.smartlead.scanner.domain.model.BusinessCardData
import com.smartlead.scanner.domain.model.GeminiResult
import com.smartlead.scanner.domain.model.Lead

interface GeminiRepository {
    suspend fun generateAiMessage(lead: Lead, scrapData: String?): GeminiResult<String>
    suspend fun extractBusinessCardData(bitmap: Bitmap): GeminiResult<BusinessCardData>
}
