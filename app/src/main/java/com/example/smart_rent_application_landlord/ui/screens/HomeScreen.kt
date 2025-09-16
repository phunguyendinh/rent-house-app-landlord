package com.example.smart_rent_application_landlord.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.models.houseList
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.services.RetrofitClient
import com.example.smart_rent_application_landlord.ui.components.HomeFilterRow
import com.example.smart_rent_application_landlord.ui.components.HouseCard
import com.example.smart_rent_application_landlord.ui.components.SearchBar
import com.example.smart_rent_application_landlord.ui.theme.Bold35Text
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.Medium16Text
import com.example.smart_rent_application_landlord.ui.theme.Semi18Text
import com.example.smart_rent_application_landlord.ui.theme.Semi20Text
import com.example.smart_rent_application_landlord.ui.theme.lightGreenMainColor
import com.example.smart_rent_application_landlord.ui.theme.lightYellowColor
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel
import java.text.DecimalFormat
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            HomeDashboard(homeViewModel) // Pass ViewModel to dashboard
            HomeProperties(navController, homeViewModel)
        }
    }
}

@Composable
fun HomeDashboard(homeViewModel: HomeViewModel) {
    // Collect dashboard statistics from ViewModel
    val dashboardStats by homeViewModel.dashboardStats.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(190.dp, 130.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFF08CB00).copy(alpha = 0.1f))
                .padding(start = 15.dp, end = 15.dp, bottom = 0.dp, top = 10.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Box(
                    modifier = Modifier
                        .padding(start = 110.dp)
                        .size(50.dp, 25.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "${dashboardStats.totalTenants}+",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
                Row(modifier = Modifier.padding(top = 15.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.man),
                        contentDescription = "man",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified
                    )
                    Icon(
                        painter = painterResource(R.drawable.man2),
                        contentDescription = "man",
                        modifier = Modifier.size(30.dp).offset(x = (-10).dp),
                        tint = Color.Unspecified
                    )
                    Icon(
                        painter = painterResource(R.drawable.man3),
                        contentDescription = "man",
                        modifier = Modifier.size(30.dp).offset(x = (-20).dp),
                        tint = Color.Unspecified
                    )
                    Icon(
                        painter = painterResource(R.drawable.fox),
                        contentDescription = "man",
                        modifier = Modifier.size(30.dp).offset(x = (-30).dp),
                        tint = Color.Unspecified
                    )
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .offset(x = (-40).dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFAF9EE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 25.sp, fontWeight = FontWeight.Normal)
                    }
                }
                Row(modifier = Modifier.padding(top = 9.dp)) {
                    Text("Người thuê", style = Semi18Text)
                    Box(
                        modifier = Modifier
                            .padding(start = 25.dp)
                            .size(40.dp)
                            .offset(y = (-10).dp)
                            .clip(CircleShape)
                            .background(Color.White),
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
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .size(145.dp, 130.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(lightYellowColor.copy(alpha = 0.3f))
                .padding(15.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text("Phòng trống", style = Semi18Text)
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text("${dashboardStats.emptyRooms}", style = Bold35Text)
                    Box(
                        modifier = Modifier
                            .padding(start = 40.dp)
                            .size(40.dp)
                            .offset(y = (-5).dp)
                            .clip(CircleShape)
                            .background(Color.White),
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
                Spacer(modifier = Modifier.weight(1f))
                Text("Tổng: ${dashboardStats.totalRooms}", style = Medium16Text)
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(110.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFFFF714B).copy(alpha = 0.1f))
                .padding(15.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text("Tổng doanh thu", style = Semi18Text)
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text(
                        formatRevenue(dashboardStats.totalRevenue),
                        style = Bold35Text
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(y = (-5).dp)
                            .clip(CircleShape)
                            .background(Color.White),
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
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(110.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFFD6C9E7).copy(alpha = 0.3f))
                .padding(15.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text("Doanh thu tháng", style = Semi18Text)
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text(
                        formatRevenue(dashboardStats.monthlyRevenue),
                        style = Bold35Text
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(y = (-5).dp)
                            .clip(CircleShape)
                            .background(Color.White),
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
            }
        }
    }
}

// Helper function to format revenue display
public fun formatRevenue(revenue: Double): String {
    return when {
        revenue >= 1_000_000 -> {
            val millions = revenue / 1_000_000
            "${DecimalFormat("#.#").format(millions)}M"
        }
        revenue >= 1_000 -> {
            val thousands = revenue / 1_000
            "${DecimalFormat("#.#").format(thousands)}k"
        }
        else -> {
            "${revenue.roundToInt()}"
        }
    }
}

@Composable
fun HomeProperties(navController: NavController, viewModel: HomeViewModel) {
    val buildings by viewModel.buildings.collectAsState()

    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Text("Nhà cho thuê", style = Semi20Text)
        Spacer(modifier = Modifier.width(15.dp))
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(yellowColorButton),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = { navController.navigate(Screen.HomeAdd.route) }) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    tint = Color.Unspecified,
                    contentDescription = "add",
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    SearchBar()
    HomeFilterRow(viewModel = viewModel)

    // Display buildings
    buildings.forEach { building ->
        HouseCard(
            buildingId = building.id,
            imageUrl = "${RetrofitClient.IMAGE_URL}${building.image_url}",
            address1 = building.name,
            address2 = building.address,
            navController = navController
        )
    }
}