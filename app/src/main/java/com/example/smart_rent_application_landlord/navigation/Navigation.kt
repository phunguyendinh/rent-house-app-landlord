package com.example.smart_rent_application_landlord.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smart_rent_application_landlord.ui.screens.HomeScreen
import com.example.smart_rent_application_landlord.ui.screens.ServiceScreen
import com.example.smart_rent_application_landlord.ui.screens.TaskScreen
import com.example.smart_rent_application_landlord.ui.screens.details.HomeDetailScreen
import com.example.smart_rent_application_landlord.ui.screens.details.LeaseDetailScreen
import com.example.smart_rent_application_landlord.ui.screens.details.MonthInvoiceDetailScreen
import com.example.smart_rent_application_landlord.ui.screens.details.RoomDetailScreen
import com.example.smart_rent_application_landlord.ui.screens.details.UserDetailScreen
import com.example.smart_rent_application_landlord.ui.screens.functions.HomeAddScreen
import com.example.smart_rent_application_landlord.ui.screens.functions.InvoiceAddScreen
import com.example.smart_rent_application_landlord.ui.screens.functions.LeaseUpdateScreen
import com.example.smart_rent_application_landlord.ui.screens.functions.MonthInvoiceScreen
import com.example.smart_rent_application_landlord.ui.screens.functions.RoomAddScreen
import com.example.smart_rent_application_landlord.ui.screens.functions.RoomEditServiceScreen
import com.example.smart_rent_application_landlord.ui.screens.functions.ServiceAddScreen
import com.example.smart_rent_application_landlord.ui.screens.functions.ServiceDealScreen
import com.example.smart_rent_application_landlord.ui.screens.functions.ServiceUpdateScreen
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues) {
    // Tạo ViewModel instance để chia sẻ giữa các screen
    val homeViewModel: HomeViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route, // Changed from Screen.RoomAdd.route
        modifier = Modifier.background(Color.White),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(Screen.Task.route) {
            TaskScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(Screen.Service.route) {
            ServiceScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        // HomeDetail with building_id parameter
        composable(
            route = Screen.HomeDetail.route,
            arguments = listOf(navArgument("building_id") { type = NavType.IntType })
        ) { backStackEntry ->
            val buildingId = backStackEntry.arguments?.getInt("building_id") ?: 0
            HomeDetailScreen(
                navController = navController,
                buildingId = buildingId,
                homeViewModel = homeViewModel,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(
            route = "room_detail/{room_id}",
            arguments = listOf(navArgument("room_id") { type = NavType.IntType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getInt("room_id") ?: 0
            RoomDetailScreen(
                navController = navController,
                roomId = roomId,
                homeViewModel = homeViewModel
            )
        }

        // Trong navigation setup
        composable("user_detail/{tenantId}") { backStackEntry ->
            val tenantId = backStackEntry.arguments?.getString("tenantId")?.toInt() ?: 0
            UserDetailScreen(navController, tenantId, homeViewModel, modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp))
        }

        composable(Screen.LeaseDetail.route) {
            LeaseDetailScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(Screen.LeaseUpdate.route) {
            LeaseUpdateScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(Screen.HomeAdd.route) {
            HomeAddScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        // RoomAdd with buildingId parameter - Fixed route pattern
        composable(
            route = "room_add/{buildingId}",
            arguments = listOf(navArgument("buildingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val buildingId = backStackEntry.arguments?.getInt("buildingId") ?: 0
            RoomAddScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                buildingId = buildingId,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(Screen.ServiceAdd.route) {
            ServiceAddScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(Screen.InvoiceAdd.route) {
            InvoiceAddScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(Screen.MonthInvoiceAdd.route) {
            MonthInvoiceScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        // Trong AppNavigation.kt
        composable(
            route = "month_invoice_detail/{bill_id}",
            arguments = listOf(navArgument("bill_id") { type = NavType.IntType })
        ) { backStackEntry ->
            val billId = backStackEntry.arguments?.getInt("bill_id") ?: 0
            MonthInvoiceDetailScreen(
                navController = navController,
                billId = billId,
                homeViewModel = homeViewModel,  // Pass the homeViewModel
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(Screen.ServiceDeal.route) {
            ServiceDealScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(
            route = Screen.ServiceUpdate.route,
            arguments = listOf(navArgument("service_id") { type = NavType.IntType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getInt("service_id") ?: 0
            ServiceUpdateScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                serviceId = serviceId,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }

        composable(Screen.RoomEditService.route) {
            RoomEditServiceScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                modifier = Modifier.padding(paddingValues).padding(start = 20.dp, end = 20.dp)
            )
        }
    }
}