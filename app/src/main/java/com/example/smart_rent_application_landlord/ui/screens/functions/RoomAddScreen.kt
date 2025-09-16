package com.example.smart_rent_application_landlord.ui.screens.functions

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.navigation.Screen
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.backgroundBoxText
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

@Composable
fun RoomAddScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    buildingId: Int, // Cần truyền building_id từ navigation
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // States cho form
    var roomName by rememberSaveable { mutableStateOf("") }
    var rentPrice by rememberSaveable { mutableStateOf("") }
    var maxOccupants by rememberSaveable { mutableStateOf("") }

    // State cho image upload
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    var uploadedImageFilename by remember { mutableStateOf<String?>(null) }
    var isUploadingImage by remember { mutableStateOf(false) }

    // State cho validation và loading
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Collect selected services từ ViewModel
    val selectedServices by homeViewModel.selectedRoomServices

    // Image picker launcher
    // THÊM VÀO RoomAddScreen - trong imagePickerLauncher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            Log.d("RoomAddScreen", "Image selected: $selectedUri")

            selectedImageUri = selectedUri
            uploadedImageUrl = null // Reset uploaded URL
            uploadedImageFilename = null
            isUploadingImage = true

            homeViewModel.uploadImage(
                context = context,
                uri = selectedUri,
                onSuccess = { imageUrl, filename ->
                    Log.d("RoomAddScreen", "=== UPLOAD SUCCESS CALLBACK ===")
                    Log.d("RoomAddScreen", "Received imageUrl: '$imageUrl'")
                    Log.d("RoomAddScreen", "Received filename: '$filename'")

                    uploadedImageUrl = imageUrl
                    uploadedImageFilename = filename
                    isUploadingImage = false
                    errorMessage = null

                    // Clear local URI after successful upload
                    selectedImageUri = null

                    Log.d("RoomAddScreen", "State updated:")
                    Log.d("RoomAddScreen", "  uploadedImageUrl: '$uploadedImageUrl'")
                    Log.d("RoomAddScreen", "  isUploadingImage: $isUploadingImage")
                    Log.d("RoomAddScreen", "=== END SUCCESS CALLBACK ===")
                },
                onError = { error ->
                    Log.e("RoomAddScreen", "=== UPLOAD ERROR CALLBACK ===")
                    Log.e("RoomAddScreen", "Error: $error")

                    isUploadingImage = false
                    errorMessage = "Lỗi upload ảnh: $error"
                    selectedImageUri = null
                    uploadedImageFilename = null
                    uploadedImageUrl = null

                    Log.e("RoomAddScreen", "=== END ERROR CALLBACK ===")
                }
            )
        }
    }

    // Load services khi screen được tạo
    LaunchedEffect(Unit) {
        homeViewModel.fetchServices()
    }

    // Clear selected services khi thoát screen
//    LaunchedEffect(Unit) {
//        homeViewModel.clearSelectedRoomServices()
//    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            RoomAddInfo(
                roomName = roomName,
                onRoomNameChange = { roomName = it },
                rentPrice = rentPrice,
                onRentPriceChange = { rentPrice = it },
                maxOccupants = maxOccupants,
                onMaxOccupantsChange = { maxOccupants = it },
                selectedServices = selectedServices,
                selectedImageUri = selectedImageUri,
                uploadedImageUrl = uploadedImageUrl,
                isUploadingImage = isUploadingImage,
                onImageClick = {
                    imagePickerLauncher.launch("image/*")
                },
                onRemoveService = { service ->
                    homeViewModel.toggleRoomService(service)
                },
                onEditServices = {
                    navController.navigate(Screen.RoomEditService.route)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Error message
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Buttons
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (!isLoading) {
                            homeViewModel.clearSelectedRoomServices()
                            navController.popBackStack()
                        }
                    },
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.DarkGray
                    ),
                    modifier = Modifier.height(45.dp),
                    enabled = !isLoading && !isUploadingImage
                ) {
                    Text("Đóng", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        // Validate form
                        when {
                            roomName.isBlank() -> {
                                errorMessage = "Vui lòng nhập tên phòng"
                            }
                            rentPrice.isBlank() -> {
                                errorMessage = "Vui lòng nhập giá thuê"
                            }
                            rentPrice.toDoubleOrNull() == null -> {
                                errorMessage = "Giá thuê phải là số"
                            }
                            maxOccupants.isBlank() -> {
                                errorMessage = "Vui lòng nhập số người ở tối đa"
                            }
                            maxOccupants.toIntOrNull() == null || maxOccupants.toInt() < 1 -> {
                                errorMessage = "Số người ở phải là số nguyên dương"
                            }
                            isUploadingImage -> {
                                errorMessage = "Vui lòng chờ upload ảnh hoàn tất"
                            }
                            else -> {
                                // Form hợp lệ, gọi API
                                errorMessage = null
                                isLoading = true

                                homeViewModel.addRoom(
                                    buildingId = buildingId,
                                    name = roomName,
                                    rentPrice = rentPrice.toDouble(),
                                    maxOccupants = maxOccupants.toInt(),
                                    imageUrl = uploadedImageFilename ?: "",
                                    selectedServices = selectedServices,
                                    onSuccess = {
                                        isLoading = false
                                        homeViewModel.clearSelectedRoomServices()
                                        navController.popBackStack()
                                    },
                                    onError = { error ->
                                        isLoading = false
                                        errorMessage = error
                                    }
                                )
                            }
                        }
                    },
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006FFD),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(45.dp),
                    enabled = !isLoading && !isUploadingImage
                ) {
                    if (isLoading) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Đang lưu...", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        }
                    } else {
                        Text("Lưu", fontFamily = GilroyFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoomAddInfo(
    roomName: String,
    onRoomNameChange: (String) -> Unit,
    rentPrice: String,
    onRentPriceChange: (String) -> Unit,
    maxOccupants: String,
    onMaxOccupantsChange: (String) -> Unit,
    selectedServices: List<com.example.smart_rent_application_landlord.models.Service>,
    selectedImageUri: Uri?,
    uploadedImageUrl: String?,
    isUploadingImage: Boolean,
    onImageClick: () -> Unit,
    onRemoveService: (com.example.smart_rent_application_landlord.models.Service) -> Unit,
    onEditServices: () -> Unit
) {
    // BOX DISPLAY
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
                Text("Thông tin cơ bản", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Thông tin cơ bản tên, giá thuê,...", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }

    // NORMAL INFO
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Tên phòng trọ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            Text("P001, P002,...", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
            RoomAddTextField(title = "Tên phòng", value = roomName, onValueChange = onRoomNameChange)
            Spacer(modifier = Modifier.height(15.dp))
            Text("Giá thuê", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            RoomAddTextField(
                title = "Giá thuê (VNĐ)",
                value = rentPrice,
                onValueChange = onRentPriceChange,
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text("Số người ở tối đa", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            RoomAddTextField(
                title = "Số người",
                value = maxOccupants,
                onValueChange = onMaxOccupantsChange,
                keyboardType = KeyboardType.Number
            )
        }
    }

    // IMAGE UPLOAD SECTION - Fixed version
    Spacer(modifier = Modifier.height(10.dp))
    // THÊM VÀO RoomAddInfo - trong Box hiển thị ảnh
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundBoxText)
            .dashedBorder(
                strokeWidth = 3f,
                color = if (isUploadingImage) Color(0xFF006FFD) else Color.LightGray,
                intervals = floatArrayOf(15f, 10f)
            )
            .clickable { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        // DEBUG LOG TRƯỚC KHI RENDER
        Log.d("RoomAddInfo", "=== RENDER STATE DEBUG ===")
        Log.d("RoomAddInfo", "isUploadingImage: $isUploadingImage")
        Log.d("RoomAddInfo", "uploadedImageUrl: '$uploadedImageUrl'")
        Log.d("RoomAddInfo", "uploadedImageUrl?.isNotEmpty(): ${uploadedImageUrl?.isNotEmpty()}")
        Log.d("RoomAddInfo", "selectedImageUri: $selectedImageUri")
        Log.d("RoomAddInfo", "=== END RENDER DEBUG ===")

        when {
            isUploadingImage -> {
                Log.d("RoomAddInfo", "Rendering: UPLOADING STATE")
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = Color(0xFF006FFD),
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Đang upload ảnh...",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color(0xFF006FFD)
                    )
                }
            }
            !uploadedImageUrl.isNullOrEmpty() -> {
                Log.d("RoomAddInfo", "Rendering: UPLOADED IMAGE STATE")
                Log.d("RoomAddInfo", "Loading AsyncImage with URL: '$uploadedImageUrl'")

                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = uploadedImageUrl,
                        contentDescription = "Uploaded room image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop,
                        onSuccess = {
                            Log.d("AsyncImage", "✅ SUCCESS: Image loaded successfully")
                            Log.d("AsyncImage", "URL: '$uploadedImageUrl'")
                        },
                        onError = { error ->
                            Log.e("AsyncImage", "❌ ERROR: Failed to load image")
                            Log.e("AsyncImage", "URL: '$uploadedImageUrl'")
                            Log.e("AsyncImage", "Error: ${error.result.throwable}")
                            Log.e("AsyncImage", "Error message: ${error.result.throwable.message}")
                        },
                        onLoading = {
                            Log.d("AsyncImage", "🔄 LOADING: Image is loading...")
                            Log.d("AsyncImage", "URL: '$uploadedImageUrl'")
                        }
                    )
                    // Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Nhấn để thay đổi ảnh",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            selectedImageUri != null -> {
                Log.d("RoomAddInfo", "Rendering: LOCAL IMAGE STATE")
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected room image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                Log.d("RoomAddInfo", "Rendering: EMPTY STATE")
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_upload),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Tải ảnh lên",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        "Nhấn để chọn ảnh từ thư viện",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
    // BOX DISPLAY - Services
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
                Text("Dịch vụ sử dụng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Tiền điện, nước, rác, mạng internet,...", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }

    // SERVICES - Show based on condition
    Spacer(modifier = Modifier.height(10.dp))

    if (selectedServices.isEmpty()) {
        // SERVICES - Empty state
        Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(backgroundBoxText)
            .dashedBorder(
                strokeWidth = 3f,
                color = Color.LightGray,
                intervals = floatArrayOf(15f, 10f)
            ), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(painter = painterResource(R.drawable.ic_box), contentDescription = null, modifier = Modifier.size(50.dp), tint = Color.Unspecified)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Chưa có dịch vụ được áp dụng", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)
            }
        }
    } else {
        // SERVICES KHI CÓ ITEM - Show selected services
        Box(modifier = Modifier.fillMaxWidth().background(backgroundBoxText)
            .dashedBorder(
                strokeWidth = 3f,
                color = Color.LightGray,
                intervals = floatArrayOf(15f, 10f)
            )) {
            FlowRow(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                selectedServices.forEach { service ->
                    ServiceTag(
                        serviceName = service.name,
                        onRemove = {
                            onRemoveService(service)
                        }
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Edit Services Button
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onEditServices,
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
}

@Composable
fun ServiceTag(
    serviceName: String,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(35.dp)
            .wrapContentWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, Color.Blue.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                serviceName,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Remove service",
                modifier = Modifier
                    .size(11.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onRemove() },
                tint = Color.Blue.copy(alpha = 0.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomAddTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                title,
                fontFamily = GilroyFont,
                fontSize = 14.sp,
                color = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp)),
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = Color.Black,
            disabledLabelColor = Color.Gray,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        )
    )
}