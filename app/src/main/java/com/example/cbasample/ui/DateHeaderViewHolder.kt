package com.example.cbasample.ui

import android.view.View
import com.example.cbasample.data.model.TransactionListItem
import com.example.cbasample.util.displayDate
import com.example.cbasample.util.getRelativeDateFromToday
import kotlinx.android.synthetic.main.layout_item_transaction_date.view.*
import java.util.Date
import java.util.Locale

class DateHeaderViewHolder(itemView: View) : TransactionBaseViewHolder(itemView) {
    override fun bind(transactionListItem: TransactionListItem, position: Int) {
        val date = transactionListItem.data as Date
        with(itemView) {
            tvDate.text = date.displayDate().toUpperCase(Locale.ROOT)
            tvDateBefore.text = date.getRelativeDateFromToday(context)
        }
    }
}