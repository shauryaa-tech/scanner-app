package com.smartlead.scanner.di;

import com.smartlead.scanner.data.remote.ScrapApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.serialization.json.Json;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AppModule_ProvideScrapApiServiceFactory implements Factory<ScrapApiService> {
  private final Provider<OkHttpClient> clientProvider;

  private final Provider<Json> jsonProvider;

  public AppModule_ProvideScrapApiServiceFactory(Provider<OkHttpClient> clientProvider,
      Provider<Json> jsonProvider) {
    this.clientProvider = clientProvider;
    this.jsonProvider = jsonProvider;
  }

  @Override
  public ScrapApiService get() {
    return provideScrapApiService(clientProvider.get(), jsonProvider.get());
  }

  public static AppModule_ProvideScrapApiServiceFactory create(
      Provider<OkHttpClient> clientProvider, Provider<Json> jsonProvider) {
    return new AppModule_ProvideScrapApiServiceFactory(clientProvider, jsonProvider);
  }

  public static ScrapApiService provideScrapApiService(OkHttpClient client, Json json) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideScrapApiService(client, json));
  }
}
