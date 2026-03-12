package com.smartlead.scanner.data.repository

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.smartlead.scanner.domain.model.BusinessCardData
import com.smartlead.scanner.domain.model.GeminiResult
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.domain.repository.GeminiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GeminiRepositoryImpl @Inject constructor(
    private val json: Json
) : GeminiRepository {

    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-2.0-flash",
            // Using the key found in the previous implementation
            apiKey = "AIzaSyBFmk4kHcW0TH--WYnlczj5HBB236wEsj8",
            generationConfig = generationConfig {
                temperature = 0.1f
            }
        )
    }

    override suspend fun extractBusinessCardData(bitmap: Bitmap): GeminiResult<BusinessCardData> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Extract business card details from this image and return as raw JSON.
                Fields: name, designation, company, phone, email, website, street, area, city, state, postalCode, country.
                Return ONLY the JSON object, no other text or explanation.
            """.trimIndent()

            val inputContent = content {
                image(bitmap)
                text(prompt)
            }

            val response = generativeModel.generateContent(inputContent)
            val responseText = response.text

            if (responseText.isNullOrBlank()) {
                GeminiResult.Error("Failed to extract data")
            } else {
                val cleanedJson = cleanJsonResponse(responseText)
                val data = json.decodeFromString<BusinessCardData>(cleanedJson)
                GeminiResult.Success(data)
            }
        } catch (e: Exception) {
            GeminiResult.Error(e.message ?: "Unknown error")
        }
    }

    private fun cleanJsonResponse(response: String): String {
        return response.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
    }

    override suspend fun generateAiMessage(
        lead: Lead,
        scrapData: String?
    ): GeminiResult<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Write a professional, short, and engaging WhatsApp message to reach out to this person.
                
                Lead Profile: 
                - Name: ${lead.name}
                - Designation: ${lead.designation}
                - Company: ${lead.company}
                
                ${if (!scrapData.isNullOrBlank()) "Additional Online Details:\n$scrapData" else ""}
                
                Guidelines:
                1. Start with a friendly greeting like "Hi [Name],"
                2. Mention their role at ${lead.company}.
                3. Propose connecting to explore synergies.
                4. Keep it conversational and simple.
                5. Maximum 300 characters.
                6. Give me just the message text. No subject lines or extra formatting.
            """.trimIndent()

            val response = generativeModel.generateContent(prompt)
            val responseText = response.text

            if (responseText.isNullOrBlank()) {
                GeminiResult.Error("Failed to generate AI message")
            } else {
                GeminiResult.Success(responseText.trim())
            }
        } catch (e: Exception) {
            GeminiResult.Error(e.message ?: "Unknown error")
        }
    }
}
