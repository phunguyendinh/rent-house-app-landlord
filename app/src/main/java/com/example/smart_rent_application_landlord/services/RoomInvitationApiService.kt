package com.example.smart_rent_application_landlord.services

import com.example.smart_rent_application_landlord.models.ApiResponse
import com.example.smart_rent_application_landlord.models.CreateInvitationRequest
import com.example.smart_rent_application_landlord.models.InvitationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomInvitationApiService {
    @POST("room-invitations/create")
    suspend fun createRoomInvitation(@Body requestBody: CreateInvitationRequest): InvitationResponse

    @GET("room-invitations/validate/{invitation_code}")
    suspend fun validateInvitationCode(@Path("invitation_code") invitationCode: String): InvitationResponse

    @POST("room-invitations/use")
    suspend fun useInvitationCode(@Body requestBody: Map<String, Any>): ApiResponse

    @GET("room-invitations/landlord/{landlord_id}")
    suspend fun getInvitationsByLandlord(@Path("landlord_id") landlordId: Int): InvitationListResponse
}

data class InvitationListResponse(
    val isSuccess: Boolean,
    val data: List<InvitationHistoryItem>
)

data class InvitationHistoryItem(
    val id: Int,
    val invitation_code: String,
    val status: String,
    val max_uses: Int,
    val used_count: Int,
    val expires_at: String,
    val created_at: String,
    val room_name: String,
    val building_name: String
)