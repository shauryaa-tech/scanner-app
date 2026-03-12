package com.smartlead.scanner.di

import android.app.Application
import androidx.room.Room
import com.smartlead.scanner.BuildConfig
import com.smartlead.scanner.data.local.AppDatabase
import com.smartlead.scanner.data.repository.GeminiRepositoryImpl
import com.smartlead.scanner.data.repository.LeadRepositoryImpl
import com.smartlead.scanner.domain.repository.GeminiRepository
import com.smartlead.scanner.domain.repository.LeadRepository
import com.smartlead.scanner.data.local.dao.LeadDao
import com.smartlead.scanner.data.remote.PostalApiService
import com.smartlead.scanner.data.remote.ScrapApiService
import com.smartlead.scanner.data.repository.ScrapRepositoryImpl
import com.smartlead.scanner.domain.repository.ScrapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).addMigrations(
            AppDatabase.MIGRATION_1_2, 
            AppDatabase.MIGRATION_2_3, 
            AppDatabase.MIGRATION_3_4,
            AppDatabase.MIGRATION_4_5,
            AppDatabase.MIGRATION_5_6
        ).fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideLeadDao(db: AppDatabase): LeadDao {
        return db.leadDao
    }

    @Provides
    @Singleton
    fun provideLeadRepository(
        leadDao: LeadDao,
        geminiRepository: GeminiRepository,
        postalApiService: PostalApiService
    ): LeadRepository {
        return LeadRepositoryImpl(
            leadDao,
            geminiRepository,
            postalApiService
        )
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun providePostalApiService(
        client: OkHttpClient
    ): PostalApiService {
        return Retrofit.Builder()
            .baseUrl(PostalApiService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostalApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideScrapApiService(
        client: OkHttpClient,
        json: Json
    ): ScrapApiService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(ScrapApiService.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ScrapApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideScrapRepository(
        json: Json,
        apiService: ScrapApiService
    ): ScrapRepository {
        return ScrapRepositoryImpl(json, apiService)
    }

    @Provides
    @Singleton
    fun provideGeminiRepository(
        json: Json
    ): GeminiRepository {
        return GeminiRepositoryImpl(json)
    }
}
