package com.example.cbasample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cbasample.R
import com.example.cbasample.data.model.Atm
import com.example.cbasample.data.model.ItemType
import com.example.cbasample.data.model.TransactionDetail
import com.example.cbasample.data.model.TransactionListItem

class TransactionAdapter(
    private val transactionList: MutableList<TransactionListItem>,
    private val onItemSelected: (atm: Atm?) -> Unit
) : RecyclerView.Adapter<TransactionBaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionBaseViewHolder {
        return when (viewType) {
            ItemType.ACCOUNT_HEADER.ordinal -> {
                AccountHeaderViewHolder(getView(R.layout.layout_item_account_header, parent))
            }
            ItemType.DATE_HEADER.ordinal -> {
                DateHeaderViewHolder(getView(R.layout.layout_item_transaction_date, parent))
            }
            ItemType.TRANSACTION.ordinal -> {
                TransactionViewHolder(getView(R.layout.layout_item_transaction, parent), ::onItemSelect)
            }
            ItemType.PROJECTED_SPEND.ordinal -> {
                ProjectedSpendViewHolder(getView(R.layout.layout_item_projected_spend, parent))
            }
            else -> {
                AccountHeaderViewHolder(getView(R.layout.layout_item_transaction, parent))
            }
        }
    }

    private fun onItemSelect(position: Int) {
        transactionList[position].data.run {
            if (this is TransactionDetail) {
                onItemSelected(this.atm)
            }
        }
    }

    override fun getItemCount() = transactionList.size

    override fun onBindViewHolder(holder: TransactionBaseViewHolder, position: Int) {
        holder.bind(transactionList[position], position)
    }

    override fun getItemViewType(position: Int) = transactionList[position].type.ordinal

    private fun getView(layoutId: Int, parent: ViewGroup) =
        LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

    fun notifyDataSetChanged(listItems: List<TransactionListItem>) {
        this.transactionList.clear()
        this.transactionList.addAll(listItems)
        notifyDataSetChanged()
    }
}