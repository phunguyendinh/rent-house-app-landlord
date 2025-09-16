package com.example.smart_rent_application_landlord.models

data class TenantCountResponse(
    val success: Boolean,
    val count: Int
)

data class RoomStatsResponse(
    val success: Boolean,
    val totalRooms: Int,
    val emptyRooms: Int,
    val occupiedRooms: Int
)

data class RevenueStatsResponse(
    val success: Boolean,
    val totalRevenue: Double,
    val monthlyRevenue: Double
)