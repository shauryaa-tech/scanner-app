package com.smartlead.scanner.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ScrapRequest(
    val person_name: String,
    val company_name: String
)
