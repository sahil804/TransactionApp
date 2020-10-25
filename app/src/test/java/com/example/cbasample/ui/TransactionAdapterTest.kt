package com.example.cbasample.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.test.core.app.ApplicationProvider
import com.example.cbasample.MySampleRunner
import com.example.cbasample.R
import com.example.cbasample.data.model.Account
import com.example.cbasample.data.model.Atm
import com.example.cbasample.data.model.ItemType
import com.example.cbasample.data.model.Location
import com.example.cbasample.data.model.Transaction
import com.example.cbasample.data.model.TransactionDetail
import com.example.cbasample.data.model.TransactionListItem
import com.example.cbasample.data.model.TransactionStatus
import kotlinx.android.synthetic.main.layout_item_account_header.view.*
import kotlinx.android.synthetic.main.layout_item_transaction.view.*
import kotlinx.android.synthetic.main.layout_item_transaction_date.view.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar
import java.util.Date

@RunWith(MySampleRunner::class)
class TransactionAdapterTest {

    private lateinit var transactionListAdapter: TransactionAdapter
    private lateinit var context: Context
    private lateinit var holder: TransactionBaseViewHolder

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        transactionListAdapter = TransactionAdapter(getTransactionListItem(), { })
    }

    @Test
    fun testAccountHeader() {
        holder = AccountHeaderViewHolder(getItemView(R.layout.layout_item_account_header))
        transactionListAdapter.onBindViewHolder(holder, 0)
        with(holder.itemView) {
            assertEquals("Account Name", tvAccountName.text)
            assertEquals("777 888 999", tvAccountNumber.text)
            assertEquals("$67.00", tvBalance.text)
            assertEquals("$56.00", tvFunds.text)
        }
    }

    @Test
    fun testclearTransaction() {
        holder = TransactionViewHolder(getItemView(R.layout.layout_item_transaction), { })
        transactionListAdapter.onBindViewHolder(holder, 1)
        with(holder.itemView) {
            assertFalse(tvTransactionDescription.text.contains("Pending"))
            assertEquals("$12.00", tvTransactionAmount.text)
            assertTrue(ivLocation.isVisible)
        }
    }

    @Test
    fun testPendingTransaction() {
        holder = TransactionViewHolder(getItemView(R.layout.layout_item_transaction), { })
        transactionListAdapter.onBindViewHolder(holder, 2)
        with(holder.itemView) {
            assertTrue(tvTransactionDescription.text.contains("Pending"))
            assertEquals("$12.00", tvTransactionAmount.text)
            assertTrue(ivLocation.isVisible)
        }
    }

    @Test
    fun testDateHeader() {
        holder = DateHeaderViewHolder(getItemView(R.layout.layout_item_transaction_date))
        transactionListAdapter.onBindViewHolder(holder, 3)
        with(holder.itemView) {
            assertEquals("01 JAN 2020", tvDate.text)
        }
    }

    private fun getItemView(layoutId: Int): View {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(layoutId, null, false)
    }

    private fun getTransactionListItem(): MutableList<TransactionListItem> {
        val transactionList = mutableListOf<TransactionListItem>()
        transactionList.add(
            0, TransactionListItem(
                ItemType.ACCOUNT_HEADER,
                Account("Account Name", "777 888 999", 56.00, 67.00)
            )
        )

        transactionList.add(
            1, TransactionListItem(
                ItemType.TRANSACTION,
                TransactionDetail(
                    Transaction(
                        "44e5b2bc484331ea24afd85ecfb212c8", "20/07/2017",
                        "Kaching TFR from JOHN CITIZEN<br/>xmas donation", 12.00, "129382"
                    ), TransactionStatus.CLEAR,
                    Atm(
                        "129382",
                        "Circular Quay Station",
                        "8 Alfred St, Sydney, NSW 2000",
                        Location(-33.861382, 151.210316)
                    )
                )
            )
        )

        transactionList.add(
            2, TransactionListItem(
                ItemType.TRANSACTION,
                TransactionDetail(
                    Transaction(
                        "44e5b2bc484331ea24afd85ecfb212c8", "20/07/2017",
                        "Kaching TFR from JOHN CITIZEN<br/>xmas donation", 12.00, "129382"
                    ), TransactionStatus.PENDING,
                    Atm(
                        "129382",
                        "Circular Quay Station",
                        "8 Alfred St, Sydney, NSW 2000",
                        Location(-33.861382, 151.210316)
                    )
                )
            )
        )

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DATE, 1)
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        calendar.set(Calendar.YEAR, 2020)
        val date = Date(calendar.timeInMillis)
        transactionList.add(
            3, TransactionListItem(
                ItemType.DATE_HEADER, date
            )
        )
        return transactionList
    }
}