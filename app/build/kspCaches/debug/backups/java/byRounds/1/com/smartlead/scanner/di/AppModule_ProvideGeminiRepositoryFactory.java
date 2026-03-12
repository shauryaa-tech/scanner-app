package com.smartlead.scanner.di;

import com.smartlead.scanner.domain.repository.GeminiRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.serialization.json.Json;

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
public final class AppModule_ProvideGeminiRepositoryFactory implements Factory<GeminiRepository> {
  private final Provider<Json> jsonProvider;

  public AppModule_ProvideGeminiRepositoryFactory(Provider<Json> jsonProvider) {
    this.jsonProvider = jsonProvider;
  }

  @Override
  public GeminiRepository get() {
    return provideGeminiRepository(jsonProvider.get());
  }

  public static AppModule_ProvideGeminiRepositoryFactory create(Provider<Json> jsonProvider) {
    return new AppModule_ProvideGeminiRepositoryFactory(jsonProvider);
  }

  public static GeminiRepository provideGeminiRepository(Json json) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGeminiRepository(json));
  }
}
