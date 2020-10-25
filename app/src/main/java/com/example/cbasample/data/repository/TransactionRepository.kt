package com.example.cbasample.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.cbasample.data.model.TransactionResponseData
import com.example.cbasample.data.network.Resource
import com.example.cbasample.data.network.TransactionDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepository @Inject constructor(private var transactionDataSource: TransactionDataSource){

    val transactionsLiveData = MutableLiveData<Resource<TransactionResponseData>>()

    suspend fun getTransactions() {
        return withContext(Dispatchers.IO) {
            transactionsLiveData.postValue(Resource.Loading())
            transactionsLiveData.postValue(transactionDataSource.getTransactions())
        }
    }
}