package com.example.smart_rent_application_landlord.ui.screens.functions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.smart_rent_application_landlord.ui.screens.details.RoomDetailImagePreview
import com.example.smart_rent_application_landlord.ui.screens.details.RoomDetailTasks
import com.example.smart_rent_application_landlord.ui.screens.details.RoomDetailUserInfo
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.backgroundBoxText
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun InvoiceAddScreen(navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            InvoiceAddInfo(navController)
            Spacer(modifier = Modifier.height(20.dp))
            TwiceButton()
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Composable
fun InvoiceAddInfo(navController: NavController) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

    var startDate by remember { mutableStateOf(LocalDate.now().format(formatter)) }
    var endDate by remember { mutableStateOf("") }
    var selectedTerm by remember { mutableStateOf<String?>(null) }
    val terms = listOf("Tùy chỉnh", "3 tháng", "6 tháng", "1 năm")

    var selectedGender by remember { mutableStateOf<String?>(null) }
    val genders = listOf("Nam", "Nữ", "Khác")

    var numTenant by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
// Tự động cập nhật endDate
    LaunchedEffect(startDate, selectedTerm) {
        if (selectedTerm == null) return@LaunchedEffect
        if (selectedTerm == "Tùy chỉnh") {
            endDate = ""                // <-- để trống khi Tùy chỉnh
            return@LaunchedEffect
        }
        endDate = runCatching {
            val start = LocalDate.parse(startDate, formatter)
            val end = when (selectedTerm) {
                "3 tháng" -> start.plusMonths(3)
                "6 tháng" -> start.plusMonths(6)
                "1 năm"   -> start.plusYears(1)
                else      -> start
            }
            end.format(formatter)
        }.getOrElse { "" }
    }

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
                Text("Thông tin thời hạn hợp đồng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Thiết lập thời hạn cho hợp đồng mới", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }
    // TIME INVOICE
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
        Column(Modifier.padding(10.dp)) {
            Text("Thời hạn hợp đồng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            ModernDropdown(
                selectedItem = selectedTerm,
                onItemSelected = { selectedTerm = it },   // không cần xử lý gì thêm ở đây
                items = terms,
                placeholder = "Thời hạn"
            )
            Spacer(Modifier.height(15.dp))
            Row {
                HomeAddTextField2(title = "Ngày vào ở", value = startDate, onValueChange = { startDate = it }, modifier = Modifier.width(160.dp))
                Spacer(Modifier.weight(1f))
                HomeAddTextField2(title = "Ngày kết thúc", value = endDate, onValueChange = { newValue ->
                        if (selectedTerm == "Tùy chỉnh") endDate = newValue
                    },
                    readOnly = selectedTerm != "Tùy chỉnh",
                    onClick = {
                        // Mở DatePicker ở đây nếu readOnly = true (không cho gõ tay)
                        // showDatePicker(...)
                    },
                    supportingText = null,
                    modifier = Modifier.width(160.dp)
                )
            }
        }
    }
    // BOX DISPLAY
    Spacer(modifier = Modifier.height(10.dp))
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
                Text("Thông tin khách thuê", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Thiết lập thông tin cơ bản của khách thuê", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }
    // TENANT INFO
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
        Column(Modifier.padding(10.dp)) {
            Text("Tổng số thành viên", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(title = "Số thành viên", value = numTenant, onValueChange = { newValue -> numTenant = newValue })
            Spacer(Modifier.height(15.dp))
            Text("Tên khách lập hợp đồng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(title = "Tên khách", value = numTenant, onValueChange = { newValue -> numTenant = newValue })
            Spacer(Modifier.height(15.dp))
            Text("Số điện thoại (ZALO)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(title = "SĐT", value = numTenant, onValueChange = { newValue -> numTenant = newValue })
            Spacer(Modifier.height(15.dp))
            Row() {
                Column {
                    Text("Ngày sinh", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                    HomeAddTextField2(title = "Ngày sinh", value = birth, onValueChange = { newValue -> birth = newValue }, modifier = Modifier.width(170.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Giới tính", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                    ModernDropdown(
                        selectedItem = selectedGender,
                        onItemSelected = { selectedGender = it },
                        items = genders,
                        placeholder = "Giới tính"
                    )
                }
            }
            Spacer(Modifier.height(15.dp))
            Text("Nghề nghiệp", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(title = "Nghề nghiệp", value = numTenant, onValueChange = { newValue -> numTenant = newValue })
            Spacer(Modifier.height(15.dp))
            Text("Thẻ CCCD (Passport)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(title = "Định danh khách", value = numTenant, onValueChange = { newValue -> numTenant = newValue })
            Spacer(Modifier.height(15.dp))
            Text("Ngày cấp", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(title = "Ngày cấp", value = numTenant, onValueChange = { newValue -> numTenant = newValue })
            Spacer(Modifier.height(15.dp))
            Text("Nơi cấp", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(title = "Nơi cấp", value = numTenant, onValueChange = { newValue -> numTenant = newValue })
        }
    }
    // CCCD IMAGE
    Spacer(Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(10.dp)).background(backgroundBoxText), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(painter = painterResource(R.drawable.ic_idicard), contentDescription = null, modifier = Modifier.size(50.dp), tint = Color.Unspecified)
            Text("CCCD/Passport mặt trước", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }
    }
    Spacer(Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(10.dp)).background(backgroundBoxText), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(painter = painterResource(R.drawable.ic_idicard), contentDescription = null, modifier = Modifier.size(50.dp), tint = Color.Unspecified)
            Text("CCCD/Passport mặt sau", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }
    }
    // BOX DISPLAY
    Spacer(modifier = Modifier.height(10.dp))
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
                Text("Thông tin giá trị hợp đồng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Thiết giá thuê, mẫu hợp đồng", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }
    var priceRent by remember { mutableStateOf("") }
    var priceCoc by remember { mutableStateOf("") }
    // PRICE RENT
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column {
                Text("Giá thuê", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                HomeAddTextField2(title = "Giá thuê", value = priceRent, onValueChange = { newValue -> priceRent = newValue }, modifier = Modifier.width(160.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text("Giá cọc", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                HomeAddTextField2(title = "Giá cọc", value = priceCoc, onValueChange = { newValue -> priceCoc = newValue }, modifier = Modifier.width(160.dp))
            }
        }
    }
    // BOX DISPLAY
    Spacer(modifier = Modifier.height(10.dp))
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
                Text("Dịch vụ sử dụng cho hợp đồng này", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Tiền điện, nước, rác, mạng internet,...", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }
    // SERVICES
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(backgroundBoxText)
        .dashedBorder(
            strokeWidth = 3f,
            color = Color.LightGray,
//            cornerRadius = 20f,
            intervals = floatArrayOf(15f, 10f) // chỉnh độ dài/dãn nét
        ), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painter = painterResource(R.drawable.ic_box), contentDescription = null, modifier = Modifier.size(50.dp), tint = Color.Unspecified)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Chưa có dịch vụ được áp dụng", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {},
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.DarkGray
            ),
            modifier = Modifier.height(45.dp)
        ) {
            Row {
                Icon(painter = painterResource(R.drawable.ic_pencil), contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Chỉnh sửa dịch vụ", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}