package com.example.smart_rent_application_landlord.models

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("filename")
    val filename: String?
)