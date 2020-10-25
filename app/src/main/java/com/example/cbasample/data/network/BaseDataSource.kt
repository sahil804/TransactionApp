package com.example.cbasample.data.network

import android.util.Log
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            Log.d("Sahil","getResult $response")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.Loaded(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            Log.d("Sahil","getResult --->"+e.message)
            return Resource.Failed("Check Internet connection !!!")
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Log.e("remoteDataSource", message)
        return Resource.Failed("Network call has failed for a following reason: $message")
    }
}