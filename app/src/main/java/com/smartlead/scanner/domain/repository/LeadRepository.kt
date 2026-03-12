package com.smartlead.scanner.domain.repository

import com.smartlead.scanner.domain.model.Lead
import kotlinx.coroutines.flow.Flow

interface LeadRepository {
    fun getAllLeads(): Flow<List<Lead>>
    fun getLeadByIdFlow(id: Int): Flow<Lead?>
    suspend fun getLeadById(id: Int): Lead?
    suspend fun getLeadByPhone(phone: String): Lead?
    suspend fun insertLead(lead: Lead): Long
    suspend fun updateLead(lead: Lead)
    suspend fun deleteLead(lead: Lead)
}
