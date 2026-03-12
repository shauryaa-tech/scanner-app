package com.smartlead.scanner.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ScrapResponse(
    @SerialName("name") val name: String? = null,
    @SerialName("headline") val headline: String? = null,
    @SerialName("about_summary") val aboutSummary: String? = null,
    @SerialName("current_role") val currentRole: Role? = null,
    @SerialName("previous_role") val previousRole: Role? = null,
    @SerialName("company_size") val companySize: String? = null,
    @SerialName("team_size") val teamSize: String? = null,
    @SerialName("company_website") val companyWebsite: String? = null,
    @SerialName("company_linkedin_url") val companyLinkedinUrl: String? = null,
    @SerialName("linkedin_url") val linkedinUrl: String? = null,
    @SerialName("status") val status: String? = null
)

@Serializable
data class Role(
    @SerialName("designation") val designation: String? = null,
    @SerialName("company_name") val companyName: String? = null,
    @SerialName("tenure_duration") val tenureDuration: String? = null
)
