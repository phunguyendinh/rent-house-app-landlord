// RetrofitClient.kt - Thêm buildingTypeApiService
package com.example.smart_rent_application_landlord.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Đổi sang HTTP
    private const val BASE_URL = "http://10.69.226.1:3000/api/"
    public const val IMAGE_URL = "http://10.69.226.1:3000/images/"

    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val buildingApiService: BuildingApiService by lazy {
        retrofit.create(BuildingApiService::class.java)
    }

    val buildingTypeApiService: BuildingTypeApiService by lazy {
        retrofit.create(BuildingTypeApiService::class.java)
    }

    val roomApiService: RoomApiService by lazy {
        retrofit.create(RoomApiService::class.java)
    }
    // Thêm vào object RetrofitClient
    val serviceApiService: ServiceApiService by lazy {
        retrofit.create(ServiceApiService::class.java)
    }
    val uploadApiService: UploadApiService by lazy {
        retrofit.create(UploadApiService::class.java)
    }

    val roomServiceApiService: RoomServiceApiService by lazy {
        retrofit.create(RoomServiceApiService::class.java)
    }

    val contractApiService: ContractApiService by lazy {
        retrofit.create(ContractApiService::class.java)
    }

    val tenantApiService: TenantApiService by lazy {
        retrofit.create(TenantApiService::class.java)
    }

    val serviceBillApiService: ServiceBillApiService by lazy {
        retrofit.create(ServiceBillApiService::class.java)
    }

    val dashboardApiService: DashboardApiService = retrofit.create(DashboardApiService::class.java)
    val roomInvitationApiService: RoomInvitationApiService by lazy {
        retrofit.create(RoomInvitationApiService::class.java)
    }
}