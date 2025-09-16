package com.example.smart_rent_application_landlord.models

data class Service(
    val id: Int,
    val landlord_id: Int,
    val name: String,
    val unit: String?,
    val price_per_unit: Double?,
    val is_mandatory: Boolean?,
    val single_unit: Boolean?,
    val created_at: String
)