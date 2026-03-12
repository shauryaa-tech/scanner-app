package com.smartlead.scanner.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessCardData(
    val name: String? = null,
    val designation: String? = null,
    val company: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val website: String? = null,
    val street: String? = null,
    val area: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
    val description: String? = null,
    val priority: LeadPriority = LeadPriority.JustLooking
) {
    fun getOverallConfidence(): Int = 100
}
