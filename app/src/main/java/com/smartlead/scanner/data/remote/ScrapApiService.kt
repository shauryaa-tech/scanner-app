package com.smartlead.scanner.data.remote

import com.smartlead.scanner.domain.model.ScrapRequest
import com.smartlead.scanner.domain.model.ScrapResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ScrapApiService {
    @POST("scrape")
    suspend fun getScrapDetails(
        @Body request: ScrapRequest
    ): Response<ScrapResponse>

    companion object {
        const val BASE_URL = "http://10.127.251.26:5762/"
    }
}
