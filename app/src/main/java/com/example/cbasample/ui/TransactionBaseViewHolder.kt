package com.example.cbasample.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cbasample.data.model.TransactionListItem

abstract class TransactionBaseViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    abstract fun bind(transactionListItem: TransactionListItem, position: Int)
}