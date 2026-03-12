package com.smartlead.scanner.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.smartlead.scanner.data.remote.ScrapApiService
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.domain.model.ScrapRequest
import com.smartlead.scanner.domain.model.ScrapResponse
import com.smartlead.scanner.domain.repository.ScrapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ScrapRepositoryImpl @Inject constructor(
    private val json: Json,
    private val apiService: ScrapApiService
) : ScrapRepository {

    override suspend fun getScrapDetails(
        personName: String,
        companyName: String
    ): Result<ScrapResponse?> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getScrapDetails(
                ScrapRequest(personName, companyName)
            )
            
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Failed to fetch scrap details: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}