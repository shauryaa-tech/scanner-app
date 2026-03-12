package com.smartlead.scanner.data.repository;

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
public final class GeminiRepositoryImpl_Factory implements Factory<GeminiRepositoryImpl> {
  private final Provider<Json> jsonProvider;

  public GeminiRepositoryImpl_Factory(Provider<Json> jsonProvider) {
    this.jsonProvider = jsonProvider;
  }

  @Override
  public GeminiRepositoryImpl get() {
    return newInstance(jsonProvider.get());
  }

  public static GeminiRepositoryImpl_Factory create(Provider<Json> jsonProvider) {
    return new GeminiRepositoryImpl_Factory(jsonProvider);
  }

  public static GeminiRepositoryImpl newInstance(Json json) {
    return new GeminiRepositoryImpl(json);
  }
}
