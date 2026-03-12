package com.smartlead.scanner.di;

import com.smartlead.scanner.data.local.AppDatabase;
import com.smartlead.scanner.data.local.dao.LeadDao;
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
public final class AppModule_ProvideLeadDaoFactory implements Factory<LeadDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideLeadDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public LeadDao get() {
    return provideLeadDao(dbProvider.get());
  }

  public static AppModule_ProvideLeadDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideLeadDaoFactory(dbProvider);
  }

  public static LeadDao provideLeadDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideLeadDao(db));
  }
}
