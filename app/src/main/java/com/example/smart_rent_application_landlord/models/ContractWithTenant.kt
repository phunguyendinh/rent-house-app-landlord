package com.example.smart_rent_application_landlord.models

data class ContractWithTenant(
    val contract_id: Int,
    val room_id: Int,
    val tenant_id: Int,
    val tenant_name: String,
    val tenant_phone: String?,
    val rent_price: Double,
    val status: String, // 'pending', 'active', 'expired', 'terminated'
    val contract_file: String?,
    val created_at: String
)