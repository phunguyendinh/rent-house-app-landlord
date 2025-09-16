package com.example.smart_rent_application_landlord.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.smart_rent_application_landlord.R

sealed class Screen(
    val route: String,
    val label: String,
    val icon: Int,
    val showBottomBar: Boolean = true,
    val showFloatingButton: Boolean = false
) {
    object Home : Screen("home", "Home", R.drawable.ic_home)
    object Task : Screen("task", "Task", R.drawable.ic_planning)
    object Service : Screen("service", "Service", R.drawable.ic_service, showFloatingButton = true)

    // Cập nhật HomeDetail để nhận building_id parameter
    object HomeDetail : Screen("home_detail/{building_id}", "HomeDetail", R.drawable.ic_planning) {
        fun createRoute(buildingId: Int) = "home_detail/$buildingId"
    }

    object RoomDetail : Screen("room_detail/{room_id}", "RoomDetail", R.drawable.ic_planning) {
        fun createRoute(roomId: Int) = "room_detail/$roomId"
    }
    object UserDetail : Screen("user_detail/{tenantId}", "UserDetail", R.drawable.ic_planning) {
        fun createRoute(tenantId: Int) = "user_detail/$tenantId"
    }
    object LeaseDetail : Screen("lease_detail", "LeaseDetail", R.drawable.ic_planning)
    object LeaseUpdate : Screen("lease_update", "LeaseUpdate", R.drawable.ic_planning)
    object HomeAdd : Screen("home_add", "HomeAdd", R.drawable.ic_planning)
    object RoomAdd : Screen("room_add", "RoomAdd", R.drawable.ic_planning)
    object ServiceAdd : Screen("service_add", "ServiceAdd", R.drawable.ic_planning)
    object InvoiceAdd : Screen("invoice_add", "InvoiceAdd", R.drawable.ic_planning)
    object MonthInvoiceAdd : Screen("month_invoice_add", "MonthInvoiceAdd", R.drawable.ic_planning)
    // Trong Screen.kt
    object MonthInvoiceDetail : Screen("month_invoice_detail/{bill_id}", "MonthInvoiceDetail", R.drawable.ic_planning) {
        fun createRoute(billId: Int) = "month_invoice_detail/$billId"
    }    object ServiceDeal : Screen("service_deal", "ServiceDeal", R.drawable.ic_planning)
    object ServiceUpdate : Screen("service_update/{service_id}", "ServiceUpdate", R.drawable.ic_planning) {
        fun createRoute(serviceId: Int) = "service_update/$serviceId"
    }
    object RoomEditService : Screen("room_edit_service", "RoomEditService", R.drawable.ic_planning)

}

fun getScreenFromRoute(route: String?): Screen? {
    return when {
        route == Screen.Home.route -> Screen.Home
        route == Screen.Task.route -> Screen.Task
        route == Screen.Service.route -> Screen.Service
        route?.startsWith("home_detail/") == true -> Screen.HomeDetail // Cập nhật để match với parameter
        route == Screen.RoomDetail.route -> Screen.RoomDetail
        route == Screen.UserDetail.route -> Screen.UserDetail
        route == Screen.LeaseDetail.route -> Screen.LeaseDetail
        route == Screen.LeaseUpdate.route -> Screen.LeaseUpdate
        route == Screen.HomeAdd.route -> Screen.HomeAdd
        route == Screen.RoomAdd.route -> Screen.RoomAdd
        route == Screen.ServiceAdd.route -> Screen.ServiceAdd
        route == Screen.InvoiceAdd.route -> Screen.InvoiceAdd
        route == Screen.MonthInvoiceAdd.route -> Screen.MonthInvoiceAdd
        route == Screen.MonthInvoiceDetail.route -> Screen.MonthInvoiceDetail
        route == Screen.ServiceDeal.route -> Screen.ServiceDeal
        route == Screen.RoomEditService.route -> Screen.RoomEditService
        route?.startsWith("service_update/") == true -> Screen.ServiceUpdate // THÊM MỚI
        else -> null
    }
}