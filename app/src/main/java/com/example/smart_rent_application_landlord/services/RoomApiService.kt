package com.example.smart_rent_application_landlord.services

import com.example.smart_rent_application_landlord.models.ApiResponse
import com.example.smart_rent_application_landlord.models.Room
import com.example.smart_rent_application_landlord.models.RoomRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomApiService {
    @GET("rooms")
    suspend fun getAllRooms(): List<Room>

    @GET("rooms/{id}")
    suspend fun getRoomById(@Path("id") roomId: Int): Room

    @GET("rooms/building/{building_id}")
    suspend fun getRoomsByBuildingId(@Path("building_id") buildingId: Int): List<Room>

    // THÊM MỚI: Lấy phòng theo service_id
    @GET("rooms/service/{service_id}")
    suspend fun getRoomsByServiceId(@Path("service_id") serviceId: Int): List<Room>

    @POST("rooms/add")
    suspend fun addRoom(@Body roomRequest: RoomRequest): ApiResponse
}