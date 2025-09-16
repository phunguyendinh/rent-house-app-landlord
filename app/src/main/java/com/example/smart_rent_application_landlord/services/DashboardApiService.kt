package com.example.smart_rent_application_landlord.services

import com.android.volley.Response
import com.example.smart_rent_application_landlord.models.TenantCountResponse
import com.example.smart_rent_application_landlord.models.RoomStatsResponse
import com.example.smart_rent_application_landlord.models.RevenueStatsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DashboardApiService {

    @GET("dashboard/landlord/{landlord_id}/tenants/count")
    suspend fun getTenantCountByLandlord(
        @Path("landlord_id") landlordId: Int
    ): TenantCountResponse

    @GET("dashboard/landlord/{landlord_id}/rooms/stats")
    suspend fun getRoomStatsByLandlord(
        @Path("landlord_id") landlordId: Int
    ): RoomStatsResponse

    @GET("dashboard/landlord/{landlord_id}/revenue/stats")
    suspend fun getRevenueStatsByLandlord(
        @Path("landlord_id") landlordId: Int
    ): RevenueStatsResponse

    @GET("dashboard/building/{buildingId}/revenue/stats")
    suspend fun getRevenueStatsByBuilding(
        @Path("buildingId") buildingId: Int
    ): RevenueStatsResponse

}