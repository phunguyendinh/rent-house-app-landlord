package com.example.smart_rent_application_landlord.ui.screens.functions

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.models.Service
import com.example.smart_rent_application_landlord.ui.screens.formatPrice
import com.example.smart_rent_application_landlord.ui.screens.getServiceIcon
import com.example.smart_rent_application_landlord.ui.theme.backgroundBoxText
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

@Composable
fun RoomEditServiceScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val services by homeViewModel.services.collectAsState()
    // SỬ DỤNG STATE TỪ VIEWMODEL thay vì parameter
    val selectedServices by homeViewModel.selectedRoomServices

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            ServiceSelectionContent(
                navController = navController,
                services = services,
                selectedServices = selectedServices,
                onServiceToggle = { service ->
                    // GỌI VIEWMODEL METHOD để toggle service
                    homeViewModel.toggleRoomService(service)
                },
                homeViewModel = homeViewModel
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ServiceSelectionContent(
    navController: NavController,
    services: List<Service>,
    selectedServices: List<Service>,
    onServiceToggle: (Service) -> Unit,
    homeViewModel: HomeViewModel
) {
    services.forEach { service ->
        val isSelected = selectedServices.any { it.id == service.id }

        ServiceSelectionItem(
            navController = navController,
            service = service,
            icon = getServiceIcon(service.name),
            nameService = service.name,
            per = service.unit ?: "",
            cost = "${formatPrice(service.price_per_unit)} đ/${service.unit ?: ""}",
            isMandatory = service.is_mandatory ?: false,
            isSelected = isSelected,
            onToggle = { onServiceToggle(service) }
        )
    }
}

@Composable
fun ServiceSelectionItem(
    navController: NavController,
    service: Service,
    icon: Int,
    nameService: String,
    per: String,
    cost: String,
    isMandatory: Boolean = false,
    isSelected: Boolean = false,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isSelected) backgroundBoxText.copy(alpha = 0.5f)
                else backgroundBoxText
            )
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
                    color = if (isSelected) Color.DarkGray.copy(alpha = 0.7f) else Color.DarkGray
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
                    color = if (isSelected) Color.DarkGray.copy(alpha = 0.7f) else Color.DarkGray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_dot2),
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

            // Nút Plus/Minus tùy theo trạng thái
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .background(Color.White, CircleShape)
                    .border(
                        1.dp,
                        if (isSelected) Color.Red.copy(alpha = 0.2f) else Color.Blue.copy(alpha = 0.5f),
                        CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        if (isSelected) R.drawable.ic_minus else R.drawable.ic_plus
                    ),
                    contentDescription = if (isSelected) "Remove service" else "Add service",
                    modifier = Modifier.size(10.dp),
                    tint = if (isSelected) Color.Red else Color.Blue.copy(alpha = 0.5f)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}