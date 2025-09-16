package com.example.smart_rent_application_landlord.ui.screens.details

import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.models.TenantContract1
import com.example.smart_rent_application_landlord.models.TenantInfo
import com.example.smart_rent_application_landlord.models.TenantServiceBill
import com.example.smart_rent_application_landlord.models.invoices
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.services.RetrofitClient
import com.example.smart_rent_application_landlord.ui.components.SearchBar
import com.example.smart_rent_application_landlord.ui.components.TabItem
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.checkColor
import com.example.smart_rent_application_landlord.ui.theme.lightGreenMainColor
import com.example.smart_rent_application_landlord.ui.theme.normalText
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

@Composable
fun UserDetailScreen(
    navController: NavController,
    tenantId: Int,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    // Fetch tenant detail khi screen được tạo
    LaunchedEffect(tenantId) {
        homeViewModel.fetchTenantDetailById(tenantId)
    }

    val tenantDetail by homeViewModel.tenantDetail.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            tenantDetail?.let { detail ->
                TenantDetailInfo(detail.tenant)
                TenantLeaseInfo(detail.contracts, detail.serviceBills, navController)
            } ?: run {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Đang tải thông tin khách thuê...")
                }
            }
        }
    }
}
@Composable
fun TenantDetailInfo(tenant: TenantInfo) {
    Column (modifier = Modifier.fillMaxSize()) {
        Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(R.drawable.man), contentDescription = "", modifier = Modifier.size(70.dp), tint = Color.Unspecified)
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Row {
                    Text(tenant.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                }
                Text(tenant.address ?: "Chưa có địa chỉ", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(R.drawable.ic_more), contentDescription = "", modifier = Modifier.size(25.dp), tint = Color.Unspecified)
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(R.drawable.ic_call), contentDescription = "", modifier = Modifier.size(15.dp), tint = Color.Unspecified)
            Spacer(modifier = Modifier.width(10.dp))
            Text(tenant.phone, fontSize = 14.sp, color = Color.DarkGray)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(R.drawable.ic_chat), contentDescription = "", modifier = Modifier.size(15.dp), tint = Color.Unspecified)
            Spacer(modifier = Modifier.width(10.dp))
            Text(tenant.email, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}
@Composable
fun TenantLeaseInfo(contracts: List<TenantContract1>, serviceBills: List<TenantServiceBill>, navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val activeColor = Color(0xFF34699A)
    val inactiveColor = Color.DarkGray

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth().height(50.dp),
            verticalAlignment = Alignment.CenterVertically) {
            TabItem(title = "Hợp đồng", selected = selectedTab == 0, onClick = { selectedTab = 0 }, modifier = Modifier.weight(1f), activeColor = activeColor, inactiveColor = inactiveColor)
            TabItem(title = "Tiền phòng", selected = selectedTab == 1, onClick = { selectedTab = 1 }, modifier = Modifier.weight(1f), activeColor = activeColor, inactiveColor = inactiveColor)
        }
        Spacer(modifier = Modifier.height(10.dp))
        when (selectedTab) {
            0 -> {
                SearchBar()
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    if (contracts.isEmpty()) {
                        Text("Không có hợp đồng nào", modifier = Modifier.padding(16.dp))
                    } else {
                        contracts.forEach { contract ->
                            TenantLeaseInfoItem(
                                contract = contract,
                                navController = navController
                            )
                        }
                    }
                }
            }
            1 -> {
                SearchBar()
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    if (serviceBills.isEmpty()) {
                        Text("Không có hóa đơn nào", modifier = Modifier.padding(16.dp))
                    } else {
                        serviceBills.forEach { bill ->
                            TenantMonthPay(
                                bill = bill,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun TenantLeaseInfoItem(contract: TenantContract1, navController: NavController) {
    val isActive = contract.contract_status == "active" &&
            (contract.leave_at == null || contract.leave_at.isEmpty())

    Box(modifier = Modifier.fillMaxSize().clickable (
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
    ) {
        navController.navigate("lease_detail/${contract.contract_id}")
    }) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            val painter = rememberAsyncImagePainter(
                model = if (contract.room_image.isNullOrEmpty()) {
                    R.drawable.room2
                } else {
                    "${RetrofitClient.IMAGE_URL}${contract.room_image}"
                }
            )
            Image(painter = painter, contentDescription = "", modifier = Modifier.size(60.dp).clip(
                RoundedCornerShape(10.dp)
            ), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(10.dp))
            Column () {
                Text(contract.building_address, fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray, modifier = Modifier.width(240.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(5.dp))
                Row {
                    Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(7.dp)).background(
                        lightGreenMainColor.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center) {
                        Text(contract.room_name, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(7.dp)).background(
                        if (isActive) Color.Green.copy(alpha = 0.1f) else Color(0xFFFFD1D1).copy(alpha = 0.3f)
                    ), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isActive) "Còn hạn" else "Hết hạn",
                            fontFamily = GilroyFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (isActive) Color(0xFF08CB00).copy(alpha = 0.9f) else Color.Red.copy(alpha = 0.5f),
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(R.drawable.ic_more), contentDescription = "", modifier = Modifier.size(25.dp), tint = Color.Unspecified)
        }
    }
    Spacer(modifier = Modifier.height(25.dp))
}

@Composable
fun TenantMonthPay(bill: TenantServiceBill, navController: NavController) {
    val isPaid = bill.payment_status == "paid"

    Box(modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() },
        indication = null) {
        navController.navigate("month_invoice_detail/${bill.bill_id}")
//        navController.navigate(Screen.MonthInvoiceDetail.route)
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