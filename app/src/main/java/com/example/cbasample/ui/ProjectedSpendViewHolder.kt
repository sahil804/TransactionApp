package com.example.cbasample.ui

import android.view.View
import com.example.cbasample.R
import com.example.cbasample.data.model.TransactionListItem
import kotlinx.android.synthetic.main.layout_item_projected_spend.view.*

class ProjectedSpendViewHolder(itemView: View) : TransactionBaseViewHolder(itemView) {
    override fun bind(transactionListItem: TransactionListItem, position: Int) {
        val spend = transactionListItem.data as Double
        with(itemView) {
            tvProjectedSpend.text = resources.getString(R.string.credit_transaction_amount, spend)
        }
    }
}