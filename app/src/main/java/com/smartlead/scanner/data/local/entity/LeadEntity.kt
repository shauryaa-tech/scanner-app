package com.smartlead.scanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.domain.model.LeadPriority

@Entity(tableName = "leads")
data class LeadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String? = null,
    val designation: String? = null,
    val company: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val website: String? = null,
    val overallConfidence: Int = 0,
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

fun LeadEntity.toDomainModel(): Lead {
    return Lead(
        id = id,
        name = name ?: "",
        designation = designation ?: "",
        company = company ?: "",
        phone = phone ?: "",
        email = email ?: "",
        website = website ?: "",
        street = street,
        area = area,
        city = city,
        state = state,
        postalCode = postalCode,
        country = country,
        overallConfidence = overallConfidence,
        description = description,
        priority = priority,
        timestamp = timestamp,
        linkedIn = linkedIn,
        scrapedCompanyStatus = scrapedCompanyStatus,
        companyCategory = companyCategory,
        aiGeneratedMessage = aiGeneratedMessage,
        scrapedDataJson = scrapedDataJson
    )
}

fun Lead.toEntity(): LeadEntity {
    return LeadEntity(
        id = id,
        name = name,
        designation = designation,
        company = company,
        phone = phone,
        email = email,
        website = website,
        street = street,
        area = area,
        city = city,
        state = state,
        postalCode = postalCode,
        country = country,
        overallConfidence = overallConfidence,
        description = description,
        priority = priority,
        timestamp = timestamp,
        linkedIn = linkedIn,
        scrapedCompanyStatus = scrapedCompanyStatus,
        companyCategory = companyCategory,
        aiGeneratedMessage = aiGeneratedMessage,
        scrapedDataJson = scrapedDataJson
    )
}
