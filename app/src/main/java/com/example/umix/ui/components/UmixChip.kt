package com.example.umix.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.umix.ui.theme.*

/** Identidade de cor do chip: define como ele fica quando selecionado. */
enum class ChipAccent { Blue, Yellow }

/**
 * Chip de filtro (Mood / Ocasião).
 *  - Azul:     contorno e texto 4C63A6  -> selecionado: fundo 4C63A6 + texto branco
 *  - Amarelo:  contorno F2D93A, texto 8A7300 -> selecionado: fundo F2D93A + texto 1F2440
 * A troca de cores é animada e o chip dá um leve "apertão" ao ser tocado.
 */
@Composable
fun UmixChip(
    text: String,
    accent: ChipAccent,
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ring = if (accent == ChipAccent.Blue) UmixAccentBlue else UmixButtonYellow
    val idleText = if (accent == ChipAccent.Blue) UmixAccentBlue else UmixGold
    val selectedText = if (accent == ChipAccent.Blue) UmixWhite else UmixTextDark

    // Mantém o matiz ao animar (evita passar por "transparente preto")
    val container by animateColorAsState(
        targetValue = if (selected) ring else ring.copy(alpha = 0f),
        animationSpec = tween(260),
        label = "chipContainer"
    )
    val content by animateColorAsState(
        targetValue = if (selected) selectedText else idleText,
        animationSpec = tween(260),
        label = "chipContent"
    )

    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "chipScale"
    )

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .height(34.dp)
            .clip(CircleShape)
            .background(container)
            .border(1.dp, ring, CircleShape)
            .toggleable(
                value = selected,
                interactionSource = interaction,
                indication = null,
                role = Role.Checkbox,
                onValueChange = { onToggle() }
            )
            .padding(horizontal = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = UmixText.Chip, color = content)
    }
}
