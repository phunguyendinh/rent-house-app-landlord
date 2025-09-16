package com.example.smart_rent_application_landlord.ui.screens.functions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.ui.components.ModernDropdown
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.backgroundBoxText
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel
import androidx.compose.foundation.BorderStroke

@Composable
fun ServiceAddScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    var serviceName by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf<String?>(null) }
    var isApplyToAllRooms by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            ServiceAddInfo(
                navController = navController,
                serviceName = serviceName,
                onServiceNameChange = { serviceName = it },
                servicePrice = servicePrice,
                onServicePriceChange = { servicePrice = it },
                selectedUnit = selectedUnit,
                onUnitChange = { selectedUnit = it },
                isApplyToAllRooms = isApplyToAllRooms,
                onSwitchChange = { isApplyToAllRooms = it }
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Custom buttons cho ServiceAdd
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { navController.popBackStack() },
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.DarkGray
                    ),
                    modifier = Modifier.height(45.dp)
                ) {
                    Text("Đóng", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        homeViewModel.addService(
                            name = serviceName,
                            unit = selectedUnit ?: "",
                            pricePerUnit = servicePrice.toDoubleOrNull() ?: 0.0,
                            isMandatory = isApplyToAllRooms
                        )
                        navController.popBackStack()
                    },
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006FFD),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(45.dp)
                ) {
                    Text("Lưu", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun ServiceAddInfo(
    navController: NavController,
    serviceName: String,
    onServiceNameChange: (String) -> Unit,
    servicePrice: String,
    onServicePriceChange: (String) -> Unit,
    selectedUnit: String?,
    onUnitChange: (String?) -> Unit,
    isApplyToAllRooms: Boolean,
    onSwitchChange: (Boolean) -> Unit
) {
    val units = listOf("Khối", "KWh", "Tháng", "Người", "Chiếc", "Lần", "Cái", "Bình", "m2", "Giờ")

    // BOX DISPLAY
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(
        backgroundBoxText
    )) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(7.dp)).background(
                yellowColorButton
            ), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(R.drawable.ic_note), contentDescription = null, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Thông tin dịch vụ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Thông tin cơ bản tên, giá dịch vụ,...", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }

    // NORMAL INFO
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(
        backgroundBoxText
    )) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Tên dịch vụ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(
                title = "Tên dịch vụ",
                value = serviceName,
                onValueChange = onServiceNameChange
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text("Ví dụ đề xuất", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            Row {
                Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(10.dp)).background(
                    yellowColorButton.copy(alpha = 0.3f))) {
                    Text("Tiền giữ xe", fontWeight = FontWeight.Medium, fontSize = 14.sp, modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(10.dp)).background(
                    yellowColorButton.copy(alpha = 0.3f))) {
                    Text("Tiền đổ rác", fontWeight = FontWeight.Medium, fontSize = 14.sp, modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(10.dp)).background(
                    yellowColorButton.copy(alpha = 0.3f))) {
                    Text("Tiền mạng", fontWeight = FontWeight.Medium, fontSize = 14.sp, modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp))
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(
        backgroundBoxText
    )) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Đơn vị và giá", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
            Text("Nhập thông tin đơn vị tính và giá dịch vụ", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(5.dp))
            ModernDropdown(
                selectedItem = selectedUnit,
                onItemSelected = onUnitChange,
                items = units,
                placeholder = "Đơn vị"
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text("Giá dịch vụ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
            Text("Ví dụ tiền rác 30.000 đ/tháng, bạn nhập 30.000", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
            HomeAddTextField(
                title = "Giá dịch vụ",
                value = servicePrice,
                onValueChange = onServicePriceChange
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Thêm dịch vụ cho toàn bộ phòng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Switch(
                    checked = isApplyToAllRooms,
                    onCheckedChange = onSwitchChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF1BCAA4),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFD4D4D2),
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}