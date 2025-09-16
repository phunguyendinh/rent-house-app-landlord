package com.example.smart_rent_application_landlord.models

data class TenantContract(
    val tenant_id: Int,
    val tenant_name: String,
    val tenant_phone: String,
    val room_id: Int,
    val room_name: String,
    val contract_id: Int,
    val contract_status: String,
    val leave_at: String?
)