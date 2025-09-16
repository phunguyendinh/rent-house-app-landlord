package com.example.smart_rent_application_landlord.ui.screens.functions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.ui.screens.details.HomeDetailCard
import com.example.smart_rent_application_landlord.ui.screens.details.HomeDetailFinancials
import com.example.smart_rent_application_landlord.ui.screens.details.HomeDetailTabContent
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.backgroundBoxText
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton

@Composable
fun MonthInvoiceScreen(navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            MonthInvoiceInfo(navController)
            Spacer(modifier = Modifier.height(20.dp))
            TwiceButton("Gửi")
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Composable
fun MonthInvoiceInfo(navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(10.dp)).background(
        Color.White).border(1.dp, Color(0xFFD3AF37), RoundedCornerShape(10.dp)).clickable {
        navController.navigate(Screen.RoomAdd.route)
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.fillMaxHeight().width(40.dp).background(
                Color(0xFF08CB00).copy(alpha = 0.5f))) {
                Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp).size(18.dp).clip(
                    CircleShape
                ).background(Color.White), contentAlignment = Alignment.Center) {
                    Text("i", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD3AF37))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Lập hóa đơn tháng (T.8/2025)", fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 18.sp, modifier = Modifier.width(250.dp))
                Spacer(modifier = Modifier.height(5.dp))
                Text("Hạn đóng tiền (3/9/2025)", fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 18.sp, modifier = Modifier.width(250.dp))
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
                Text("Thu tiền hàng tháng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("vào 30/8/2025. Chu kì 1 tháng.", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().height(70.dp).clip(RoundedCornerShape(10.dp)).background(yellowColorButton.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)) {
            Column {
                Text("Tháng 8/2025", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                Text("x1.500.000đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text("Thành tiền", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                Text("1.500.000đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    }
    // Dịch vụ tính tiền
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
                Text("Dịch vụ tính tiền", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Chốt mức khách sử dụng để tính tiền", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color.White).border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))) {
        Column() {
            Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 15.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Tiền điện", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                        Text("1.700đ/1KWh", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        Text("Số: [200-100]", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                        Text("100 KWh x 1.700đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                    }
                }
            }
            Divider(modifier = Modifier, 1.dp, Color.LightGray)
            Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 15.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Tiền nước", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                        Text("18.000đ/1 Khối", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        Text("Số: [120-100]", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                        Text("20 Khối x 18.000đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {navController.navigate(Screen.ServiceDeal.route)},
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
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().height(70.dp).clip(RoundedCornerShape(10.dp)).background(yellowColorButton.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)) {
            Column {
                Text("Tháng 8/2025", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                Text("100x1.700+20x18.000", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text("Thành tiền dịch vụ", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
                Text("530.000đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text("Tổng cộng", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(5.dp))
            Text("1.090.000đ", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
        }
    }
}