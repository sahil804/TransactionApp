package com.example.samplemapdemo.data.network

import com.example.cbasample.data.model.TransactionResponseData
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    /**
     * Get the list of the transactions from the API
     */
    @GET("/s/tewg9b71x0wrou9/data.json?dl=1")
    suspend fun getTransactions(): Response<TransactionResponseData>
}