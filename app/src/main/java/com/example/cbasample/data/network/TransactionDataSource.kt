package com.example.cbasample.data.network

import com.example.samplemapdemo.data.network.ApiInterface
import javax.inject.Inject

class TransactionDataSource @Inject constructor(
    private val apiInterface: ApiInterface
) : BaseDataSource() {
    suspend fun getTransactions() = getResult { apiInterface.getTransactions() }
}