package com.example.cbasample.di

import com.example.cbasample.CbaSampleApp
import com.example.samplemapdemo.di.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, AppModule::class, AndroidModule::class, ViewModelModule::class]
)
interface TestAppComponent: AppComponent, AndroidInjector<CbaSampleApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<CbaSampleApp>()
}