package com.example.androidstarting.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseSuccess(
    val message: String? = null,
    val status: Boolean? = null
) : Parcelable
