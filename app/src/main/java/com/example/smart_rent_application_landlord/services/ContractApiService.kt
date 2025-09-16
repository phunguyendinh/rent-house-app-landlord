package com.example.smart_rent_application_landlord.services

import com.example.smart_rent_application_landlord.models.ContractWithTenant
import com.example.smart_rent_application_landlord.models.TenantContract
import retrofit2.http.GET
import retrofit2.http.Path

interface ContractApiService {
    @GET("contracts/tenants/building/{buildingId}")
    suspend fun getTenantsByBuildingId(@Path("buildingId") buildingId: Int): List<TenantContract>

    // THÊM MỚI: Lấy active contract theo room_id
    @GET("contracts/room/{room_id}/active")
    suspend fun getActiveContractByRoomId(@Path("room_id") roomId: Int): ContractWithTenant?

    // THÊM MỚI: Lấy tất cả contracts theo room_id (nếu cần)
    @GET("contracts/room/{room_id}")
    suspend fun getContractsByRoomId(@Path("room_id") roomId: Int): List<ContractWithTenant>
}