package com.example.samplemapdemo.di

import androidx.lifecycle.ViewModel
import com.example.cbasample.ui.TransactionListViewModel
import com.example.cbasample.ui.FindUsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TransactionListViewModel::class)
    abstract fun bindaccountDetailsVM(transactionListViewModel: TransactionListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FindUsViewModel::class)
    abstract fun bindFindUsVM(findUsViewModel: FindUsViewModel): ViewModel
}