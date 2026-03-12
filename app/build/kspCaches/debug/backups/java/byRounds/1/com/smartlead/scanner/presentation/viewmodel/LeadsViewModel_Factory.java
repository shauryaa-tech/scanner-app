package com.smartlead.scanner.presentation.viewmodel;

import com.smartlead.scanner.domain.repository.GeminiRepository;
import com.smartlead.scanner.domain.repository.LeadRepository;
import com.smartlead.scanner.domain.repository.ScrapRepository;
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
public final class LeadsViewModel_Factory implements Factory<LeadsViewModel> {
  private final Provider<LeadRepository> repositoryProvider;

  private final Provider<GeminiRepository> geminiRepositoryProvider;

  private final Provider<ScrapRepository> scrapRepositoryProvider;

  private final Provider<Json> jsonProvider;

  public LeadsViewModel_Factory(Provider<LeadRepository> repositoryProvider,
      Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<ScrapRepository> scrapRepositoryProvider, Provider<Json> jsonProvider) {
    this.repositoryProvider = repositoryProvider;
    this.geminiRepositoryProvider = geminiRepositoryProvider;
    this.scrapRepositoryProvider = scrapRepositoryProvider;
    this.jsonProvider = jsonProvider;
  }

  @Override
  public LeadsViewModel get() {
    return newInstance(repositoryProvider.get(), geminiRepositoryProvider.get(), scrapRepositoryProvider.get(), jsonProvider.get());
  }

  public static LeadsViewModel_Factory create(Provider<LeadRepository> repositoryProvider,
      Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<ScrapRepository> scrapRepositoryProvider, Provider<Json> jsonProvider) {
    return new LeadsViewModel_Factory(repositoryProvider, geminiRepositoryProvider, scrapRepositoryProvider, jsonProvider);
  }

  public static LeadsViewModel newInstance(LeadRepository repository,
      GeminiRepository geminiRepository, ScrapRepository scrapRepository, Json json) {
    return new LeadsViewModel(repository, geminiRepository, scrapRepository, json);
  }
}
