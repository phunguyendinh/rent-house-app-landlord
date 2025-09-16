package com.example.smart_rent_application_landlord.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.smart_rent_application_landlord.navigation.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.navigation.getScreenFromRoute
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.normalSemiText
import com.example.smart_rent_application_landlord.ui.theme.normalText
import com.example.smart_rent_application_landlord.ui.theme.titleMedium
import com.example.smart_rent_application_landlord.ui.theme.titleSemiBold
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(navController: NavHostController, content: @Composable (PaddingValues) -> Unit) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val currentScreen = getScreenFromRoute(currentRoute)
    val showBottomBar = currentScreen?.showBottomBar ?: false
    val showFloatingButton = currentScreen?.showFloatingButton ?:false

    val annotatedString = buildAnnotatedString {
        withStyle(style = titleSemiBold) {append("Smart ")}
        withStyle(style = titleMedium) {append("Rent")}
    }

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        // TOP APP BAR
        topBar = {
            TopAppBar(title = { Text(annotatedString) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.ic_justify), modifier = Modifier.size(30.dp), tint = Color.DarkGray, contentDescription = "justify")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.ic_panda), contentDescription = "avatar", tint = Color.Unspecified, modifier = Modifier.size(40.dp))
                    }
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.ic_more), contentDescription = "avatar", tint = Color.Unspecified, modifier = Modifier.size(40.dp))
                    }
                })
        },
        // FLOATING ACTION BUTTON
        floatingActionButton = {
            if (showFloatingButton) {
                FloatingActionButton(onClick = {navController.navigate(Screen.ServiceAdd.route)}) {
                    Icon(painter = painterResource(R.drawable.ic_plus), contentDescription = "additional", tint = Color.Black, modifier = Modifier.size(25.dp))
                }
            }
        },
        // BOTTOM NAV BAR
        bottomBar = {
            if (showBottomBar) {
                NavigationBar (modifier = Modifier.border(1.dp, Color(0xFFF1EFEC), shape = RectangleShape),
                    containerColor = Color.White) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(painter = painterResource(screen.icon), contentDescription = null, modifier = Modifier.size(25.dp),
                                tint = if (currentDestination?.route == screen.route) {
                                    Color(0xFFFFD700)
                                } else {
                                    Color.DarkGray
                                }
                            )},
//                            label = {
//                                Icon(painter = painterResource(R.drawable.ic_dot), contentDescription = "dot"
//                                    , modifier = Modifier.size(25.dp)
//                                        .alpha(if (currentDestination?.route == screen.route) 1f else 0f)
//                                    , tint = Color(0xFF2C3CFF)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            interactionSource = remember { MutableInteractionSource() },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = Color.Transparent,
                                unselectedIconColor = Color.Transparent,
                                selectedTextColor = Color.Transparent,
                                unselectedTextColor = Color.Transparent,
                                disabledIconColor = Color.Transparent,
                                disabledTextColor = Color.Transparent
                            ),
                            modifier = Modifier.indication(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ),
                            onClick = {
                                // Luôn navigate về screen được chọn, clear back stack
                                navController.navigate(screen.route) {
                                    // Clear toàn bộ back stack và đặt screen này làm root
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
    var idHouse by remember { mutableStateOf("29389483-P101") }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color.White)) {
                Column (modifier = Modifier.padding(top = 20.dp, bottom = 50.dp, start = 15.dp, end = 15.dp)) {
                    Text("Hỏi chủ trọ của bạn để biết mã truy cập khu trọ rồi nhập vào đây", style = normalSemiText, lineHeight = 25.sp)
                    Spacer(modifier = Modifier.height(15.dp))
                    TextField(value = idHouse, onValueChange = {newValue -> idHouse = newValue},
                        label = { Text("Mã phòng trọ", fontFamily = GilroyFont, fontSize = 14.sp, color = Color.Gray) },
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
                    Spacer(modifier = Modifier.height(15.dp))
                    Text("Lưu ý", style = normalSemiText)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Mã gồm 8 chữ số - mã phòng, không có dấu cách hoặc kí hiệu", style = normalText, lineHeight = 20.sp, fontWeight = FontWeight.Normal, fontSize = 14.sp)
                }
            }
        }
    }
}

private val bottomNavItems = listOf(
    Screen.Home,
    Screen.Service,
//    Screen.Task,
)