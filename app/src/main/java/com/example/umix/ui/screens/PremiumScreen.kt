package com.example.umix.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.umix.ui.components.UmixLogo
import com.example.umix.ui.theme.*

enum class PremiumPlan(val label: String) { Monthly("Mensal"), Yearly("Anual") }

private data class Benefit(val icon: ImageVector, val text: String)

private val benefits = listOf(
    Benefit(Icons.Outlined.CheckCircle, "Selo de verificado ao lado do usuário"),
    Benefit(Icons.Outlined.Tune, "Personalização completa do perfil"),
    Benefit(Icons.Outlined.Block, "Livre de anúncios"),
    Benefit(Icons.Outlined.Bolt, "Tópicos e reviews impulsionadas"),
)

private fun sans(weight: FontWeight, size: Float) =
    TextStyle(fontFamily = Montserrat, fontWeight = weight, fontSize = size.sp)

@Composable
fun PremiumScreen(
    onClose: () -> Unit,
    onSubscribe: (PremiumPlan) -> Unit
) {
    var plan by remember { mutableStateOf(PremiumPlan.Yearly) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UmixCream)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
    ) {
        // Fechar + logo
        Box(Modifier.fillMaxWidth()) {
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(Icons.Outlined.Close, contentDescription = "Fechar", tint = UmixInk)
            }
            UmixLogo(
                width = 76.dp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
        Text(
            text = "Se torne um avaliador",
            style = sans(FontWeight.SemiBold, 22f),
            color = UmixInk,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "PREMIUM",
            style = sans(FontWeight.Medium, 22f),
            color = UmixInk,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        )

        // "benefícios" sublinhado
        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .width(IntrinsicSize.Max)
        ) {
            Text(
                text = "benefícios",
                style = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 22.sp),
                color = UmixInk
            )
            Box(
                Modifier
                    .padding(top = 2.dp)
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(UmixNavy)
            )
        }

        Spacer(Modifier.height(14.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(UmixNavy)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            benefits.forEach { BenefitRow(it) }
        }

        // Planos
        Spacer(Modifier.height(56.dp))
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlanCard(
                selected = plan == PremiumPlan.Monthly,
                onClick = { plan = PremiumPlan.Monthly },
                modifier = Modifier.weight(1f)
            ) { fg ->
                Text("Mensal", style = sans(FontWeight.SemiBold, 15f), color = fg)
                Spacer(Modifier.weight(1f))
                Text("R$15,00/mês", style = sans(FontWeight.SemiBold, 15f), color = fg)
            }
            PlanCard(
                selected = plan == PremiumPlan.Yearly,
                onClick = { plan = PremiumPlan.Yearly },
                modifier = Modifier.weight(1f)
            ) { fg ->
                Text("Anual", style = sans(FontWeight.SemiBold, 15f), color = fg)
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("De ", style = sans(FontWeight.SemiBold, 11f), color = fg)
                    StrikeText("R$180,00", sans(FontWeight.Bold, 14f), fg)
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("Por ", style = sans(FontWeight.SemiBold, 11f), color = fg)
                    Text("R$150,00", style = sans(FontWeight.Bold, 14f), color = fg)
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clip(CircleShape)
                .background(UmixButtonYellow)
                .clickable { onSubscribe(plan) },
            contentAlignment = Alignment.Center
        ) {
            Text("Assinar e seguir", style = sans(FontWeight.Bold, 16f), color = UmixInk)
        }

        Spacer(Modifier.height(40.dp))
        Text(
            text = "Termos de serviço",
            style = sans(FontWeight.SemiBold, 14f),
            color = UmixInk,
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(Modifier.height(8.dp))
        TermsBox()
    }
}

@Composable
private fun BenefitRow(benefit: Benefit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = benefit.icon,
            contentDescription = null,
            tint = UmixWhite,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text("•", style = sans(FontWeight.Bold, 13f), color = UmixWhite)
        Spacer(Modifier.width(8.dp))
        Text(benefit.text, style = sans(FontWeight.SemiBold, 12.5f), color = UmixWhite)
    }
}

/**
 * Card de plano. Selecionado = fundo creme + contorno azul-marinho;
 * não selecionado = preenchido de azul-marinho. A troca é animada.
 */
@Composable
private fun PlanCard(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(textColor: Color) -> Unit
) {
    val shape = RoundedCornerShape(24.dp)
    val bg by animateColorAsState(if (selected) UmixCream else UmixNavy, tween(300), label = "planBg")
    val fg by animateColorAsState(if (selected) UmixInk else UmixWhite, tween(300), label = "planFg")
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.96f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "planScale"
    )
    Column(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .fillMaxHeight()
            .heightIn(min = 106.dp)
            .clip(shape)
            .background(bg)
            .border(4.dp, UmixNavy, shape)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        content(fg)
    }
}

/** Texto riscado com a linha vermelha do Figma. */
@Composable
private fun StrikeText(text: String, style: TextStyle, color: Color) {
    Text(
        text = text,
        style = style,
        color = color,
        modifier = Modifier.drawWithContent {
            drawContent()
            drawLine(
                color = UmixRed,
                start = Offset(-2.dp.toPx(), size.height * 0.68f),
                end = Offset(size.width + 2.dp.toPx(), size.height * 0.42f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    )
}

@Composable
private fun TermsBox() {
    var expanded by remember { mutableStateOf(false) }
    val terms = buildAnnotatedString {
        append("Ao assinar ou utilizar nossos serviços, você (“Usuário” ou “Assinante”) concorda integralmente com as condições dispostas em nossos ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("TERMOS DE SERVIÇO") }
        append(". As assinaturas são renovadas automaticamente até o cancelamento. Cancele a qualquer momento desde que feito com pelo menos 24h de antecedência a data de renovação.")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, UmixInk, RoundedCornerShape(10.dp))
            .clickable { expanded = !expanded }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = terms,
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 10.sp, lineHeight = 14.sp),
            color = UmixInk
        )
        if (expanded) {
            // Texto provisório: substituir pelo texto jurídico oficial
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Os valores podem ser reajustados mediante aviso prévio. Para cancelar, acesse as configurações da sua conta.",
                style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 10.sp, lineHeight = 14.sp),
                color = UmixInk
            )
        }
        Icon(
            imageVector = if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
            contentDescription = if (expanded) "Recolher termos" else "Expandir termos",
            tint = UmixInk,
            modifier = Modifier.size(20.dp)
        )
    }
}
