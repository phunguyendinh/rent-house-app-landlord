package com.example.smart_rent_application_landlord.services

import com.example.smart_rent_application_landlord.models.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UploadApiService {
    @Multipart
    @POST("upload/image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): UploadResponse
}