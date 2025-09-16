package com.example.smart_rent_application_landlord.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.models.Service
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.ui.screens.functions.RoomAddInfo
import com.example.smart_rent_application_landlord.ui.screens.functions.TwiceButton
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.backgroundBoxText
import com.example.smart_rent_application_landlord.ui.theme.lightYellowColor
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

fun formatPrice(price: Double?): String {
    return price?.let {
        String.format("%,.0f", it).replace(",", ".")
    } ?: "0"
}

fun getServiceIcon(serviceName: String): Int {
    return when (serviceName.lowercase()) {
        "tiền điện", "điện" -> R.drawable.ic_flash
        "tiền nước", "nước" -> R.drawable.ic_water
        else -> R.drawable.ic_flash
    }
}

@Composable
fun ServiceScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val services by homeViewModel.services.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            ServiceContent(navController, services, homeViewModel)
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ServiceContent(navController: NavController, services: List<Service>, homeViewModel: HomeViewModel) {
    services.forEach { service ->
        ServiceItem(
            navController = navController,
            service = service,
            icon = getServiceIcon(service.name),
            nameService = service.name,
            per = service.unit ?: "",
            cost = "${formatPrice(service.price_per_unit)} đ/${service.unit ?: ""}",
            isMandatory = service.is_mandatory ?: false,
            homeViewModel = homeViewModel
        )
    }
}

@Composable
fun ServiceItem(
    navController: NavController,
    service: Service,
    icon: Int,
    nameService: String,
    per: String,
    cost: String,
    isMandatory: Boolean = false,
    homeViewModel: HomeViewModel
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundBoxText)
            .clickable {
                navController.navigate(Screen.ServiceUpdate.createRoute(service.id))
            }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    nameService,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Text(
                    per,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    cost,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(
                            if (isMandatory) R.drawable.ic_dot2 else R.drawable.ic_dot2
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = if (isMandatory) Color.Green else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        if (isMandatory) "Đang áp dụng tất cả các phòng" else "Chưa áp dụng tự động",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(35.dp) // Đặt kích thước cho Box giống IconButton
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color.Red.copy(alpha = 0.2f), CircleShape)
                    .clickable (
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
                Column {
                    Text(
                        text = buildAnnotatedString {
                            append("Bạn có chắc chắn muốn xóa dịch vụ ")

                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Red.copy(alpha = 0.7f),
                                    fontFamily = GilroyFont
                                )
                            ) {
                                append("\"$nameService\"")
                            }

                            append(" ?")
                        },
                        fontFamily = GilroyFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Việc xóa sẽ loại bỏ dịch vụ khỏi tất cả các phòng và không thể hoàn tác.",
                        fontFamily = GilroyFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        homeViewModel.deleteService(service.id)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red.copy(alpha = 0.8f),
                        contentColor = Color.White
                    )
                ) {
                    Text("Xóa", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Hủy", color = Color.DarkGray, fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}