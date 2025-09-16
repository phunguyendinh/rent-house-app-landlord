package com.example.smart_rent_application_landlord.services

import com.android.volley.Response
import com.example.smart_rent_application_landlord.models.Service
import com.example.smart_rent_application_landlord.models.ServiceRequest
import com.example.smart_rent_application_landlord.models.ServiceUpdateRequest
import com.example.smart_rent_application_landlord.models.DeleteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceApiService {
    @GET("services/landlord/{landlord_id}")
    suspend fun getServicesByLandlordId(@Path("landlord_id") landlordId: Int): List<Service>

    @POST("services/add-with-rooms")
    suspend fun addServiceWithRooms(@Body service: ServiceRequest): Response<ServiceRequest>

    @PUT("services/update")
    suspend fun updateService(@Body service: ServiceUpdateRequest): Response<ServiceUpdateRequest>

    @PUT("services/update-with-rooms")
    suspend fun updateServiceWithRooms(@Body service: ServiceUpdateRequest): Response<ServiceUpdateRequest>

    // THÊM MỚI: Delete service với rooms
    @DELETE("services/delete-with-rooms/{service_id}")
    suspend fun deleteServiceWithRooms(@Path("service_id") serviceId: Int): DeleteResponse

    @DELETE("room-services/room/{roomId}/service/{serviceId}")
    suspend fun deleteRoomServiceByRoomAndService(
        @Path("roomId") roomId: Int,
        @Path("serviceId") serviceId: Int
    ): DeleteResponse  // Sử dụng DeleteResponse như các API khác
}