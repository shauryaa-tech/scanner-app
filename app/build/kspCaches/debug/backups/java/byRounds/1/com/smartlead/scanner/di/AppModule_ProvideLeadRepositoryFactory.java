package com.smartlead.scanner.di;

import com.smartlead.scanner.data.local.dao.LeadDao;
import com.smartlead.scanner.data.remote.PostalApiService;
import com.smartlead.scanner.domain.repository.GeminiRepository;
import com.smartlead.scanner.domain.repository.LeadRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AppModule_ProvideLeadRepositoryFactory implements Factory<LeadRepository> {
  private final Provider<LeadDao> leadDaoProvider;

  private final Provider<GeminiRepository> geminiRepositoryProvider;

  private final Provider<PostalApiService> postalApiServiceProvider;

  public AppModule_ProvideLeadRepositoryFactory(Provider<LeadDao> leadDaoProvider,
      Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<PostalApiService> postalApiServiceProvider) {
    this.leadDaoProvider = leadDaoProvider;
    this.geminiRepositoryProvider = geminiRepositoryProvider;
    this.postalApiServiceProvider = postalApiServiceProvider;
  }

  @Override
  public LeadRepository get() {
    return provideLeadRepository(leadDaoProvider.get(), geminiRepositoryProvider.get(), postalApiServiceProvider.get());
  }

  public static AppModule_ProvideLeadRepositoryFactory create(Provider<LeadDao> leadDaoProvider,
      Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<PostalApiService> postalApiServiceProvider) {
    return new AppModule_ProvideLeadRepositoryFactory(leadDaoProvider, geminiRepositoryProvider, postalApiServiceProvider);
  }

  public static LeadRepository provideLeadRepository(LeadDao leadDao,
      GeminiRepository geminiRepository, PostalApiService postalApiService) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideLeadRepository(leadDao, geminiRepository, postalApiService));
  }
}
