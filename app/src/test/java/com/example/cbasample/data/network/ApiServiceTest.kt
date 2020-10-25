package com.example.cbasample.data.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.samplemapdemo.data.network.ApiInterface
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: ApiInterface

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun testCount() {
        runBlocking {
            enqueueResponse("data.json")
            val jsonModel = service.getTransactions().body()
            assertEquals(jsonModel?.atms?.size, 2)
        }
    }

    @Test
    fun testAccountHeader() {
        runBlocking {
            enqueueResponse("data.json")
            val jsonModel = service.getTransactions().body()
            assertEquals(jsonModel?.account?.accountName, "Complete Access")
            assertEquals(jsonModel?.account?.accountNumber, "062005 1709 5888")
        }
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
            ?.getResourceAsStream(fileName)
        val source = inputStream!!.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }


}