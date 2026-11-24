package com.example.umix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

/**
 * Capa de álbum provisória (gradiente + nota musical).
 * Quando houver imagens reais (ex.: Coil/AsyncImage), é só trocar aqui.
 */
@Composable
fun AlbumArt(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    iconSize: Dp = 36.dp,
    imageUrl: String? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(Brush.linearGradient(colors)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.MusicNote,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.45f),
            modifier = Modifier.size(iconSize)
        )
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        content()
    }
}