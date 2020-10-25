package com.example.cbasample.ui

import android.view.View
import com.example.cbasample.R
import com.example.cbasample.data.model.TransactionDetail
import com.example.cbasample.data.model.TransactionListItem
import com.example.cbasample.data.model.TransactionStatus
import com.example.cbasample.util.getSpannedText
import com.example.cbasample.util.setVisibility
import kotlinx.android.synthetic.main.layout_item_transaction.view.*
import kotlin.math.abs

class TransactionViewHolder(itemView: View, onItemSelected: (position: Int) -> Unit) :
    TransactionBaseViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            onItemSelected(adapterPosition)
        }
    }

    override fun bind(transactionListItem: TransactionListItem, position: Int) {
        val transactionObj = transactionListItem.data as TransactionDetail
        with(itemView) {
            transactionObj.run {
                tvTransactionDescription.text = resources.getString(
                    if (transactionStatus == TransactionStatus.PENDING) R.string.pending_transaction_text else R.string.transaction_text,
                    transaction.description
                ).getSpannedText()
                tvTransactionAmount.text = resources.getString(
                    if (transaction.amount > 0) R.string.credit_transaction_amount
                    else R.string.withdrawal_transaction_amount, abs(transaction.amount)
                )
                ivLocation.setVisibility(atm != null)
            }
        }
    }
}