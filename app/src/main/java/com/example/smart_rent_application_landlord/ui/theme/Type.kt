package com.example.smart_rent_application_landlord.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.smart_rent_application_landlord.R

val GilroyFont = FontFamily(
    Font(R.font.gilroy_back1, weight = FontWeight.Black),
    Font(R.font.gilroy_bold1, weight = FontWeight.Bold),
    Font(R.font.gilroy_medium1, weight = FontWeight.Medium),
    Font(R.font.gilroy_light1, weight = FontWeight.Light),
    Font(R.font.gilroy_regular1, weight = FontWeight.Normal),
    Font(R.font.gilroy_semibold1, weight = FontWeight.SemiBold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = GilroyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
val titleSemiBold = SpanStyle(
    fontFamily = GilroyFont,
    fontSize = 22.sp,
    fontWeight = FontWeight.SemiBold,
    color = Color.DarkGray
)
val titleMedium = SpanStyle(
    fontFamily = GilroyFont,
    fontSize = 22.sp,
    fontWeight = FontWeight.Medium,
    color = Color.DarkGray
)
val largeTitle = TextStyle(
    fontFamily = GilroyFont,
    fontSize = 28.sp,
    fontWeight = FontWeight.Bold,
    color = Color.White
)
val normalWhiteText = TextStyle(
    fontFamily = GilroyFont,
    fontSize = 16.sp,
    fontWeight = FontWeight.SemiBold,
    color = Color.White
)
val normalText = TextStyle(
    fontFamily = GilroyFont,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    color = Color.DarkGray
)
val normalSemiText = TextStyle(
    fontFamily = GilroyFont,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    color = Color.DarkGray
)
val Semi18Text = TextStyle(
    fontFamily = GilroyFont,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    color = Color.DarkGray
)
val Semi16Text = TextStyle(
    fontFamily = GilroyFont,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    color = Color.DarkGray
)
val Semi20Text = TextStyle( // TITLE
    fontFamily = GilroyFont,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    color = Color.DarkGray
)

val Bold35Text = TextStyle(
    fontFamily = GilroyFont,
    fontWeight = FontWeight.Bold,
    fontSize = 35.sp,
    color = Color.DarkGray
)
val Bold30Text = TextStyle(
    fontFamily = GilroyFont,
    fontWeight = FontWeight.Bold,
    fontSize = 30.sp,
    color = Color.DarkGray
)
val Medium16Text = TextStyle(
    fontFamily = GilroyFont,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    color = Color.DarkGray
)
val Medium14Text = TextStyle(
    fontFamily = GilroyFont,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    color = Color.DarkGray
)