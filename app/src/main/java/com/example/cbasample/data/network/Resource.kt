package com.example.cbasample.data.network

sealed class Resource<T> {

    class Loading<T> : Resource<T>()
    class Loaded<T>(val data: T) : Resource<T>()
    class Failed<T>(val exception: String) : Resource<T>()

}