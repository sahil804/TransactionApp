package com.example.cbasample

import com.example.cbasample.di.AppComponent
import com.example.cbasample.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

open class CbaSampleApp : DaggerApplication() {
    lateinit var component: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerAppComponent.builder().create(this) as AppComponent
        return component
    }
}