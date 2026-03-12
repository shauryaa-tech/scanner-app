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
public final class ScannerViewModel_Factory implements Factory<ScannerViewModel> {
  private final Provider<GeminiRepository> geminiRepositoryProvider;

  private final Provider<LeadRepository> leadRepositoryProvider;

  private final Provider<ScrapRepository> scrapRepositoryProvider;

  public ScannerViewModel_Factory(Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<LeadRepository> leadRepositoryProvider,
      Provider<ScrapRepository> scrapRepositoryProvider) {
    this.geminiRepositoryProvider = geminiRepositoryProvider;
    this.leadRepositoryProvider = leadRepositoryProvider;
    this.scrapRepositoryProvider = scrapRepositoryProvider;
  }

  @Override
  public ScannerViewModel get() {
    return newInstance(geminiRepositoryProvider.get(), leadRepositoryProvider.get(), scrapRepositoryProvider.get());
  }

  public static ScannerViewModel_Factory create(Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<LeadRepository> leadRepositoryProvider,
      Provider<ScrapRepository> scrapRepositoryProvider) {
    return new ScannerViewModel_Factory(geminiRepositoryProvider, leadRepositoryProvider, scrapRepositoryProvider);
  }

  public static ScannerViewModel newInstance(GeminiRepository geminiRepository,
      LeadRepository leadRepository, ScrapRepository scrapRepository) {
    return new ScannerViewModel(geminiRepository, leadRepository, scrapRepository);
  }
}
