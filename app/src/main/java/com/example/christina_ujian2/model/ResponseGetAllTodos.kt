package com.example.androidstarting.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseGetAllOutstanding(
    val total: Int? = null,
    val data: DataOutstandingList? = null,
    val message: String? = null,
    val status: Boolean? = null
) : Parcelable

@Parcelize
data class DataOutstandingList(
    val collection: List<DataOutstanding?>? = null
) : Parcelable

@Parcelize
data class DataOutstanding(
    val id: String? = null,
    val nama: String? = null,
    val alamat: String? = null,
    val outstanding: String? = null
): Parcelable
