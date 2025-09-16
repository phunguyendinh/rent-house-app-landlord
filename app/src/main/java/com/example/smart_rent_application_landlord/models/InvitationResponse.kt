package com.example.smart_rent_application_landlord.models

// Thêm data class cho API response
data class InvitationResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: InvitationData?
)

data class InvitationData(
    val invitation_id: Int,
    val invitation_code: String,
    val room_id: Int,
    val room_name: String,
    val expires_at: String,
    val max_uses: Int
)