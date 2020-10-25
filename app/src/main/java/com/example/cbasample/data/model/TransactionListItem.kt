package com.example.cbasample.data.model

enum class ItemType {
    TRANSACTION,
    DATE_HEADER,
    ACCOUNT_HEADER,
    PROJECTED_SPEND
}
data class TransactionListItem(val type: ItemType, val data: Any)