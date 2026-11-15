package com.example.umix.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.umix.ui.theme.*

enum class NavTab(val label: String, val icon: ImageVector) {
    Home("Início", Icons.Outlined.Home),
    Search("Buscar", Icons.Outlined.Search),
    Profile("Perfil", Icons.Outlined.PersonOutline),
}

private val ItemSize = 46.dp
private val ItemGap = 8.dp
private const val NavAnimMs = 360

/**
 * Barra flutuante: o item selecionado vira uma "pílula" amarela com rótulo e os
 * demais ficam como círculos. Ao trocar de aba, a pílula desliza/expande de forma fluida.
 */
@Composable
fun UmixBottomBar(
    selected: NavTab,
    onSelect: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(CircleShape)
            .background(UmixNavy)
            .padding(6.dp)
    ) {
        val expandedWidth = maxWidth - ItemSize * 2 - ItemGap * 2
        Row(
            horizontalArrangement = Arrangement.spacedBy(ItemGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTab.entries.forEach { tab ->
                NavItem(
                    tab = tab,
                    selected = tab == selected,
                    expandedWidth = expandedWidth,
                    onClick = { onSelect(tab) }
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    tab: NavTab,
    selected: Boolean,
    expandedWidth: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    // Todos usam o mesmo tween: enquanto um encolhe o outro cresce, a soma das larguras é constante.
    val width by animateDpAsState(
        targetValue = if (selected) expandedWidth else ItemSize,
        animationSpec = tween(NavAnimMs, easing = FastOutSlowInEasing),
        label = "navWidth"
    )
    val background by animateColorAsState(
        targetValue = if (selected) UmixYellowBright else UmixYellowBright.copy(alpha = 0f),
        animationSpec = tween(NavAnimMs),
        label = "navBg"
    )
    val ring by animateColorAsState(
        targetValue = if (selected) UmixTextSoft.copy(alpha = 0f) else UmixTextSoft,
        animationSpec = tween(NavAnimMs),
        label = "navRing"
    )
    val tint by animateColorAsState(
        targetValue = if (selected) UmixTextDark else UmixWhite,
        animationSpec = tween(NavAnimMs),
        label = "navTint"
    )
    val labelAlpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(if (selected) 320 else 120, delayMillis = if (selected) 120 else 0),
        label = "navLabel"
    )

    Row(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .clip(CircleShape)
            .background(background)
            .border(1.dp, ring, CircleShape)
            .clickable(role = Role.Tab, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(ItemSize - 2.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .border(1.dp, UmixNavy.copy(alpha = 0.18f * labelAlpha), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.label,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Text(
            text = tab.label,
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
            color = UmixTextDark,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip,
            modifier = Modifier
                .padding(start = 4.dp)
                .alpha(labelAlpha)
        )
    }
}
