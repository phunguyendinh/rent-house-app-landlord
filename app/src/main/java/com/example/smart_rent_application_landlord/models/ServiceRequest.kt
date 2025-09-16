package com.example.smart_rent_application_landlord.models

data class ServiceRequest(
    val landlord_id: Int,
    val name: String,
    val unit: String,
    val price_per_unit: Double,
    val is_mandatory: Boolean,
    val single_unit: Boolean
)

data class ServiceResponse(
    val serviceId: Int,
    val message: String
)

data class ServiceUpdateRequest(
    val id: Int,
    val landlord_id: Int,
    val name: String,
    val unit: String,
    val price_per_unit: Double,
    val is_mandatory: Boolean,
    val single_unit: Boolean
)

// THÊM MỚI: Response cho delete operation
data class DeleteResponse(
    val isSuccess: Boolean,
    val message: String
)