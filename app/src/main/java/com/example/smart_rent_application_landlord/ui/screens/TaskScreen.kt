package com.example.smart_rent_application_landlord.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.ui.components.SearchBar
import com.example.smart_rent_application_landlord.ui.theme.Bold30Text
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.Medium14Text
import com.example.smart_rent_application_landlord.ui.theme.Semi16Text
import com.example.smart_rent_application_landlord.ui.theme.Semi20Text
import com.example.smart_rent_application_landlord.ui.theme.checkColor
import com.example.smart_rent_application_landlord.ui.theme.lightCheckColor
import com.example.smart_rent_application_landlord.ui.theme.lightGreenMainColor
import com.example.smart_rent_application_landlord.ui.theme.lightWrongColor
import com.example.smart_rent_application_landlord.ui.theme.wrongColor
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton

@Composable
fun TaskScreen(navController: NavController, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            TaskHead()
            TaskContent(navController)
        }
    }
}
@Composable
fun TaskHead() {
    Row (modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text("Công việc", style = Semi20Text)
        Spacer(modifier = Modifier.width(15.dp))
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(yellowColorButton),
            contentAlignment = Alignment.Center) {
            IconButton(onClick = {}) {
                Icon(painter = painterResource(R.drawable.add), tint = Color.Unspecified, contentDescription = "add", modifier = Modifier.size(15.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    SearchBar()
}
@Composable
fun TaskContent(navController: NavController) {
    Spacer(modifier = Modifier.height(10.dp))
    TaskItemHouse("Phường Mỹ Hào, Hưng Yên")
    TaskItem("Chưa hoàn thành","Sửa điều hòa","Phòng 103", image = R.drawable.room3, navController = navController, isDone = false)
    TaskItem("Hoàn thành","Sửa điều hòa","Phòng 103", image = R.drawable.room1, navController = navController, isDone = true)
    Spacer(modifier = Modifier.height(40.dp))
    TaskItemHouse("Việt Tiến, Hưng Yên")
    TaskItem("Chưa hoàn thành","Sửa điều hòa","Phòng 103", image = R.drawable.room2, navController = navController, isDone = false)
    TaskItem("Hoàn thành","Sửa điều hòa","Phòng 103", image = R.drawable.room4, navController = navController, isDone = true)
    TaskItem("Chưa hoàn thành","Sửa điều hòa","Phòng 103", image = R.drawable.room5, navController = navController, isDone = false)
    TaskItem("Chưa hoàn thành","Sửa điều hòa","Phòng 103", image = R.drawable.room3, navController = navController, isDone = false)
}
@Composable
fun TaskItemHouse(address: String) {
    Box(modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(10.dp))) {
        Image(painter = painterResource(R.drawable.house2), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Box(modifier = Modifier
            .matchParentSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.8f),
                        Color.Transparent,
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
        ) {
            Column (modifier = Modifier.width(270.dp).padding(start = 10.dp, top = 10.dp, bottom = 10.dp)) {
                Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(15.dp)).background(Color.Gray.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
                        Box(modifier = Modifier.size(25.dp).clip(CircleShape).background(Color.White),
                            contentAlignment = Alignment.Center) {
                            Icon(painter = painterResource(R.drawable.ic_pin), contentDescription = "pin", modifier = Modifier.size(15.dp), tint = Color(0xFF3F7D58))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(address, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.White, lineHeight = 30.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}
@Composable
fun TaskItem(name: String, title: String, roomID: String, image: Int, navController: NavController, isDone: Boolean) {
    Spacer(modifier = Modifier.height(5.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp)).clickable { navController.navigate("tenant_detail") }) {
        Row (modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 10.dp, top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(image), contentDescription = "", modifier = Modifier.clip(
                RoundedCornerShape(5.dp)).size(55.dp), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.wrapContentWidth()) {
                Text(title, fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(5.dp))
                Row (modifier = Modifier.padding(end = 10.dp)) {
                    Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(7.dp)).background(
                        lightGreenMainColor.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center) {
                        Text(roomID, fontWeight = FontWeight.Normal, fontSize = 14.sp, color = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(7.dp)).background(if (isDone) Color(0xFFCADCAE).copy(alpha = 0.3f) else Color(0xFFFFD1D1).copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                        Text(name, fontFamily = GilroyFont, fontWeight = FontWeight.Normal, fontSize = 14.sp, color = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(R.drawable.ic_more), contentDescription = "", modifier = Modifier.size(25.dp), tint = Color.Unspecified)
        }
    }
}