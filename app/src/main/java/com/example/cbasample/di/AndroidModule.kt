package com.example.cbasample.di

import com.example.cbasample.MainActivity
import com.example.cbasample.ui.TransactionListFragment
import com.example.cbasample.ui.FindUsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindAccountDetailsFragment(): TransactionListFragment

    @ContributesAndroidInjector
    internal abstract fun bindFindUsFragment(): FindUsFragment
}