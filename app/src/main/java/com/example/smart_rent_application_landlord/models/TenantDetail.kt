package com.example.smart_rent_application_landlord.models

// TenantDetail.kt
data class TenantDetail(
    val tenant: TenantInfo,
    val contracts: List<TenantContract1>,
    val serviceBills: List<TenantServiceBill>
)

data class TenantInfo(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val citizen_id: String?,
    val dob: String?,
    val gender: String?,
    val address: String?
)

data class TenantContract1(
    val contract_id: Int,
    val room_id: Int,
    val rent_price: Double,
    val contract_status: String,
    val contract_created_at: String,
    val room_name: String,
    val room_image: String?,
    val building_name: String,
    val building_address: String,
    val leave_at: String?
)

data class TenantServiceBill(
    val bill_id: Int,
    val month: Int,
    val year: Int,
    val total_amount: Double,
    val payment_status: String,
    val due_date: String?,
    val payment_date: String?,
    val room_name: String
)