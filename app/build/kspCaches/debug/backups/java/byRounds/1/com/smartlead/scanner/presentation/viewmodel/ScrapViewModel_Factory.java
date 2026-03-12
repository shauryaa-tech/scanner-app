package com.smartlead.scanner.presentation.viewmodel;

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
public final class ScrapViewModel_Factory implements Factory<ScrapViewModel> {
  private final Provider<ScrapRepository> scrapRepositoryProvider;

  private final Provider<LeadRepository> leadRepositoryProvider;

  private final Provider<Json> jsonProvider;

  public ScrapViewModel_Factory(Provider<ScrapRepository> scrapRepositoryProvider,
      Provider<LeadRepository> leadRepositoryProvider, Provider<Json> jsonProvider) {
    this.scrapRepositoryProvider = scrapRepositoryProvider;
    this.leadRepositoryProvider = leadRepositoryProvider;
    this.jsonProvider = jsonProvider;
  }

  @Override
  public ScrapViewModel get() {
    return newInstance(scrapRepositoryProvider.get(), leadRepositoryProvider.get(), jsonProvider.get());
  }

  public static ScrapViewModel_Factory create(Provider<ScrapRepository> scrapRepositoryProvider,
      Provider<LeadRepository> leadRepositoryProvider, Provider<Json> jsonProvider) {
    return new ScrapViewModel_Factory(scrapRepositoryProvider, leadRepositoryProvider, jsonProvider);
  }

  public static ScrapViewModel newInstance(ScrapRepository scrapRepository,
      LeadRepository leadRepository, Json json) {
    return new ScrapViewModel(scrapRepository, leadRepository, json);
  }
}
