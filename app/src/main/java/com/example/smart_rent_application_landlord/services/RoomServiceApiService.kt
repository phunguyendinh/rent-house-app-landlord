package com.example.smart_rent_application_landlord.services

import com.example.smart_rent_application_landlord.models.ApiResponse
import com.example.smart_rent_application_landlord.models.RoomServiceRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface RoomServiceApiService {
    @POST("room-services/add")
    suspend fun addRoomService(@Body roomServiceRequest: RoomServiceRequest): ApiResponse
}