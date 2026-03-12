package com.smartlead.scanner.di;

import com.smartlead.scanner.data.remote.ScrapApiService;
import com.smartlead.scanner.domain.repository.ScrapRepository;
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
public final class AppModule_ProvideScrapRepositoryFactory implements Factory<ScrapRepository> {
  private final Provider<Json> jsonProvider;

  private final Provider<ScrapApiService> apiServiceProvider;

  public AppModule_ProvideScrapRepositoryFactory(Provider<Json> jsonProvider,
      Provider<ScrapApiService> apiServiceProvider) {
    this.jsonProvider = jsonProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public ScrapRepository get() {
    return provideScrapRepository(jsonProvider.get(), apiServiceProvider.get());
  }

  public static AppModule_ProvideScrapRepositoryFactory create(Provider<Json> jsonProvider,
      Provider<ScrapApiService> apiServiceProvider) {
    return new AppModule_ProvideScrapRepositoryFactory(jsonProvider, apiServiceProvider);
  }

  public static ScrapRepository provideScrapRepository(Json json, ScrapApiService apiService) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideScrapRepository(json, apiService));
  }
}
