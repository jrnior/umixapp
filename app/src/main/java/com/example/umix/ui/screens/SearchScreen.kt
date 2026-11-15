package com.example.umix.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.umix.ui.components.AlbumArt
import com.example.umix.ui.components.ChipAccent
import com.example.umix.ui.components.UmixChip
import com.example.umix.ui.theme.*

/** Estado da busca: fica no app (fora da tela) para sobreviver à troca de abas. */
class SearchUiState {
    var query by mutableStateOf("")
    // Seleção inicial = a mostrada no Figma; toque para marcar/desmarcar
    val moods = mutableStateListOf("Alegre", "Relaxar")
    val occasions = mutableStateListOf("Estudo", "Heartbreak")
}

private fun SnapshotStateList<String>.toggle(item: String) {
    if (!remove(item)) add(item)
}

// ---------- Dados de exemplo ----------
private val moodTags = listOf(
    "Alegre" to ChipAccent.Blue, "Nostálgico" to ChipAccent.Yellow, "Intenso" to ChipAccent.Blue,
    "Relaxar" to ChipAccent.Yellow, "Foco" to ChipAccent.Blue, "Dançar" to ChipAccent.Yellow,
)
private val occasionTags = listOf(
    "Treino" to ChipAccent.Blue, "Estudo" to ChipAccent.Blue, "Festa" to ChipAccent.Yellow,
    "Chuva" to ChipAccent.Blue, "Road trip" to ChipAccent.Blue, "Heartbreak" to ChipAccent.Yellow,
)

private data class SearchCommunity(val name: String, val members: String, val color: Color)
private data class TopComment(val rank: Int, val title: String, val artist: String, val count: String, val colors: List<Color>)

private val searchCommunities = listOf(
    SearchCommunity("Pagode no Quintal", "5,4k membros", UmixCommunityBlue),
    SearchCommunity("Música Boa", "900 membros", UmixYellowBright),
)
private val topComments = listOf(
    TopComment(1, "Okê", "Anitta", "412", listOf(Color(0xFFE6D5C3), Color(0xFFB5503F))),
    TopComment(2, "Sequência Cunt", "Pedro Sampaio", "301", listOf(Color(0xFFE63E9B), Color(0xFF2A2F6B))),
    TopComment(3, "Let it Go", "Frozen", "276", listOf(Color(0xFF2B6FA8), Color(0xFF0B1B3A))),
)

@Composable
fun SearchScreen(state: SearchUiState) {
    val query = state.query.trim()
    fun matches(vararg fields: String) = query.isEmpty() || fields.any { it.contains(query, ignoreCase = true) }
    val noTop = topComments.none { matches(it.title, it.artist) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UmixCream)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 16.dp, bottom = 112.dp)
    ) {
        Text(
            text = "Buscar",
            style = TextStyle(fontFamily = Fraunces, fontWeight = FontWeight.Bold, fontSize = 24.sp),
            color = UmixTextDark
        )
        Spacer(Modifier.height(14.dp))
        SearchField(query = state.query, onQueryChange = { state.query = it })

        Spacer(Modifier.height(22.dp))
        SearchSectionTitle("Mood")
        Spacer(Modifier.height(10.dp))
        ChipGrid(tags = moodTags, selected = state.moods, onToggle = state.moods::toggle)

        Spacer(Modifier.height(22.dp))
        SearchSectionTitle("Ocasião")
        Spacer(Modifier.height(10.dp))
        ChipGrid(tags = occasionTags, selected = state.occasions, onToggle = state.occasions::toggle)

        Spacer(Modifier.height(22.dp))
        SearchSectionTitle("Comunidades em alta")
        Spacer(Modifier.height(14.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            searchCommunities.forEach { c ->
                AnimatedVisibility(
                    visible = matches(c.name),
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    CommunityItem(c, Modifier.padding(end = 24.dp))
                }
            }
        }

        Spacer(Modifier.height(22.dp))
        SearchSectionTitle("Top comentados")
        Spacer(Modifier.height(8.dp))
        topComments.forEach { item ->
            AnimatedVisibility(
                visible = matches(item.title, item.artist),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                TopCommentRow(item)
            }
        }
        AnimatedVisibility(visible = noTop, enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = "Nenhum resultado para “${state.query.trim()}”",
                style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 12.sp),
                color = UmixTextSoft,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun SearchSectionTitle(text: String) {
    Text(text = text, style = UmixText.SectionTitleSerif.copy(fontSize = 15.sp))
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    val focusManager: FocusManager = LocalFocusManager.current
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 13.sp, color = UmixTextDark),
        cursorBrush = SolidColor(UmixNavy),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
        decorationBox = { inner ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(UmixCardYellow.copy(alpha = 0.8f))
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = UmixTextSoft,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (query.isEmpty()) {
                        Text(
                            text = "artistas, álbuns, pessoas, comunidades",
                            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 12.sp),
                            color = UmixTextSoft,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    inner()
                }
                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = fadeIn() + scaleIn(initialScale = 0.6f),
                    exit = fadeOut() + scaleOut(targetScale = 0.6f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Limpar busca",
                        tint = UmixTextSoft,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onQueryChange("") }
                    )
                }
            }
        }
    )
}

/** Chips em linhas de 3, como no Figma. */
@Composable
private fun ChipGrid(
    tags: List<Pair<String, ChipAccent>>,
    selected: List<String>,
    onToggle: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        tags.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { (label, accent) ->
                    UmixChip(
                        text = label,
                        accent = accent,
                        selected = label in selected,
                        onToggle = { onToggle(label) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunityItem(community: SearchCommunity, modifier: Modifier = Modifier) {
    Column(modifier = modifier.width(96.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(community.color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Group,
                contentDescription = null,
                tint = UmixNavy,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = community.name,
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 10.sp),
            color = UmixTextDark,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = community.members,
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 9.sp),
            color = UmixTextSoft
        )
    }
}

@Composable
private fun TopCommentRow(item: TopComment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.rank.toString(),
            style = TextStyle(fontFamily = Fraunces, fontWeight = FontWeight.Bold, fontSize = 15.sp),
            color = UmixAccentBlue,
            modifier = Modifier.width(24.dp)
        )
        AlbumArt(
            colors = item.colors,
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(8.dp),
            iconSize = 18.dp
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 13.sp),
                color = UmixTextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.artist,
                style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 10.sp),
                color = UmixTextSoft
            )
        }
        Text(text = item.count, style = UmixText.Mono10, color = UmixAccentBlue)
    }
}
