package com.example.umix.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.umix.R

val Montserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_bold, FontWeight.Bold),
)

// Fonte monoespaçada: "ver mais", contadores e "benefícios"
val Mono = FontFamily(
    Font(R.font.space_mono_regular, FontWeight.Normal),
    Font(R.font.space_mono_bold, FontWeight.Bold),
)

// Fonte serifada usada no slogan "Ouça, avalie e descubra!"
val Fraunces = FontFamily(
    Font(R.font.fraunces_bold, FontWeight.Bold),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
)

// Estilos específicos das telas de autenticação
object UmixText {
    val Wordmark = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 80.sp, lineHeight = 84.sp, letterSpacing = (-2).sp, color = UmixInk)
    val Tagline = TextStyle(fontFamily = Fraunces, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp, color = UmixInk)
    val ScreenTitle = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 34.sp, color = UmixInk)
    val Tab = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    val FieldLabel = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = UmixInk)
    val FieldInput = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = UmixInk)
    val Button = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    val Small = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = UmixInk)
    val Link = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 11.sp, color = UmixInk)

    // --- Home / Buscar / Premium ---
    val HeaderWordmark = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 22.sp, letterSpacing = (-0.5).sp, color = UmixTextDark)
    val SectionTitle = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = UmixTextDark)
    val SectionTitleSerif = TextStyle(fontFamily = Fraunces, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = UmixTextDark)
    val SeeMore = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Normal, fontSize = 10.sp, color = UmixAccentBlue, textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)
    val Mono10 = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Normal, fontSize = 10.sp, color = UmixTextSoft)
    val Chip = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 11.sp)
}
