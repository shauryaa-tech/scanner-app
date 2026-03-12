package com.smartlead.scanner.di;

import com.smartlead.scanner.data.remote.PostalApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
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
public final class AppModule_ProvidePostalApiServiceFactory implements Factory<PostalApiService> {
  private final Provider<OkHttpClient> clientProvider;

  public AppModule_ProvidePostalApiServiceFactory(Provider<OkHttpClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public PostalApiService get() {
    return providePostalApiService(clientProvider.get());
  }

  public static AppModule_ProvidePostalApiServiceFactory create(
      Provider<OkHttpClient> clientProvider) {
    return new AppModule_ProvidePostalApiServiceFactory(clientProvider);
  }

  public static PostalApiService providePostalApiService(OkHttpClient client) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePostalApiService(client));
  }
}
