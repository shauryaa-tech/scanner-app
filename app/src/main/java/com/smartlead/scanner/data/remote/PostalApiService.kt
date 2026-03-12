package com.smartlead.scanner.data.remote

import com.smartlead.scanner.data.remote.model.PostalResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PostalApiService {
    @GET("pincode/{pin}")
    suspend fun getIndianPostal(
        @Path("pin") pin: String
    ): List<PostalResponse>

    companion object {
        const val BASE_URL = "https://api.postalpincode.in/"
    }
}
