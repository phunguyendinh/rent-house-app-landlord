package com.example.smart_rent_application_landlord.models

data class Building(
    val id: Int,
    val landlord_id: Int,
    val name: String,
    val address: String,
    val type_id: Int,
    val billing_date: Int,
    val image_url: String,
    val is_deleted: Boolean,
    val created_at: String
)
data class BuildingCount(
    val type_id: Int,
    val type_name: String,
    val count: Int
)