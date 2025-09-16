package com.example.smart_rent_application_landlord.ui.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.services.RetrofitClient
import com.example.smart_rent_application_landlord.services.ServiceBillDetail
import com.example.smart_rent_application_landlord.services.ServiceDetail
import com.example.smart_rent_application_landlord.ui.screens.functions.TwiceButton
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.backgroundBoxText
import com.example.smart_rent_application_landlord.ui.theme.checkColor
import com.example.smart_rent_application_landlord.ui.theme.wrongColor
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

// Thêm hàm này vào đầu file, trước @Composable
fun formatDateFromAPI(apiDate: String?): String {
    if (apiDate.isNullOrEmpty()) return ""

    return try {
        // Input format: "2025-08-15T00:00:00.000Z"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        // Output format: "15/08/2025"
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val date = inputFormat.parse(apiDate)
        date?.let { outputFormat.format(it) } ?: apiDate
    } catch (e: Exception) {
        try {
            val inputFormat2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val date = inputFormat2.parse(apiDate)
            date?.let { outputFormat.format(it) } ?: apiDate
        } catch (e2: Exception) {
            apiDate
        }
    }
}

@Composable
fun MonthInvoiceDetailScreen(
    navController: NavController,
    billId: Int,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    // Fetch service bill detail when screen is created
    LaunchedEffect(billId) {
        homeViewModel.fetchServiceBillDetail(billId)
    }

    val serviceBillDetail by homeViewModel.serviceBillDetail.collectAsState()

    var isMarkingPaid by remember { mutableStateOf(false) }
    // Fetch service bill detail when screen is created
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            serviceBillDetail?.let { detail ->
                MonthInvoiceDetailActionButton(navController)
                MonthInvoiceDetailInfo(detail)
                Spacer(modifier = Modifier.height(20.dp))

                // Show payment button only if not paid
                if (detail.status != "paid") {
                    // Payment action buttons
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                // Handle close/cancel action
                                navController.popBackStack()
                            },
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
                                isMarkingPaid = true  // SỬA LẠI
                                homeViewModel.markServiceBillAsPaid(
                                    billId = billId,
                                    onSuccess = {
                                        isMarkingPaid = false  // SỬA LẠI
                                        // Có thể thêm toast/snackbar để thông báo thành công
                                    },
                                    onError = { error ->
                                        isMarkingPaid = false  // SỬA LẠI
                                        // Có thể thêm toast/snackbar để thông báo lỗi
                                        Log.e("MonthInvoiceDetail", "Error: $error")
                                    }
                                )
                            },
                            enabled = !isMarkingPaid, // Disable khi đang xử lý
                            border = BorderStroke(1.dp, Color.LightGray),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF006FFD),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.height(45.dp)
                        ) {
                            if (isMarkingPaid) {
                                Text("Đang xử lý...", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                            } else {
                                Text("Đánh dấu thanh toán", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            } ?: run {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Đang tải thông tin hóa đơn...")
                }
            }
        }
    }
}

@Composable
fun MonthInvoiceDetailActionButton(navController: NavController) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)).background(backgroundBoxText.copy(alpha = 0.5f)).border(1.dp, Color.Black.copy(alpha = 0.3f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White).border(1.dp, Color.LightGray, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(R.drawable.ic_printing), contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Unspecified)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text("In phiếu", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            }
        }
        Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)).background(backgroundBoxText.copy(alpha = 0.5f)).border(1.dp, Color.Black.copy(alpha = 0.3f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White).border(1.dp, Color.LightGray, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(R.drawable.ic_share), contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Unspecified)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text("Gửi HĐ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            }
        }
        Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)).background(backgroundBoxText.copy(alpha = 0.5f)).border(1.dp, Color.Black.copy(alpha = 0.3f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White).border(1.dp, Color.LightGray, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(R.drawable.ic_call), contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Unspecified)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text("Gọi điện", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            }
        }
        Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)).background(backgroundBoxText.copy(alpha = 0.5f)).border(1.dp, Color.Red.copy(alpha = 0.3f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Red.copy(alpha = 0.5f)).border(1.dp, Color.LightGray, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(R.drawable.ic_close), contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text("Hủy HĐ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun MonthInvoiceDetailInfo(detail: ServiceBillDetail) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    val isPaid = detail.status == "paid"

    Spacer(modifier = Modifier.height(10.dp))

    // Header info
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color.White).border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${detail.building_name}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
            Text("${detail.building_address}", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Phòng: ${detail.room_name}", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.DarkGray)
            Text("Khách thuê: ${detail.tenant_name}", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Rent section
    Box(modifier = Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(10.dp)).background(yellowColorButton.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)) {
            Column {
                Text("Tiền thuê phòng", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                Text("${currencyFormat.format(detail.rent_price)}đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                Text("Tháng ${detail.month}/${detail.year}", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text("Thành tiền", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                Text("${currencyFormat.format(detail.rent_price)}đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Service fee summary
    val totalServiceAmount = detail.service_details.sumOf { it.total }
    if (detail.service_details.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(10.dp)).background(yellowColorButton.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)) {
                Column {
                    Text("Tiền dịch vụ", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                    Text("Tháng ${detail.month}/${detail.year}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text("Tổng tiền dịch vụ", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                    Text("${currencyFormat.format(totalServiceAmount)}đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Service details
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color.White).border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))) {
            Column {
                detail.service_details.forEachIndexed { index, service ->
                    ServiceDetailItem(service = service, currencyFormat = currencyFormat)
                    if (index < detail.service_details.size - 1) {
                        Divider(modifier = Modifier, thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Payment info section
    if (isPaid && !detail.payment_date.isNullOrEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color.Green.copy(alpha = 0.1f)).border(1.dp, Color.Green.copy(alpha = 0.3f), RoundedCornerShape(10.dp))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Thông tin thanh toán", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Ngày thanh toán: ${formatDateFromAPI(detail.payment_date)}", fontSize = 13.sp, color = Color.Gray)
                detail.payment_method?.let {
                    Text("Phương thức: $it", fontSize = 13.sp, color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }

    // Due date info (if not paid and has due date)
    if (!isPaid && !detail.due_date.isNullOrEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(wrongColor.copy(alpha = 0.1f)).border(1.dp, wrongColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Thông tin hạn thanh toán", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Hạn thanh toán: ${formatDateFromAPI(detail.due_date)}", fontSize = 14.sp, color = wrongColor.copy(alpha = 0.8f))            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }

    // Total amount
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text("Tổng tiền tháng này", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.Gray)
            Text("${currencyFormat.format(detail.total_amount)} đ", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(5.dp))
            Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(7.dp)).background(
                if (isPaid) Color.Green.copy(alpha = 0.1f) else Color(0xFFFFD1D1).copy(alpha = 0.5f)
            ), contentAlignment = Alignment.Center) {
                Text(
                    text = if (isPaid) "Đã thanh toán" else "Chưa thanh toán",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                    color = if (isPaid) checkColor.copy(alpha = 0.8f) else Color.Red.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun ServiceDetailItem(service: ServiceDetail, currencyFormat: NumberFormat) {
    Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 15.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(service.service_name, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                Text("${currencyFormat.format(service.price_per_unit)}đ/${service.unit}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)

                // Show proof image if available
                service.proof_image?.let { imageUrl ->
                    if (imageUrl.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(5.dp))
                        val painter = rememberAsyncImagePainter(
                            model = "${RetrofitClient.IMAGE_URL}$imageUrl"
                        )
                        androidx.compose.foundation.Image(
                            painter = painter,
                            contentDescription = "Ảnh chứng minh",
                            modifier = Modifier.size(40.dp, 30.dp).clip(RoundedCornerShape(5.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Số lượng: ${service.quantity}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                Text("${currencyFormat.format(service.total)}đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    }
}