package com.smartlead.scanner.domain.repository

import com.smartlead.scanner.domain.model.ScrapResponse

interface ScrapRepository {
    suspend fun getScrapDetails(personName: String, companyName: String): Result<ScrapResponse?>
}
