package com.example.smart_rent_application_landlord.ui.screens.details

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.models.ContractWithTenant
import com.example.smart_rent_application_landlord.models.Room
import com.example.smart_rent_application_landlord.models.TenantServiceBill
import com.example.smart_rent_application_landlord.models.TenantServiceBill1
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.services.RetrofitClient
import com.example.smart_rent_application_landlord.ui.components.SearchBar
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.checkColor
import com.example.smart_rent_application_landlord.ui.theme.lightGreenMainColor
import com.example.smart_rent_application_landlord.ui.theme.lightYellowColor
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

@Composable
fun RoomDetailScreen(
    navController: NavController,
    roomId: Int,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    // Fetch room detail, service bills và active contract khi screen được tạo
    LaunchedEffect(roomId) {
        homeViewModel.fetchRoomById(roomId)
        homeViewModel.fetchServiceBillsByRoomId(roomId)
        homeViewModel.fetchActiveContractByRoomId(roomId) // Thêm dòng này
    }

    val room by homeViewModel.currentRoom.collectAsState()
    val serviceBills by homeViewModel.roomServiceBills.collectAsState()
    val activeContract by homeViewModel.activeContract.collectAsState() // Thêm dòng này

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            RoomDetailImagePreview(room = room)
            RoomDetailUserInfo(navController, room = room, activeContract = activeContract) // Truyền thêm activeContract
            RoomDetailTasks(
                navController,
                room = room,
                serviceBills = serviceBills,
                activeContract = activeContract, // Thêm dòng này
                homeViewModel = homeViewModel    // Thêm dòng này
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun RoomDetailImagePreview(room: Room? = null) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Sử dụng image từ room hoặc ảnh mặc định
        val painter = rememberAsyncImagePainter(
            model = if (room?.image_url.isNullOrEmpty()) {
                R.drawable.roomdetail1 // Ảnh mặc định
            } else {
                "${RetrofitClient.IMAGE_URL}${room?.image_url}"
            }
        )

        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.fillMaxWidth().height(350.dp),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),  // mờ từ trên
                            Color.Black.copy(alpha = 0.7f)   // đậm hơn ở dưới
                        )
                    )
                )
        ) {
            Column (modifier = Modifier.padding(top = 45.dp, start = 15.dp, end = 15.dp)) {
                Row {
                    Column {
                        Spacer(modifier = Modifier.height(175.dp))
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            "${String.format("%.1f", (room?.rent_price ?: 0.0) / 1000000)} Triệu VNĐ/Tháng",
                            fontFamily = GilroyFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            "${room?.name ?: "N/A"}",
                            fontFamily = GilroyFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(10.dp)).background(
                            Color.White.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 5.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)) {
                                Box(modifier = Modifier.size(25.dp).clip(CircleShape).background(Color.White),
                                    contentAlignment = Alignment.Center) {
                                    Icon(painter = painterResource(R.drawable.ic_pin), contentDescription = "pin", modifier = Modifier.size(15.dp), tint = Color(0xFF3F7D58))
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    room?.status ?: "Trống",
                                    fontFamily = GilroyFont,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoomDetailUserInfo(
    navController: NavController,
    room: Room? = null,
    activeContract: ContractWithTenant? = null // Thêm parameter này
) {
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxSize().padding(start = 18.dp, end = 18.dp).clip(
        RoundedCornerShape(10.dp)
    ).background(lightYellowColor.copy(alpha = 0.2f))
        .clickable (
            interactionSource = remember { MutableInteractionSource() },
            indication = null, // ✅ TẮT ripple xám
        ) {
            // Navigate to user detail if there's a tenant
            activeContract?.let { contract ->
                // navController.navigate("user_detail/${contract.tenant_id}")
            }
        }) {
        RoomDetailUserCard(
            name = activeContract?.tenant_name ?: "Chưa có khách thuê", // Sử dụng tenant_name từ contract
            roomInfo = if (activeContract != null) {
                "Trạng thái: ${
                    when(activeContract.status) {
                        "active" -> "Đang thuê"
                        "pending" -> "Chờ xác nhận"
                        "expired" -> "Hết hạn"
                        "terminated" -> "Đã kết thúc"
                        else -> activeContract.status
                    }
                }"
            } else {
                "Tối đa: ${room?.max_occupants ?: 0} người"
            },
            avatar = R.drawable.man,
            hasContract = activeContract != null // Thêm parameter để biết có contract hay không
        )
    }
}

@Composable
fun RoomDetailUserCard(
    name: String,
    roomInfo: String,
    avatar: Int,
    hasContract: Boolean = false // Thêm parameter này
) {
    Row (modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(avatar), contentDescription = "", tint = Color.Unspecified, modifier = Modifier.size(50.dp))
        Column (modifier = Modifier.padding(start = 10.dp), verticalArrangement = Arrangement.Center) {
            Text(name, fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(roomInfo, fontFamily = GilroyFont, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.DarkGray)
        }
        Spacer(modifier = Modifier.weight(1f))

        // Chỉ hiển thị nút call và chat khi có contract
        if (hasContract) {
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
}
@Composable
fun RoomDetailTasks(
    navController: NavController,
    room: Room? = null,
    serviceBills: List<TenantServiceBill1> = emptyList(),
    activeContract: ContractWithTenant? = null, // Thêm parameter này
    homeViewModel: HomeViewModel // Thêm parameter này để gọi API
) {
    var showInvitationDialog by remember { mutableStateOf(false) }
    var invitationCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current

    Spacer(modifier = Modifier.height(10.dp))
    Column(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp)) {

        // Chỉ hiển thị phần tạo mã mời nếu phòng chưa có khách thuê
        if (activeContract == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFD3AF37), RoundedCornerShape(10.dp))
                    .clickable {
                        // Gọi API tạo mã mời
                        room?.let {
                            isLoading = true
                            homeViewModel.createRoomInvitation(
                                roomId = it.id,
                                onSuccess = { code ->
                                    invitationCode = code
                                    showInvitationDialog = true
                                    isLoading = false
                                },
                                onError = { error ->
                                    isLoading = false
                                    // Handle error - có thể hiển thị toast hoặc dialog
                                }
                            )
                        }
                    }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(40.dp)
                            .background(yellowColorButton)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp)
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "i",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD3AF37)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        if (isLoading) "Đang tạo mã mời..." else "Phòng vẫn còn chỗ. Ấn vào để tạo mã mời cho khách thuê.",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.width(250.dp)
                    )
                    if (!isLoading) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.ic_rightarrow),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = Color(0xFFD3AF37)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Tiền phòng",
                fontFamily = GilroyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.DarkGray
            )
        }
        SearchBar()
        Column {
            Spacer(modifier = Modifier.height(20.dp))

            if (serviceBills.isEmpty()) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Chưa có hóa đơn nào",
                        fontFamily = GilroyFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            } else {
                serviceBills.forEach { bill ->
                    RoomMonthPay(bill = bill, navController = navController)
                }
            }
        }
    }

    // Dialog hiển thị mã mời
    if (showInvitationDialog) {
        InvitationCodeDialog(
            invitationCode = invitationCode,
            onDismiss = { showInvitationDialog = false },
            onCopy = {
                clipboardManager.setText(AnnotatedString(invitationCode))
                // Có thể hiển thị toast "Đã copy mã mời"
            }
        )
    }
}

@Composable
fun InvitationCodeDialog(
    invitationCode: String,
    onDismiss: () -> Unit,
    onCopy: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Mã mời phòng trọ",
                fontFamily = GilroyFont,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    "Mã mời đã được tạo thành công! Chia sẻ mã này với khách thuê:",
                    fontFamily = GilroyFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Mã mời có thể được chọn và copy
                SelectionContainer {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Gray.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            invitationCode,
                            fontFamily = GilroyFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFFD3AF37)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Mã có hiệu lực trong 7 ngày",
                    fontFamily = GilroyFont,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onCopy()
                    onDismiss()
                }
            ) {
                Text(
                    "Copy mã",
                    fontFamily = GilroyFont,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD3AF37)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Đóng",
                    fontFamily = GilroyFont,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
fun RoomMonthPay(bill: TenantServiceBill1, navController: NavController) {
    val isPaid = bill.payment_status == "paid"

    Box(modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() },
        indication = null) {
        navController.navigate("month_invoice_detail/${bill.bill_id}")
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Tháng ${bill.month}/${bill.year}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text("${String.format("%.0f", bill.total_amount)} VND", fontWeight = FontWeight.Normal, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(7.dp)).background(
                if (isPaid) Color.Green.copy(alpha = 0.1f) else Color(0xFFFFD1D1).copy(alpha = 0.3f)
            ), contentAlignment = Alignment.Center) {
                Text(
                    text = if (isPaid) "Đã thanh toán" else "Chưa thanh toán",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                    color = if (isPaid) checkColor.copy(alpha = 0.9f) else Color.Red.copy(alpha = 0.5f)
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))
            IconButton(onClick = {}) {
                Icon(painter = painterResource(R.drawable.ic_more2), contentDescription = null, modifier = Modifier.size(13.dp), tint = Color.Unspecified)
            }
        }
    }
    Spacer(modifier = Modifier.height(25.dp))
}