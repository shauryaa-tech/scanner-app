package com.smartlead.scanner.data.repository;

import com.smartlead.scanner.data.remote.ScrapApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.serialization.json.Json;

@ScopeMetadata
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
public final class ScrapRepositoryImpl_Factory implements Factory<ScrapRepositoryImpl> {
  private final Provider<Json> jsonProvider;

  private final Provider<ScrapApiService> apiServiceProvider;

  public ScrapRepositoryImpl_Factory(Provider<Json> jsonProvider,
      Provider<ScrapApiService> apiServiceProvider) {
    this.jsonProvider = jsonProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public ScrapRepositoryImpl get() {
    return newInstance(jsonProvider.get(), apiServiceProvider.get());
  }

  public static ScrapRepositoryImpl_Factory create(Provider<Json> jsonProvider,
      Provider<ScrapApiService> apiServiceProvider) {
    return new ScrapRepositoryImpl_Factory(jsonProvider, apiServiceProvider);
  }

  public static ScrapRepositoryImpl newInstance(Json json, ScrapApiService apiService) {
    return new ScrapRepositoryImpl(json, apiService);
  }
}
