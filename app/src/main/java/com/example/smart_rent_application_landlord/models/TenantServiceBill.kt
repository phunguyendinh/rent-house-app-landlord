package com.example.smart_rent_application_landlord.models

data class TenantServiceBill1(
    val bill_id: Int,
    val room_id: Int,
    val tenant_id: Int?,
    val month: Int,
    val year: Int,
    val total_amount: Double,
    val payment_status: String, // "paid" or "unpaid"
    val sent_date: String?,
    val due_date: String?,
    val payment_date: String?,
    val payment_method: String?,
    val receipt_image: String?
)