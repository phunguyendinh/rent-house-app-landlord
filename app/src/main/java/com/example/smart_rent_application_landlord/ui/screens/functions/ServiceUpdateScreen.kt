package com.example.smart_rent_application_landlord.ui.screens.functions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.models.Room
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.services.RetrofitClient
import com.example.smart_rent_application_landlord.ui.components.ModernDropdown
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.backgroundBoxText
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

@Composable
fun ServiceUpdateScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    serviceId: Int,
    modifier: Modifier = Modifier
) {
    var serviceName by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf<String?>(null) }
    var isApplyToAllRooms by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Collect current service data từ ViewModel
    val services by homeViewModel.services.collectAsState()
    val roomsByService by homeViewModel.roomsByService.collectAsState()

    val currentService = remember(services, serviceId) {
        services.find { it.id == serviceId }
    }

    // Load service data và rooms khi component mount
    LaunchedEffect(currentService) {
        currentService?.let { service ->
            serviceName = service.name
            servicePrice = service.price_per_unit?.toString() ?: ""
            selectedUnit = service.unit
            isApplyToAllRooms = service.is_mandatory ?: false
        }
    }

    // Load rooms by service - chỉ load một lần khi serviceId thay đổi
    LaunchedEffect(serviceId) {
        homeViewModel.fetchRoomsByServiceId(serviceId)
    }

    DisposableEffect(Unit) {
        onDispose {
            homeViewModel.clearRoomsByService()
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            ServiceUpdateInfo(
                navController = navController,
                serviceName = serviceName,
                onServiceNameChange = { serviceName = it },
                servicePrice = servicePrice,
                onServicePriceChange = { servicePrice = it },
                selectedUnit = selectedUnit,
                onUnitChange = { selectedUnit = it },
                isApplyToAllRooms = isApplyToAllRooms,
                onSwitchChange = { isApplyToAllRooms = it },
                roomsByService = roomsByService,
                serviceId = serviceId,
                homeViewModel = homeViewModel
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Custom buttons
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
                        isLoading = true
                        homeViewModel.updateService(
                            serviceId = serviceId,
                            name = serviceName,
                            unit = selectedUnit ?: "",
                            pricePerUnit = servicePrice.toDoubleOrNull() ?: 0.0,
                            isMandatory = isApplyToAllRooms
                        )
                        navController.popBackStack()
                    },
                    enabled = !isLoading,
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006FFD),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(45.dp)
                ) {
                    Text(
                        if (isLoading) "Đang lưu..." else "Lưu",
                        fontFamily = GilroyFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ServiceUpdateInfo(
    navController: NavController,
    serviceName: String,
    onServiceNameChange: (String) -> Unit,
    servicePrice: String,
    onServicePriceChange: (String) -> Unit,
    selectedUnit: String?,
    onUnitChange: (String?) -> Unit,
    isApplyToAllRooms: Boolean,
    onSwitchChange: (Boolean) -> Unit,
    roomsByService: List<Room>,
    serviceId: Int,
    homeViewModel: HomeViewModel
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
                Text("Chỉnh sửa dịch vụ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Cập nhật thông tin dịch vụ", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
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
                Text("Áp dụng cho toàn bộ phòng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
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

    Spacer(modifier = Modifier.height(10.dp))

    // BOX DISPLAY - Phòng áp dụng dịch vụ
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
                Text("Phòng áp dụng dịch vụ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("${roomsByService.size} phòng đang áp dụng dịch vụ này", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Hiển thị danh sách phòng với scroll khi quá 3 phòng
    if (roomsByService.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
            Column(modifier = Modifier.padding(10.dp)) {
                // Tính chiều cao tối đa cho container phòng (3 phòng = 3 * (60dp card + 15dp spacing))
                val maxHeight = if (roomsByService.size > 3) 255.dp else (roomsByService.size * 85).dp

                LazyColumn(
                    modifier = Modifier.height(maxHeight)
                ) {
                    items(
                        items = roomsByService,
                        key = { room -> room.id } // Thêm key để tối ưu recomposition
                    ) { room ->
                        RoomCard(
                            roomID = room.name,
                            roomImageUrl = room.image_url,
                            buildingName = room.building_name ?: "Unknown Building",
                            roomId = room.id,
                            serviceId = serviceId,
                            onRemoveService = { roomId, svcId ->
                                homeViewModel.removeServiceFromRoom(roomId, svcId)
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Không có phòng nào áp dụng dịch vụ này",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun RoomCard(
    roomID: String,
    roomImageUrl: String?,
    buildingName: String,
    roomId: Int,
    serviceId: Int,
    onRemoveService: (Int, Int) -> Unit,
    navController: NavController
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth().clickable (
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
    ) { navController.navigate(Screen.RoomDetail.route) }) {
        Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)) {
            // Sử dụng AsyncImage thay vì Image với resource
            val painter = rememberAsyncImagePainter(
                model = if (roomImageUrl.isNullOrEmpty()) {
                    R.drawable.room2 // Ảnh mặc định nếu không có image_url
                } else {
                    "${RetrofitClient.IMAGE_URL}$roomImageUrl"
                }
            )
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column() {
                Row (modifier = Modifier.width(200.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(roomID, fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(buildingName, fontFamily = GilroyFont, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))

            // Thay thế Icon bằng IconButton circular với icon minus
            Box(
                modifier = Modifier
                    .size(35.dp) // Đặt kích thước cho Box giống IconButton
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color.Red.copy(alpha = 0.2f), CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null  // tắt ripple
                    ) { showDeleteDialog = true }, // Thay thế onClick của IconButton
                contentAlignment = Alignment.Center // Đảm bảo Icon nằm giữa
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_minus),
                    contentDescription = "Remove service from room",
                    modifier = Modifier.size(10.dp),
                    tint = Color.Red
                )
            }

        }
    }

    // Dialog xác nhận xóa
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Xác nhận xóa",
                    fontFamily = GilroyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "Bạn có muốn xóa dịch vụ này khỏi $roomID không?",
                    fontFamily = GilroyFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemoveService(roomId, serviceId)
                        showDeleteDialog = false
                    }
                ) {
                    Text(
                        "Đồng ý",
                        fontSize = 14.sp,
                        fontFamily = GilroyFont,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Red
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(
                        "Hủy",
                        fontSize = 14.sp,
                        fontFamily = GilroyFont,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        )
    }
}