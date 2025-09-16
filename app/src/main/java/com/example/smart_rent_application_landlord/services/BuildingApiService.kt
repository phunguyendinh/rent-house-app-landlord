// BuildingApiService.kt - Thêm endpoint lấy counts
package com.example.smart_rent_application_landlord.services

import com.example.smart_rent_application_landlord.models.ApiResponse
import com.example.smart_rent_application_landlord.models.Building
import com.example.smart_rent_application_landlord.models.BuildingCount
import com.example.smart_rent_application_landlord.models.BuildingRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BuildingApiService {
    @GET("buildings/landlord/{landlord_id}")
    suspend fun getBuildingsByLandlordId(@Path("landlord_id") landlordId: Int): List<Building>

    @GET("buildings/landlord/{landlord_id}/type/{type_id}")
    suspend fun getBuildingsByLandlordAndType(
        @Path("landlord_id") landlordId: Int,
        @Path("type_id") typeId: Int
    ): List<Building>

    @GET("buildings/landlord/{landlord_id}/counts")
    suspend fun getBuildingCountsByLandlord(@Path("landlord_id") landlordId: Int): List<BuildingCount>

    @GET("buildings/{id}")
    suspend fun getBuildingById(@Path("id") buildingId: Int): Building

    // THÊM MỚI: Method để thêm building
    @POST("buildings/add")
    suspend fun addBuilding(@Body buildingRequest: BuildingRequest): ApiResponse
}