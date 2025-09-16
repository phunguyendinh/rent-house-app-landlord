package com.example.smart_rent_application_landlord.services

import com.example.smart_rent_application_landlord.models.TenantDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface TenantApiService {
    @GET("tenants/detail/{id}")
    suspend fun getTenantDetailById(@Path("id") tenantId: Int): TenantDetail
}