package com.example.smart_rent_application_landlord.services

import com.example.smart_rent_application_landlord.models.BuildingType
import retrofit2.http.GET

interface BuildingTypeApiService {
    @GET("building-types")
    suspend fun getAllBuildingTypes(): List<BuildingType>
}