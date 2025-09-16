package com.example.smart_rent_application_landlord.models

data class BuildingType(
    val id: Int,
    val name: String,
    val description: String?,
    val has_detail: Boolean,
    val created_at: String
)