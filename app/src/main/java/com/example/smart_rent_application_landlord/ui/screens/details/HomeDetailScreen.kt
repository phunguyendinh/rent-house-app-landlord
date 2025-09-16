package com.example.smart_rent_application_landlord.ui.screens.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.models.Building
import com.example.smart_rent_application_landlord.models.Room
import com.example.smart_rent_application_landlord.models.TenantContract
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.services.RetrofitClient
import com.example.smart_rent_application_landlord.ui.components.SearchBar
import com.example.smart_rent_application_landlord.ui.components.TabItem
import com.example.smart_rent_application_landlord.ui.screens.formatPrice
import com.example.smart_rent_application_landlord.ui.screens.formatRevenue
import com.example.smart_rent_application_landlord.ui.theme.Bold35Text
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.Semi18Text
import com.example.smart_rent_application_landlord.ui.theme.checkColor
import com.example.smart_rent_application_landlord.ui.theme.lightGreenMainColor
import com.example.smart_rent_application_landlord.ui.theme.lightYellowColor
import com.example.smart_rent_application_landlord.ui.theme.wrongColor
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

// Cập nhật HomeDetailScreen để fetch building
@Composable
fun HomeDetailScreen(
    navController: NavController,
    buildingId: Int,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    // Fetch building, rooms và tenants khi screen được tạo
    LaunchedEffect(buildingId) {
        homeViewModel.fetchBuildingById(buildingId)
        homeViewModel.fetchRoomsByBuildingId(buildingId)
        homeViewModel.fetchTenantsByBuildingId(buildingId) // Thêm dòng này
        homeViewModel.fetchBuildingRevenueStats(buildingId) // ✅ mới thêm
    }

    val building by homeViewModel.currentBuilding.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            HomeDetailCard(building = building)
            HomeDetailFinancials(homeViewModel)
            HomeDetailTabContent(navController, homeViewModel, buildingId)
        }
    }
}
// Cập nhật HomeDetailCard để nhận building parameter
@Composable
fun HomeDetailCard(building: Building? = null) {
    Box(modifier = Modifier.height(300.dp).clip(RoundedCornerShape(20.dp))) {
        // Sử dụng image từ API hoặc ảnh mặc định
        val painter = rememberAsyncImagePainter(
            model = if (building?.image_url.isNullOrEmpty()) {
                R.drawable.house2 // Ảnh mặc định
            } else {
                "${RetrofitClient.IMAGE_URL}${building?.image_url}"
            }
        )

        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.matchParentSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.8f)
                    ), startY = 0f, endY = Float.POSITIVE_INFINITY)
            )
        )

        Column(modifier = Modifier.align(Alignment.BottomStart).padding(start = 15.dp, bottom = 20.dp).width(270.dp)) {
            // Hiển thị name từ building hoặc text mặc định
            Text(
                building?.name ?: "22, Bần Yên Nhân, Mỹ Hào",
                fontFamily = GilroyFont,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                lineHeight = 30.sp
            )

            // Hiển thị address từ building hoặc text mặc định
            Text(
                building?.address ?: "Hưng Yên, Việt Nam",
                fontFamily = GilroyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(35.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = painterResource(R.drawable.ic_room), contentDescription = "", tint = Color.White, modifier = Modifier.size(25.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("6 Phòng", fontFamily = GilroyFont, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.LightGray)
                Spacer(modifier = Modifier.width(30.dp))
                Box(modifier = Modifier.size(35.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = painterResource(R.drawable.ic_distance), contentDescription = "", tint = Color.White, modifier = Modifier.size(25.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("2.3 km", fontFamily = GilroyFont, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.LightGray)
            }
        }
    }
}
@Composable
fun HomeDetailFinancials(homeViewModel: HomeViewModel) {
    val revenueStats by homeViewModel.buildingRevenueStats.collectAsState()

    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tổng doanh thu
        Box(
            modifier = Modifier.weight(1f).height(110.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFFFF714B).copy(alpha = 0.1f))
                .padding(15.dp)
        ) {
            Column {
                Text("Tổng doanh thu", style = Semi18Text)
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text(formatRevenue(revenueStats.totalRevenue), style = Bold35Text)
                    Spacer(modifier = Modifier.weight(1f))
                    CircleButton()
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Doanh thu tháng
        Box(
            modifier = Modifier.weight(1f).height(110.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFFD6C9E7).copy(alpha = 0.3f))
                .padding(15.dp)
        ) {
            Column {
                Text("Doanh thu tháng", style = Semi18Text)
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text(formatRevenue(revenueStats.monthlyRevenue), style = Bold35Text)
                    Spacer(modifier = Modifier.weight(1f))
                    CircleButton()
                }
            }
        }
    }
}
@Composable
fun CircleButton() {
    Box(
        modifier = Modifier.size(40.dp).offset(y = (-5).dp)
            .clip(CircleShape).background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.right_arrow),
            contentDescription = "new tab",
            tint = Color.Unspecified,
            modifier = Modifier.size(15.dp)
        )
    }
}
@Composable
fun HomeDetailTabContent(
    navController: NavController,
    homeViewModel: HomeViewModel,
    buildingId: Int // Add buildingId parameter
) {
    var selectedTab by remember { mutableStateOf(0) }
    val activeColor = Color(0xFF34699A)
    val inactiveColor = Color.DarkGray

    // Collect rooms và tenants từ ViewModel
    val tenants by homeViewModel.tenantsByBuilding.collectAsState()

    // Collect rooms từ ViewModel
    val rooms by homeViewModel.rooms.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth().height(50.dp),
            verticalAlignment = Alignment.CenterVertically) {
            TabItem(title = "Phòng", selected = selectedTab == 0, onClick = { selectedTab = 0 }, modifier = Modifier.weight(1f), activeColor = activeColor, inactiveColor = inactiveColor)
            TabItem(title = "Khách thuê", selected = selectedTab == 1, onClick = { selectedTab = 1 }, modifier = Modifier.weight(1f), activeColor = activeColor, inactiveColor = inactiveColor)
        }
        Spacer(modifier = Modifier.height(10.dp))
        when (selectedTab) {
            0 -> {
                SearchBar()
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(10.dp)).background(
                        Color.White).border(1.dp, Color(0xFFD3AF37), RoundedCornerShape(10.dp)).clickable {
                        navController.navigate(Screen.MonthInvoiceAdd.route)
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.fillMaxHeight().width(40.dp).background(yellowColorButton)) {
                                Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp).size(18.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                                    Text("i", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD3AF37))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Khi cho thuê cả căn nhà cũng cần thêm phòng mới với tên phòng là tên căn nhà", fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 18.sp, modifier = Modifier.width(280.dp), color = Color.DarkGray)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // FIXED: Use string interpolation to build the route with buildingId
                    Box(modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(10.dp)).background(
                        Color.White).border(1.dp, Color(0xFFD3AF37), RoundedCornerShape(10.dp)).clickable {
                        navController.navigate("room_add/$buildingId")
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.fillMaxHeight().width(40.dp).background(
                                Color(0xFF08CB00).copy(alpha = 0.5f))) {
                                Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp).size(18.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                                    Text("i", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD3AF37))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Ấn vào đây để thêm thông tin phòng trọ mới.", fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 18.sp, modifier = Modifier.width(250.dp), color = Color.DarkGray)
                            IconButton(onClick = {}) {
                                Icon(painter = painterResource(R.drawable.ic_rightarrow), contentDescription = null, modifier = Modifier.size(30.dp), tint = Color(0xFFD3AF37))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(10.dp)).background(
                        Color.White).border(1.dp, Color(0xFFD3AF37), RoundedCornerShape(10.dp)).clickable {
                        navController.navigate(Screen.MonthInvoiceAdd.route)
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.fillMaxHeight().width(40.dp).background(yellowColorButton)) {
                                Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp).size(18.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                                    Text("i", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD3AF37))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Đã đến ngày thu tiền trọ. Ấn vào đây để lập hóa đơn mới.", fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 18.sp, modifier = Modifier.width(250.dp), color = Color.DarkGray)
                            IconButton(onClick = {}) {
                                Icon(painter = painterResource(R.drawable.ic_rightarrow), contentDescription = null, modifier = Modifier.size(30.dp), tint = Color(0xFFD3AF37))
                            }
                        }
                    }

                    if (rooms.isEmpty()) {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "BĐS chưa được thêm phòng",
                                fontFamily = GilroyFont,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    } else {
                        // Có phòng thì hiển thị danh sách
                        rooms.forEach { room ->
                            TabContentRoomCard(
                                room = room,
                                navController = navController
                            )
                        }
                    }
                }
            }
            1 -> {
                // Tab Khách thuê - sử dụng dữ liệu thực
                SearchBar()
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(10.dp)).background(
                    Color.White).border(1.dp, Color(0xFFD3AF37), RoundedCornerShape(10.dp)).clickable {
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.fillMaxHeight().width(40.dp).background(yellowColorButton)) {
                            Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp).size(18.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                                Text("i", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD3AF37))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Chọn phòng để lập hợp đồng và thêm thông tin khách thuê mới.", fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 18.sp, modifier = Modifier.width(250.dp))
                    }
                }

                Column {
                    if (tenants.isEmpty()) {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "Chưa có khách thuê nào",
                                fontFamily = GilroyFont,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    } else {
                        tenants.forEach { tenant ->
                            TabContentUserInfo(
                                tenant = tenant,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
// TAB USER
@Composable
fun TabContentUserInfo(tenant: TenantContract, navController: NavController) {
    Spacer(modifier = Modifier.height(20.dp))
    Box(modifier = Modifier.fillMaxSize().clickable (
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
    ) {
        navController.navigate("user_detail/${tenant.tenant_id}")
    }) {
        Row (horizontalArrangement = Arrangement.Center) {
            // Avatar mặc định - có thể random hoặc dựa trên ID
            val avatars = listOf(R.drawable.man, R.drawable.man2, R.drawable.man3, R.drawable.fox)
            val avatarIndex = tenant.tenant_id % avatars.size

            Icon(painter = painterResource(avatars[avatarIndex]), contentDescription = "", tint = Color.Unspecified, modifier = Modifier.size(50.dp))
            Column (modifier = Modifier.padding(start = 10.dp), verticalArrangement = Arrangement.Center) {
                Text(tenant.tenant_name, fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
                Text(tenant.room_name, fontFamily = GilroyFont, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.DarkGray)
                // Hiển thị status nếu cần
                Text(
                    text = when(tenant.contract_status) {
                        "active" -> "Đang thuê"
                        "pending" -> "Chờ duyệt"
                        else -> tenant.contract_status
                    },
                    fontFamily = GilroyFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = if (tenant.contract_status == "active") checkColor else wrongColor
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(
                yellowColorButton), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(R.drawable.ic_call), contentDescription = "", modifier = Modifier.size(15.dp), tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(
                yellowColorButton), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(R.drawable.ic_chat), contentDescription = "", modifier = Modifier.size(15.dp), tint = Color.Black)
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}
// TAB ROOMS
@Composable
fun TabContentRoomCard(
    room: Room, // Thay đổi để nhận Room object thay vì các parameters riêng lẻ
    navController: NavController
) {
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxSize().clickable (
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
    ) {
        // FIXED: Navigate với room_id thực tế
        navController.navigate("room_detail/${room.id}")
    }) {
        Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 10.dp)) {
            // Sử dụng AsyncImage thay vì Image với resource
            val painter = rememberAsyncImagePainter(
                model = if (room.image_url.isNullOrEmpty()) {
                    R.drawable.room2 // Ảnh mặc định nếu không có image_url
                } else {
                    "${RetrofitClient.IMAGE_URL}${room.image_url}"
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
                    Text(room.name, fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "${String.format("%.1f", room.rent_price / 1000000)} Triệu",
                        fontFamily = GilroyFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )
                }
                Text(
                    room.status ?: "Trống",
                    fontFamily = GilroyFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color(0xFF78C841)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(R.drawable.ic_more), contentDescription = "", modifier = Modifier.size(25.dp), tint = Color.Unspecified)
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
}