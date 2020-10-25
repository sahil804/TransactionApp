package com.example.cbasample.data.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Entity
data class TransactionResponseData (
    val account : Account,
    val transactions : List<Transaction>,
    val pending : List<Transaction>,
    val atms : List<Atm>
)

@Parcelize
data class Transaction (
    val id : String,
    val effectiveDate : String,
    val description : String,
    val amount : Double,
    val atmId: String?
) : Parcelable

@Parcelize
data class Location (
    val lat : Double,
    val lng : Double
) : Parcelable

@Parcelize
data class Atm (
    val id : String,
    val name : String,
    val address : String,
    val location : Location
) : Parcelable

data class Account (
    val accountName : String,
    val accountNumber : String,
    val available : Double,
    val balance : Double
)

enum class TransactionStatus {
    CLEAR,
    PENDING
}

@Parcelize
data class TransactionDetail(
    val transaction: Transaction,
    val transactionStatus: TransactionStatus,
    val atm: Atm?
) : Parcelable