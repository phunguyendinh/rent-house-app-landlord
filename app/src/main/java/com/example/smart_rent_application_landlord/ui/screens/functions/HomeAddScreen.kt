package com.example.smart_rent_application_landlord.ui.screens.functions

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.smart_rent_application_landlord.R
import com.example.smart_rent_application_landlord.ui.components.ModernDropdown
import com.example.smart_rent_application_landlord.ui.theme.GilroyFont
import com.example.smart_rent_application_landlord.ui.theme.backgroundBoxText
import com.example.smart_rent_application_landlord.ui.theme.yellowColorButton
import com.example.smart_rent_application_landlord.viewmodels.HomeViewModel

@Composable
fun HomeAddScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Collect building types từ ViewModel
    val buildingTypes by homeViewModel.buildingTypes.collectAsState()

    // State cho form
    var homeName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedBuildingType by remember { mutableStateOf<String?>(null) }
    var billingDate by remember { mutableStateOf("") }

    // State cho image upload
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    var uploadedImageFilename by remember { mutableStateOf<String?>(null) }

    var isUploadingImage by remember { mutableStateOf(false) }

    // State cho validation
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            selectedImageUri = selectedUri
            isUploadingImage = true

            // Upload image
            homeViewModel.uploadImage(
                context = context,
                uri = selectedUri,
                onSuccess = { imageUrl, filename ->
                    uploadedImageUrl = imageUrl // Dùng để hiển thị preview
                    uploadedImageFilename = filename // Dùng để lưu vào DB
                    isUploadingImage = false
                    errorMessage = null
                },
                onError = { error ->
                    isUploadingImage = false
                    errorMessage = "Lỗi upload ảnh: $error"
                    selectedImageUri = null
                    uploadedImageFilename = null
                }
            )
        }
    }

    // Load building types khi screen được tạo
    LaunchedEffect(Unit) {
        homeViewModel.fetchBuildingTypes()
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            HomeAddInfo(
                homeName = homeName,
                onHomeNameChange = { homeName = it },
                address = address,
                onAddressChange = { address = it },
                selectedBuildingType = selectedBuildingType,
                onBuildingTypeChange = { selectedBuildingType = it },
                buildingTypeOptions = buildingTypes.map { it.name },
                billingDate = billingDate,
                onBillingDateChange = { billingDate = it },
                selectedImageUri = selectedImageUri,
                uploadedImageUrl = uploadedImageUrl,
                isUploadingImage = isUploadingImage,
                onImageClick = {
                    imagePickerLauncher.launch("image/*")
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
                            homeName.isBlank() -> {
                                errorMessage = "Vui lòng nhập tên nhà cho thuê"
                            }
                            address.isBlank() -> {
                                errorMessage = "Vui lòng nhập địa chỉ"
                            }
                            selectedBuildingType == null -> {
                                errorMessage = "Vui lòng chọn loại hình cho thuê"
                            }
                            billingDate.isBlank() -> {
                                errorMessage = "Vui lòng nhập ngày lập hóa đơn"
                            }
                            billingDate.toIntOrNull() == null || billingDate.toInt() !in 1..31 -> {
                                errorMessage = "Ngày lập hóa đơn phải là số từ 1 đến 31"
                            }
                            isUploadingImage -> {
                                errorMessage = "Vui lòng chờ upload ảnh hoàn tất"
                            }
                            else -> {
                                // Form hợp lệ, gọi API
                                errorMessage = null
                                isLoading = true

                                val selectedType = buildingTypes.find { it.name == selectedBuildingType }
                                if (selectedType != null) {
                                    homeViewModel.addBuilding(
                                        landlordId = 1,
                                        name = homeName,
                                        address = address,
                                        typeId = selectedType.id,
                                        billingDate = billingDate.toInt(),
                                        imageUrl = uploadedImageFilename ?: "", // chỉ lưu tên file
                                        onSuccess = {
                                            isLoading = false
                                            navController.popBackStack()
                                        },
                                        onError = { error ->
                                            isLoading = false
                                            errorMessage = error
                                        }
                                    )
                                } else {
                                    isLoading = false
                                    errorMessage = "Không tìm thấy loại hình được chọn"
                                }
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

fun Modifier.dashedBorder(
    strokeWidth: Float,
    color: Color,
    cornerRadius: Float = 0f,
    intervals: FloatArray = floatArrayOf(10f, 10f)
): Modifier = this.then(
    Modifier.drawBehind {
        val stroke = Stroke(
            width = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(intervals, 0f)
        )
        drawRoundRect(
            color = color,
            style = stroke,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
        )
    }
)

@Composable
fun HomeAddInfo(
    homeName: String,
    onHomeNameChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    selectedBuildingType: String?,
    onBuildingTypeChange: (String?) -> Unit,
    buildingTypeOptions: List<String>,
    billingDate: String,
    onBillingDateChange: (String) -> Unit,
    selectedImageUri: Uri?,
    uploadedImageUrl: String?,
    isUploadingImage: Boolean,
    onImageClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(7.dp)).background(yellowColorButton), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(R.drawable.ic_note), contentDescription = null, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Thông tin nhà cho thuê", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Thông tin cơ bản tên, loại hình", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Tên nhà cho thuê", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(title = "Tên nhà cho thuê", value = homeName, onValueChange = onHomeNameChange)
            Spacer(modifier = Modifier.height(15.dp))
            Text("Địa chỉ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            HomeAddTextField(title = "Địa chỉ", value = address, onValueChange = onAddressChange)
            Spacer(modifier = Modifier.height(15.dp))
            Text("Loại hình cho thuê", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
            ModernDropdown(
                selectedItem = selectedBuildingType,
                onItemSelected = onBuildingTypeChange,
                items = buildingTypeOptions,
                placeholder = "Loại hình cho thuê"
            )
        }
    }

    // DATE LEASE
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(7.dp)).background(yellowColorButton), contentAlignment = Alignment.Center) {
                Icon(painter = painterResource(R.drawable.ic_note), contentDescription = null, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Cài đặt ngày chốt & hạn hóa đơn", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Tùy chỉnh tích năng sử dụng cho nhà trọ", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray, lineHeight = 14.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(backgroundBoxText)) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column {
                Text("Ngày lập hóa đơn", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                HomeAddTextField2(
                    title = "Ngày lập (1-31)",
                    value = billingDate,
                    onValueChange = onBillingDateChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(10.dp)).background(
        Color.White).border(1.dp, Color(0xFFD3AF37), RoundedCornerShape(10.dp))) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.fillMaxHeight().width(40.dp).background(yellowColorButton)) {
                Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp).size(18.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                    Text("i", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD3AF37))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text("Khi có khách thuê không đóng tiền đúng hẹn. Phần mềm sẽ nhắc nhở bạn.", fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 18.sp, modifier = Modifier.width(300.dp))
        }
    }

    // IMAGE UPLOAD SECTION
    Spacer(modifier = Modifier.height(10.dp))
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
        when {
            isUploadingImage -> {
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
            uploadedImageUrl != null -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = uploadedImageUrl,
                        contentDescription = "Uploaded building image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    // Overlay để cho biết có thể thay đổi ảnh
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Nhấn để thay đổi ảnh",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            selectedImageUri != null -> {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected building image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAddTextField(title: String, value: String, onValueChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(title, fontFamily = GilroyFont, fontSize = 14.sp, color = Color.Gray) },
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
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAddTextField2(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    supportingText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClick: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(10.dp)
    val focusManager = LocalFocusManager.current

    val clickableModifier = if (readOnly && onClick != null) {
        modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { onClick() })
        }
    } else modifier

    Column {
        TextField(
            value = value,
            onValueChange = { if (!readOnly) onValueChange(it) },
            label = { Text(title, fontFamily = GilroyFont, fontSize = 14.sp, color = Color.Gray) },
            placeholder = {
                if (placeholder != null) Text(placeholder, fontSize = 14.sp, color = Color.LightGray)
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            modifier = clickableModifier
                .border(1.dp, if (isError) Color(0xFFD32F2F) else Color.LightGray, shape),
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() },
                onGo = { focusManager.clearFocus() },
                onNext = { focusManager.clearFocus() },
                onSearch = { focusManager.clearFocus() },
                onSend = { focusManager.clearFocus() }
            ),
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Black,
                disabledLabelColor = Color.Gray,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                containerColor = Color.White,
            ),
            shape = shape
        )

        if (supportingText != null) {
            Text(
                text = supportingText,
                color = if (isError) Color(0xFFD32F2F) else Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}