package com.example.smart_rent_application_landlord.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.ui.components.HouseCard
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton

@Composable
fun LeaseDetailScreen(navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            LeaseDetailUserInfo()
            LeaseDetailStatus(navController)
            LeaseDetailInfo()
            LeasePropertyDetail(navController)
        }
    }
}
@Composable
fun LeaseDetailUserInfo() {
    Row (verticalAlignment = Alignment.Top) {
        Image(painter = painterResource(R.drawable.house2), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier.size(100.dp).clip(
            RoundedCornerShape(10.dp)
        ))
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text("nguyendinhphu200@gmail.com", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
            Text("Nguyễn Đình Phú", fontWeight = FontWeight.SemiBold, fontSize = 20.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.padding(end = 20.dp)) {
                Box(modifier = Modifier.size(30.dp).clip(RoundedCornerShape(7.dp)).background(
                    yellowColorButton), contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(R.drawable.ic_call), contentDescription = "", modifier = Modifier.size(15.dp), tint = Color.Black)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.size(30.dp).clip(RoundedCornerShape(7.dp)).background(
                    yellowColorButton), contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(R.drawable.ic_chat), contentDescription = "", modifier = Modifier.size(15.dp), tint = Color.Black)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = painterResource(R.drawable.ic_more), contentDescription = "", modifier = Modifier.size(25.dp), tint = Color.Unspecified)
            }
        }
    }
}
@Composable
fun LeaseDetailStatus(navController: NavController) {
    var isPaid by remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(15.dp))
    Box(modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(10.dp)).background(
        Color(0xFFFBF5DE).copy(alpha = 0.5f)
    ), contentAlignment = Alignment.Center) {
        Row (modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Sắp hết hạn hợp đồng", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF3E3F29))
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { navController.navigate(Screen.LeaseUpdate.route) }, colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ), shape = RoundedCornerShape(10.dp),
            ) {
                Text("Gia hạn", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
    Box(modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(10.dp)).background(if (isPaid) Color(0xFFD3ECCD).copy(alpha = 0.3f) else Color(0xFFF8ECEC).copy(alpha = 0.5f)), // ✅ đổi màu nền
        contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = if (isPaid) "Đã thanh toán" else "Chưa thanh toán", fontWeight = if (isPaid) FontWeight.SemiBold else FontWeight.SemiBold, fontSize = 16.sp, color = if (isPaid) Color(0xFF06923E) else Color(0xFFE14434))
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { isPaid = !isPaid }, // ✅ Toggle trạng thái
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = if (isPaid) "Hoàn tác" else "Đánh dấu",
                    fontFamily = GilroyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
@Composable
fun LeaseDetailInfo() {
    Spacer(modifier = Modifier.height(25.dp))
    Row {
        Text("Địa chỉ thuê", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.weight(1f))
        Text("22, Bần Yên Nhân, Mỹ Hào", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
    }
    Spacer(modifier = Modifier.height(10.dp))
    Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
    Spacer(modifier = Modifier.height(15.dp))
    LeaseDetailInfoItem("Số phòng", "Phòng 101")
    LeaseDetailInfoItem("Giá/Tháng", "$1.2 Triệu")
    LeaseDetailInfoItem("Trạng thái thanh toán", "Chưa trả")
    LeaseDetailInfoItem("Ngày bắt đầu thuê", "21/3/2022")
    LeaseDetailInfoItem("Ngày hết hạn", "21/3/2023")
    LeaseDetailInfoItem("Thời hạn thuê (ngày)", "365 ngày")
    LeaseDetailInfoItem("Số ngày còn lại", "12 Ngày")
}
@Composable
fun LeaseDetailInfoItem(title: String, content: String) {
    Row {
        Text(title, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.weight(1f))
        Text(content, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
    }
    Spacer(modifier = Modifier.height(10.dp))
    Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
    Spacer(modifier = Modifier.height(15.dp))
}
@Composable
fun LeasePropertyDetail(navController: NavController) {
    Spacer(modifier = Modifier.height(10.dp))
//    HouseCard(imageRes = R.drawable.house2, address1 = "22, Bần Yên Nhân", address2 = "Quận 1, TP.HCM", navController = navController)
}