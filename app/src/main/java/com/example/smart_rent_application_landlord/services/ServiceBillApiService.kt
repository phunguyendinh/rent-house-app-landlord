package com.example.smart_rent_application_landlord.services

import com.example.smart_rent_application_landlord.models.TenantServiceBill1
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// Thêm vào ServiceBillApiService.kt
interface ServiceBillApiService {
    @GET("service-bills/details/{bill_id}")
    suspend fun getServiceBillWithDetails(@Path("bill_id") billId: Int): ServiceBillDetail

    // THÊM MỚI - API để đánh dấu thanh toán
    @PUT("service-bills/{bill_id}/mark-paid")
    suspend fun markServiceBillAsPaid(@Path("bill_id") billId: Int): ApiResponse

    @GET("service-bills/room/{room_id}")
    suspend fun getServiceBillsByRoomId(@Path("room_id") roomId: Int): List<TenantServiceBill1>
}

// Thêm data class cho response
data class ApiResponse(
    val isSuccess: Boolean,
    val message: String
)

// Thêm data class
data class ServiceBillDetail(
    val id: Int,
    val room_id: Int,
    val room_name: String,
    val rent_price: Double,
    val building_name: String,
    val building_address: String,
    val tenant_name: String,
    val month: Int,
    val year: Int,
    val total_amount: Double,
    val status: String,
    val sent_date: String?,
    val due_date: String?,
    val payment_date: String?,
    val payment_method: String?,
    val service_details: List<ServiceDetail>
)

data class ServiceDetail(
    val id: Int,
    val service_name: String,
    val unit: String,
    val price_per_unit: Double,
    val quantity: Int,
    val total: Double,
    val proof_image: String?
)