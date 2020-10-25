package com.example.cbasample

import com.example.cbasample.di.DaggerTestAppComponent
import com.example.cbasample.di.TestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class CbaSampleAppTest : CbaSampleApp()  {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerTestAppComponent.builder().create(this) as TestAppComponent
        return component
    }
}