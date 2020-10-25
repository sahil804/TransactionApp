package com.example.cbasample.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.cbasample.CoroutinesTestRule
import com.example.cbasample.MySampleRunner
import com.example.cbasample.data.model.TransactionListItem
import com.example.cbasample.data.model.TransactionResponseData
import com.example.cbasample.data.repository.TransactionRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Observer

@RunWith(MySampleRunner::class)
class TransactionListViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    lateinit var transactionResponseData: TransactionResponseData

    lateinit var viewModel: TransactionListViewModel

    @Mock
    lateinit var observer: androidx.lifecycle.Observer<List<TransactionListItem>>


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = TransactionListViewModel(transactionRepository)
        transactionResponseData = mockk<TransactionResponseData>(relaxed = true)
    }

    @Test
    fun testRepositoryFunctions() {
        runBlocking {
            viewModel.getTransactions()
            verify(transactionRepository).getTransactions()
        }
    }

//    @Test
//    fun testComputeTransactionsDetails() {
//        //runBlockingTest {
//            viewModel.accountDetails.observeForever { observer }
//            viewModel.computeTransactionsDetails(transactionResponseData)
//            verify(observer).onChanged(any())
//        //}
//    }
}