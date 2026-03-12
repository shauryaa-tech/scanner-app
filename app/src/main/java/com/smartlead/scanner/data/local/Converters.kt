package com.smartlead.scanner.data.local

import androidx.room.TypeConverter
import com.smartlead.scanner.domain.model.LeadPriority

class Converters {
    @TypeConverter
    fun fromPriority(value: LeadPriority): String = value.name

    @TypeConverter
    fun toPriority(value: String?): LeadPriority {
        return try {
            if (value.isNullOrBlank()) return LeadPriority.JustLooking
            
            when {
                value.equals("Ready", ignoreCase = true) || value.equals("Hot", ignoreCase = true) -> LeadPriority.WantToBuy
                value.equals("Considering", ignoreCase = true) || value.equals("Warm", ignoreCase = true) -> LeadPriority.Interested
                value.equals("Exploring", ignoreCase = true) || value.equals("Cold", ignoreCase = true) -> LeadPriority.JustLooking
                else -> {
                    try {
                        LeadPriority.valueOf(value)
                    } catch (e: Exception) {
                        LeadPriority.JustLooking
                    }
                }
            }
        } catch (e: Exception) {
            LeadPriority.JustLooking
        }
    }
}
