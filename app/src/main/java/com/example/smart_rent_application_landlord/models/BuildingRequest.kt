// BuildingRequest.kt - Tạo file model mới
package com.example.smart_rent_application_landlord.models

data class BuildingRequest(
    val landlord_id: Int,
    val name: String,
    val address: String,
    val type_id: Int,
    val billing_date: Int,
    val image_url: String = ""
)

data class ApiResponse(
    val isSuccess: Boolean,
    val message: String?,
    val data: Any? = null
)