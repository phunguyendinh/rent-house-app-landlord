package com.example.smart_rent_application_landlord.models

// File: com.example.smart_rent_application_landlord.models.InvitationRequest.kt

data class CreateInvitationRequest(
    val room_id: Int,
    val landlord_id: Int,
    val expires_in_days: Int,
    val max_uses: Int
)