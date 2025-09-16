package com.example.smart_rent_application_landlord.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.android.volley.toolbox.ImageRequest
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.ui.theme.Bold30Text
import com.example.smart_rent_application_landlord.ui.theme.Bold35Text
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.Semi16Text
import com.example.smart_rent_application_landlord.ui.theme.Semi18Text
import com.example.smart_rent_application_landlord.ui.theme.lightGreenMainColor
import com.example.smart_rent_application_landlord.ui.theme.lightYellowColor
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import coil.compose.rememberAsyncImagePainter
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

// 2. Cập nhật HouseCard để truyền building_id
@Composable
fun HouseCard(
    buildingId: Int,
    imageUrl: String,
    address1: String,
    address2: String,
    navController: NavController
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(350.dp)
        .clip(RoundedCornerShape(20.dp))
        .clickable {
            // Use the createRoute function to properly format the route
            navController.navigate(Screen.HomeDetail.createRoute(buildingId))
        }
    ) {
        val painter = rememberAsyncImagePainter(model = imageUrl)
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        ) {
            Column(modifier = Modifier.width(270.dp).padding(start = 20.dp, top = 260.dp, bottom = 20.dp)) {
                Text(
                    address1,
                    style = Bold30Text,
                    color = Color.White,
                    lineHeight = 30.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(address2, style = Semi16Text, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}



@Composable
fun SearchBar() {
    var search by remember { mutableStateOf("") }

    Row (modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = search,
            onValueChange = { newValue -> search = newValue },
            singleLine = true,
            textStyle = TextStyle(fontFamily = GilroyFont, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black),
            modifier = Modifier.width(305.dp).height(40.dp).padding(top = 0.dp).clip(
                RoundedCornerShape(10.dp)
            ).background(Color.LightGray.copy(alpha = 0.3f)),
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    Icon(painter = painterResource(R.drawable.search), contentDescription = "search", tint = Color.Unspecified, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (search.isEmpty()) {
                            Text(text = "Tìm kiếm", fontFamily = GilroyFont, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.Gray, textAlign = TextAlign.Center)
                        }
                        innerTextField()
                    }
                }
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(
            yellowColorButton
        ),
            contentAlignment = Alignment.Center) {
            IconButton(onClick = {}) {
                Icon(painter = painterResource(R.drawable.sort), contentDescription = "sort", tint = Color.Unspecified, modifier = Modifier.size(25.dp))
            }
        }
    }
}
// HomeFilterRow.kt - Sử dụng số lượng thực từ API
@Composable
fun HomeFilterRow(viewModel: HomeViewModel) {
    // Lấy building types và counts từ ViewModel
    val buildingTypes by viewModel.buildingTypes.collectAsState()
    val buildingCounts by viewModel.buildingCounts.collectAsState()

    // Tạo danh sách folders từ building counts (đã bao gồm "Tất cả")
    val folders = remember(buildingCounts) {
        buildingCounts.map { it.type_name }
    }

    // Tạo map counts từ API data
    val folderCounts = remember(buildingCounts) {
        buildingCounts.associate { it.type_name to it.count.toString() }
    }

    var selectedFolder by remember { mutableStateOf("Tất cả") }

    Row(modifier = Modifier.fillMaxWidth().height(70.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).horizontalScroll(rememberScrollState()).height(50.dp).padding(top = 2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                folders.forEachIndexed { index, folderName ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    val isSelected = folderName == selectedFolder
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) lightYellowColor else lightGreenMainColor,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedFolder = folderName
                                // Gọi filter khi click
                                viewModel.filterBuildings(1, folderName) // landlord_id = 1
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = folderName,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = GilroyFont,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) Color.Black else Color.Black,
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(30.dp, 25.dp)
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = folderCounts[folderName] ?: "0", // Hiển thị số lượng thực từ API
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Black,
                                        fontFamily = GilroyFont,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// NAVIGATE TAB IN SCREEN
@Composable
fun TabItem(title: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, activeColor: Color, inactiveColor: Color) {
    val underlineHeight = 2.dp
    val textColor = if (selected) activeColor else inactiveColor
    Column(modifier = modifier.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null, // ✅ TẮT ripple xám
        onClick = onClick,
    ).fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, color = textColor, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, fontFamily = GilroyFont)
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.height(underlineHeight).fillMaxWidth(0.6f).background(if (selected) activeColor else Color.Transparent))
    }
}
// CUSTOM TEXT FIELD
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )
) {
    Spacer(modifier = Modifier.height(10.dp))
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val mergedActions = KeyboardActions(
        onDone = {
            keyboardActions.onDone?.invoke(this)
            focusManager.clearFocus()
            keyboardController?.hide()
        },
        onNext = {
            keyboardActions.onNext?.invoke(this)
            focusManager.moveFocus(FocusDirection.Next)
        },
        onPrevious = {
            keyboardActions.onPrevious?.invoke(this)
            focusManager.moveFocus(FocusDirection.Previous)
        },
        onSearch = {
            keyboardActions.onSearch?.invoke(this)
            focusManager.clearFocus()
            keyboardController?.hide()
        },
        onSend = {
            keyboardActions.onSend?.invoke(this)
            focusManager.clearFocus()
            keyboardController?.hide()
        },
        onGo = {
            keyboardActions.onGo?.invoke(this)
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = mergedActions,
        singleLine = singleLine,
        maxLines = maxLines,
        colors = colors,
        shape = RoundedCornerShape(12.dp)
    )
}
@Composable
fun TapOutsideToClearFocus(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(                             // 👈 thay vì detectTapGestures
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                keyboard?.hide()
                focusManager.clearFocus(force = true)
            }
    ) {
        content()
    }
}
// CUSTOM DROPDOWN
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ModernDropdown(
    modifier: Modifier = Modifier,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    items: List<T>,
    itemDisplayText: (T) -> String = { it.toString() },
    placeholder: String = "Select an option",
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    ),
    shape: RoundedCornerShape = RoundedCornerShape(10.dp)
) {
    require(items.isNotEmpty()) { "items must not be empty" }

    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = !expanded
                if (expanded) {
                    keyboard?.hide()
                    focusManager.clearFocus()
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        // TextField (anchor)
        OutlinedTextField(
            value = selectedItem?.let(itemDisplayText) ?: "",
            onValueChange = {}, // readOnly
            readOnly = true,
            enabled = enabled,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = {
                // Giữ tinh thần icon Home/Check của bạn
                Icon(
                    painter = painterResource(R.drawable.ic_down_arrow),
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(40.dp)
                        .rotate(if (expanded) 180f else 0f) // 👈 Xoay 270° khi đóng
                )

            },
            colors = colors,
            shape = shape,
            modifier = Modifier
                .menuAnchor() // 🔑 bắt buộc để Dropdown bám đúng vị trí
                .fillMaxWidth().border(1.dp, Color.LightGray, RoundedCornerShape(10.dp)),
            singleLine = true,
        )

        // Popup dropdown: không bị LazyColumn cắt
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .exposedDropdownSize(matchTextFieldWidth = true)
                .background(Color.White)
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    modifier = Modifier.background(Color.White),
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = itemDisplayText(item),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (selectedItem == item) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp
                            )
                            if (selectedItem == item) {
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                        focusManager.clearFocus()
                        keyboard?.hide()
                    }
                )

                if (index < items.size - 1) {
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = Color.DarkGray.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}
