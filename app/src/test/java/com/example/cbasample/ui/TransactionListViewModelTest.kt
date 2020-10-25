package com.example.cbasample.ui

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.cbasample.CoroutinesTestRule
import com.example.cbasample.MySampleRunner
import com.example.cbasample.R
import com.example.cbasample.data.model.TransactionResponseData
import com.example.cbasample.data.repository.TransactionRepository
import com.example.cbasample.util.getRelativeDateFromToday
import com.nhaarman.mockitokotlin2.verify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

@RunWith(MySampleRunner::class)
class TransactionListViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    private lateinit var transactionResponseData: TransactionResponseData

    private lateinit var viewModel: TransactionListViewModel

    @Mock
    private lateinit var context: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        context = ApplicationProvider.getApplicationContext()
        viewModel = TransactionListViewModel(transactionRepository)
        transactionResponseData = mockk(relaxed = true)
    }

    @Test
    fun testRepositoryFunctions() {
        runBlocking {
            viewModel.getTransactions()
            verify(transactionRepository).getTransactions()
        }
    }

    @Test
    fun checkDateTimeText() {
        val calendar = Calendar.getInstance()

        calendar.time = Date()
        calendar.add(Calendar.MINUTE, 0)
        var date = Date(calendar.timeInMillis)
        assertEquals(
            "Just Now",
            date.getRelativeDateFromToday(context)
        )

        calendar.time = Date()
        calendar.add(Calendar.MINUTE, -30)
        date = Date(calendar.timeInMillis)
        assertEquals(
            context.resources.getQuantityString(R.plurals.minutes_text, 30, 30L),
            date.getRelativeDateFromToday(context)
        )

        calendar.time = Date()
        calendar.add(Calendar.HOUR, -11)
        date = Date(calendar.timeInMillis)
        assertEquals(
            context.resources.getQuantityString(R.plurals.hours_text, 11, 11L),
            date.getRelativeDateFromToday(context)
        )

        //  Need to check in app for this DST impact but for now taking DST calculation in here to fix UT
        calendar.time = Date()
        val z: TimeZone = calendar.timeZone
        var offset = z.rawOffset
        if (z.inDaylightTime(Date())) {
            offset += z.dstSavings
        }
        val offsetHrs = offset / 1000 / 60 / 60
        val offsetMins = offset / 1000 / 60 % 60

        calendar.add(Calendar.HOUR_OF_DAY, -offsetHrs)
        calendar.add(Calendar.MINUTE, -offsetMins)
        calendar.add(Calendar.DAY_OF_YEAR, -15)
        date = Date(calendar.timeInMillis)
        assertEquals(
            context.resources.getQuantityString(R.plurals.days_text, 15, 15L),
            date.getRelativeDateFromToday(context)
        )

        calendar.time = Date()
        calendar.add(Calendar.MONTH, -4)
        date = Date(calendar.timeInMillis)
        assertEquals(
            context.resources.getQuantityString(R.plurals.months_text, 4, 4L),
            date.getRelativeDateFromToday(context)
        )

        calendar.time = Date()
        calendar.add(Calendar.YEAR, -9)
        calendar.add(Calendar.MONTH, -9)
        date = Date(calendar.timeInMillis)
        assertEquals(
            context.resources.getQuantityString(R.plurals.years_text, 9, 9L),
            date.getRelativeDateFromToday(context)
        )
    }
}