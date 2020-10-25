package com.example.cbasample.ui

import android.view.View
import com.example.cbasample.R
import com.example.cbasample.data.model.Account
import com.example.cbasample.data.model.TransactionListItem
import kotlinx.android.synthetic.main.layout_item_account_header.view.*
import kotlin.math.abs

class AccountHeaderViewHolder(itemView: View) : TransactionBaseViewHolder(itemView) {
    override fun bind(transactionListItem: TransactionListItem, position: Int) {
        val account = transactionListItem.data as Account
        with(itemView) {
            account.run {
                tvAccountName.text = accountName
                tvAccountNumber.text = accountNumber
                tvFunds.text = resources.getString(
                    if (available > 0) R.string.credit_transaction_amount
                    else R.string.withdrawal_transaction_amount, abs(available)
                )
                tvBalance.text = resources.getString(
                    if (available > 0) R.string.credit_transaction_amount
                    else R.string.withdrawal_transaction_amount, abs(balance)
                )
            }
        }
    }
}