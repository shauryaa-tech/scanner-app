package com.smartlead.scanner.domain.model

data class Lead(
    val id: Int = 0,
    val name: String,
    val designation: String,
    val company: String,
    val phone: String,
    val email: String,
    val website: String,
    val overallConfidence: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val street: String? = null,
    val area: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
    val description: String? = null,
    val priority: LeadPriority = LeadPriority.JustLooking,
    val linkedIn: String? = null,
    val scrapedCompanyStatus: String? = null,
    val companyCategory: String? = null,
    val aiGeneratedMessage: String? = null,
    val scrapedDataJson: String? = null
)
