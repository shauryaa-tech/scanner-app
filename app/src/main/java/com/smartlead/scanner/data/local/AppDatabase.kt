package com.smartlead.scanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smartlead.scanner.data.local.dao.LeadDao
import com.smartlead.scanner.data.local.entity.LeadEntity

@Database(entities = [LeadEntity::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val leadDao: LeadDao
    
    companion object {
        const val DATABASE_NAME = "smartlead_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE leads ADD COLUMN street TEXT")
                db.execSQL("ALTER TABLE leads ADD COLUMN area TEXT")
                db.execSQL("ALTER TABLE leads ADD COLUMN city TEXT")
                db.execSQL("ALTER TABLE leads ADD COLUMN state TEXT")
                db.execSQL("ALTER TABLE leads ADD COLUMN postalCode TEXT")
                db.execSQL("ALTER TABLE leads ADD COLUMN country TEXT")
                db.execSQL("ALTER TABLE leads ADD COLUMN description TEXT")
                db.execSQL("ALTER TABLE leads ADD COLUMN priority TEXT NOT NULL DEFAULT 'COLD'")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Version 2 to 3 migration: fields were reordered in the entity class.
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE leads ADD COLUMN linkedIn TEXT")
                db.execSQL("ALTER TABLE leads ADD COLUMN scrapedCompanyStatus TEXT")
                db.execSQL("ALTER TABLE leads ADD COLUMN companyCategory TEXT")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE leads ADD COLUMN aiGeneratedMessage TEXT")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE leads ADD COLUMN scrapedDataJson TEXT")
            }
        }
    }
}
