package com.example.courcework.data.network.model

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("result") val result: String,
    @SerializedName("msg") val msg: String,
) {
    val isSuccessful get() = msg == "success"
}