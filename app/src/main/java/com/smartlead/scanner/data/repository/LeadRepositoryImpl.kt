package com.smartlead.scanner.data.repository

import com.smartlead.scanner.data.local.dao.LeadDao
import com.smartlead.scanner.data.local.entity.toDomainModel
import com.smartlead.scanner.data.local.entity.toEntity
import com.smartlead.scanner.data.remote.PostalApiService
import com.smartlead.scanner.domain.model.Lead
import com.smartlead.scanner.domain.repository.GeminiRepository
import com.smartlead.scanner.domain.repository.LeadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LeadRepositoryImpl(
    private val dao: LeadDao,
    private val geminiRepository: GeminiRepository,
    private val postalApiService: PostalApiService
) : LeadRepository {

    private suspend fun autoFillLeadAddress(lead: Lead): Lead {
        if (lead.state.isNullOrBlank() || lead.country.isNullOrBlank()) {
            if (!lead.postalCode.isNullOrBlank()) {
                return try {
                    val response = postalApiService.getIndianPostal(lead.postalCode)
                    val postOffice = response.firstOrNull()?.postOffice?.firstOrNull()
                    if (postOffice != null) {
                        lead.copy(
                            state = if (lead.state.isNullOrBlank()) postOffice.state else lead.state,
                            country = if (lead.country.isNullOrBlank()) postOffice.country else lead.country
                        )
                    } else {
                        lead
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    lead
                }
            }
        }
        return lead
    }

    override fun getAllLeads(): Flow<List<Lead>> {
        return dao.getAllLeads().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getLeadByIdFlow(id: Int): Flow<Lead?> {
        return dao.getLeadByIdFlow(id).map { it?.toDomainModel() }
    }

    override suspend fun getLeadById(id: Int): Lead? {
        return dao.getLeadById(id)?.toDomainModel()
    }

    override suspend fun getLeadByPhone(phone: String): Lead? {
        return dao.getLeadByPhone(phone)?.toDomainModel()
    }

    override suspend fun insertLead(lead: Lead): Long {
        val filledLead = autoFillLeadAddress(lead)
        return dao.insertLead(filledLead.toEntity())
    }

    override suspend fun updateLead(lead: Lead) {
        val filledLead = autoFillLeadAddress(lead)
        dao.updateLead(filledLead.toEntity())
    }

    override suspend fun deleteLead(lead: Lead) {
        dao.deleteLead(lead.toEntity())
    }
}
