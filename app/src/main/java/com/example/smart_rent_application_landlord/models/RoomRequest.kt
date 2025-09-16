package com.example.smart_rent_application_landlord.models

data class RoomRequest(
    val name: String,
    val building_id: Int,
    val rent_price: Double,
    val max_occupants: Int,
    val image_url: String = ""
)