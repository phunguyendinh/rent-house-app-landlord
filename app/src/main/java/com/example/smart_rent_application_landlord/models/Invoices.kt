package com.example.smart_rent_application_landlord.models

data class Invoices(
    val title: String,
    val price: String,
    val isPay: Boolean
)
val invoices = listOf(
    Invoices("Tháng 1/2024", "1.2 Triệu/Tháng", isPay = true),
    Invoices("Tháng 2/2024", "1.234 Triệu/Tháng", isPay = false),
    Invoices("Tháng 3/2024", "1.567 Triệu/Tháng", isPay = false),
)