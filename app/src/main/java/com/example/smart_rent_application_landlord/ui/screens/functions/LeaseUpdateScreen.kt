package com.example.smart_rent_application_landlord.ui.screens.functions

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.ui.screens.details.LeaseDetailInfo
import com.example.smart_rent_application_landlord.ui.screens.details.LeaseDetailInfoItem
import com.example.smart_rent_application_landlord.ui.screens.details.LeaseDetailStatus
import com.example.smart_rent_application_landlord.ui.screens.details.LeaseDetailUserInfo
import com.example.smart_rent_application_landlord.ui.screens.details.LeasePropertyDetail
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.lightYellowColor
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import java.time.LocalDate

@Composable
fun LeaseUpdateScreen(navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            UpdateLeaseHouseInfo(navController)
            UpdateLeaseInfo()
        }
    }
}
@Composable
fun UpdateLeaseHouseInfo(navController: NavController) {
    Spacer(modifier = Modifier.height(10.dp))
    Row (verticalAlignment = Alignment.Top) {
        Image(painter = painterResource(R.drawable.house2), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier
            .size(110.dp)
            .clip(
                RoundedCornerShape(10.dp)
            ))
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text("22, Bần Yên Nhân", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text("22, Bần Yên Nhân, Mỹ Hào, Hưng Yên", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(5.dp))
            Row (modifier = Modifier.clickable { navController.navigate(Screen.RoomDetail.route) }){
                Text("Phòng 101", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(5.dp))
                Icon(painter = painterResource(R.drawable.right_arrow1), contentDescription = "", modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp), tint = Color.Black)
            }
        }
    }
}
@Composable
fun UpdateLeaseInfo() {
    Spacer(modifier = Modifier.height(20.dp))
    LeaseDetailInfoItem("Tên người thuê", "Nguyễn Đình Phú")
    LeaseDetailInfoItem("Số ngày đến khi hết hợp đồng", "12 Ngày")
    Spacer(modifier = Modifier.height(10.dp))
    var startDate by remember { mutableStateOf("08/08/2025") } // Ngày bắt đầu từ ngoài truyền vào
    var endDate by remember { mutableStateOf("08/09/2025") }   // Ngày kết thúc cũng từ ngoài
    ContractScreen(
        startDate = startDate,
        endDate = endDate,
        onEndDateChange = { newEnd -> endDate = newEnd }
    )
}

@Composable
fun ContractScreen(
    startDate: String,
    endDate: String,
    onEndDateChange: (String) -> Unit
) {
    var selectedItem by remember { mutableStateOf("1 tháng") }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text("Bắt đầu hợp đồng", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        DisabledDateTextField(startDate)

        Spacer(modifier = Modifier.height(20.dp))

        Text("Kết thúc hợp đồng", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        DisabledDateTextField(endDate)

        Spacer(modifier = Modifier.height(20.dp))

        Text("Gia hạn", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            UpdateItem("1 tháng", selectedItem) {
                selectedItem = it
                onEndDateChange(calculateEndDate(startDate, 1))
            }
            UpdateItem("6 tháng", selectedItem) {
                selectedItem = it
                onEndDateChange(calculateEndDate(startDate, 6))
            }
            UpdateItem("1 năm", selectedItem) {
                selectedItem = it
                onEndDateChange(calculateEndDate(startDate, 12))
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
        TwiceButton()
    }
}

// Hàm cộng tháng
fun calculateEndDate(startDate: String, monthsToAdd: Int): String {
    if (startDate.isBlank()) return ""
    val formatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return try {
        val date = formatter.parse(startDate)
        val cal = java.util.Calendar.getInstance()
        cal.time = date!!
        cal.add(java.util.Calendar.MONTH, monthsToAdd)
        formatter.format(cal.time)
    } catch (e: Exception) {
        ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisabledDateTextField(dateString: String) {
    Spacer(modifier = Modifier.height(10.dp))
    TextField(value = dateString, onValueChange = { },
        enabled = false, // Disable input
        readOnly = true, // Chỉ đọc
        label = { Text("Ngày tháng năm", fontFamily = GilroyFont) },
        modifier = Modifier
            .fillMaxWidth().border(1.dp, Color.LightGray, RoundedCornerShape(10.dp)),

        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = Color.Black,
            disabledLabelColor = Color.Gray,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.White,
        ),
    )
}
@Composable
fun UpdateItem(text: String, selectedItem: String, onSelect: (String) -> Unit) {
    var isSelected = text == selectedItem
    Box(modifier = Modifier.size(90.dp, 30.dp).clip(RoundedCornerShape(7.dp)).background(if (isSelected) yellowColorButton.copy(alpha = 0.5f) else Color.White).border(1.dp, Color.LightGray, RoundedCornerShape(7.dp))
        .clickable (
            interactionSource = remember { MutableInteractionSource() },
            indication = null, // ✅ TẮT ripple xám
        ) { onSelect(text) },
        contentAlignment = Alignment.Center) {
        Text(text, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
    Spacer(modifier = Modifier.width(10.dp))
}
@Composable
fun TwiceButton(
    text: String = "Lưu",
    onCloseClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onCloseClick, // Thay đổi
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
        Button(onClick = onSaveClick, // Thay đổi
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF006FFD),
                contentColor = Color.White
            ),
            modifier = Modifier.height(45.dp)
        ) {
            Text(text, fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        }
    }
}