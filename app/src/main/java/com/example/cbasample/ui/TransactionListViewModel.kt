package com.example.cbasample.ui

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

    val transactionList = MutableLiveData<List<TransactionListItem>>()

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
            val transactionListItems = mutableListOf<TransactionListItem>()
            withContext(Dispatchers.IO) {
                val transactionList = mutableListOf<TransactionDetail>()
                val atmMap = mutableMapOf<String, Atm>()
                // created map for O(1) access of transaction ATM
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
                var totalExpenditure = 0.0
                var lastDate: Date? = null
                var currentDate: Date?
                transactionList.forEach {
                    currentDate = it.transaction.effectiveDate.parseToDate()
                    // for group of same date, adding a single header
                    if (lastDate != currentDate) {
                        transactionListItems.add(TransactionListItem(ItemType.DATE_HEADER, currentDate as Date))
                        transactionListItems.add(TransactionListItem(ItemType.TRANSACTION, it))
                    } else {
                        transactionListItems.add(TransactionListItem(ItemType.TRANSACTION, it))
                    }
                    lastDate = currentDate
                    if (it.transaction.amount < 0.0) {
                        totalExpenditure += abs(it.transaction.amount)
                    }
                }
                transactionListItems.add(0, TransactionListItem(ItemType.ACCOUNT_HEADER, transactionResponseData.account))
                transactionListItems.add(
                    TransactionListItem(
                        ItemType.PROJECTED_SPEND,
                        getProjectedSpending(daysBetweenLowAndUp, totalExpenditure)
                    )
                )
            }
            transactionList.value = transactionListItems
        }
    }

    /*
        This function takes the total expenditure over n days and calculate approx for 14 days
     */
    private fun getProjectedSpending(daysBetweenLowAndUp: Long, expenditure: Double): Double {
        return (expenditure * TWO_WEEKS) / daysBetweenLowAndUp
    }
}