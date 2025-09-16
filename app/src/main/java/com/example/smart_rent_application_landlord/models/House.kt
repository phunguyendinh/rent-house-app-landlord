package com.example.smart_rent_application_landlord.models

import com.example.smart_rent_application_landlord.R

data class HouseInfo(
    val imageResId: Int,
    val addressLine1: String,
    val addressLine2: String,
)

val houseList = listOf(
    HouseInfo(imageResId = R.drawable.house2, addressLine1 = "123 Nguyễn Huệ, Hà Nội", addressLine2 = "Quận 1, TP.HCM"),
    HouseInfo(imageResId = R.drawable.house4, addressLine1 = "21, Bần Yên Nhân, Mỹ Hào", addressLine2 = "Hưng Yên, Việt Nam"),
    HouseInfo(imageResId = R.drawable.house3, addressLine1 = "88 Trần Hưng Đạo", addressLine2 = "Đà Nẵng, Việt Nam")
)
