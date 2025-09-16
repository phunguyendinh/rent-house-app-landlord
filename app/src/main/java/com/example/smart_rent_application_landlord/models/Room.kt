package com.example.smart_rent_application_landlord.models

data class Room(
    val id: Int,
    val name: String,
    val building_id: Int,
    val rent_price: Double,
    val status: String?,
    val max_occupants: Int?,
    val image_url: String?,
    val created_at: String,
    // THÊM MỚI: Thông tin building (chỉ có khi join với bảng Building)
    val building_name: String? = null,
    val building_address: String? = null
)