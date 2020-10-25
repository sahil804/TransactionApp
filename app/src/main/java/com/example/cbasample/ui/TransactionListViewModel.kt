package com.example.cbasample.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cbasample.data.model.Atm
import com.example.cbasample.data.model.ItemType
import com.example.cbasample.data.model.TransactionDetail
import com.example.cbasample.data.model.TransactionListItem
import com.example.cbasample.data.model.TransactionResponseData
import com.example.cbasample.data.model.TransactionStatus
import com.example.cbasample.data.repository.TransactionRepository
import com.example.cbasample.util.differenceDays
import com.example.cbasample.util.parseToDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import kotlin.math.abs

class TransactionListViewModel @Inject constructor(private var transactionRepository: TransactionRepository) : ViewModel() {

    val transactions = transactionRepository.transactionsLiveData

    val accountDetails = MutableLiveData<List<TransactionListItem>>()

    companion object {
        const val TWO_WEEKS = 14
    }

    fun getTransactions() {
        viewModelScope.launch {
            transactionRepository.getTransactions()
        }
    }

    fun computeTransactionsDetails(transactionResponseData: TransactionResponseData) {
        viewModelScope.launch {
            val accountDetail = mutableListOf<TransactionListItem>()
            println("****")
            Log.d("Sahil", "Thread before calling withcontext " + Thread.currentThread().name)
            withContext(Dispatchers.IO) {
                println("****")
                val transactionList = mutableListOf<TransactionDetail>()
                val atmMap = mutableMapOf<String, Atm>()
                transactionResponseData.atms.forEach {
                    atmMap[it.id] = it
                }
                transactionList.addAll(
                    transactionResponseData.transactions.map {
                        val atm = it.atmId?.run { atmMap[this] }
                        TransactionDetail(it, TransactionStatus.CLEAR, atm)
                    }
                )

                transactionList.addAll(
                    transactionResponseData.pending.map {
                        val atm = it.atmId?.run { atmMap[this] }
                        TransactionDetail(it, TransactionStatus.PENDING, atm)
                    }
                )

                transactionList.sortedByDescending { it.transaction.effectiveDate.parseToDate() }
                val daysBetweenLowAndUp = transactionList[transactionList.size - 1]
                    .transaction.effectiveDate.parseToDate().differenceDays(
                        transactionList[0]
                            .transaction.effectiveDate.parseToDate()
                    )
                Log.d("Sahil", "days bet $daysBetweenLowAndUp")
                var totalExpenditure = 0.0
                var lastDate: Date? = null
                var currentDate: Date?
                transactionList.forEach {
                    currentDate = it.transaction.effectiveDate.parseToDate()
                    if (lastDate != currentDate) {
                        accountDetail.add(TransactionListItem(ItemType.DATE_HEADER, currentDate as Date))
                        accountDetail.add(TransactionListItem(ItemType.TRANSACTION, it))
                    } else {
                        accountDetail.add(TransactionListItem(ItemType.TRANSACTION, it))
                    }
                    lastDate = currentDate
                    if (it.transaction.amount < 0.0) {
                        Log.d("Sahil","---> "+totalExpenditure + "  "+abs(it.transaction.amount))
                        totalExpenditure += abs(it.transaction.amount)
                    }
                }
                accountDetail.add(0, TransactionListItem(ItemType.ACCOUNT_HEADER, transactionResponseData.account))
                Log.d("Sahil", "totalExpenditure $totalExpenditure")
                accountDetail.add(
                    TransactionListItem(
                        ItemType.PROJECTED_SPEND,
                        getProjectedSpending(daysBetweenLowAndUp, totalExpenditure)
                    )
                )
                println("^^^^")
            }
            println("^^^^")
            accountDetails.value = accountDetail
        }
    }

    private fun getProjectedSpending(daysBetweenLowAndUp: Long, expenditure: Double): Double {
        return (expenditure * TWO_WEEKS) / daysBetweenLowAndUp
    }
}