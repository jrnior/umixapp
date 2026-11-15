package com.example.umix.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.umix.ui.components.AlbumArt
import com.example.umix.ui.components.UmixLogo
import com.example.umix.ui.theme.*

// ---------- Dados de exemplo (trocar por dados reais depois) ----------
private data class AlbumUi(val title: String, val artist: String, val rating: String, val colors: List<Color>)
private data class ReviewUi(val author: String, val stars: Int, val text: String)
private data class TopicUi(val comments: String, val title: String, val author: String)
private data class CommunityUi(val name: String, val color: Color)

private val albums = listOf(
    AlbumUi("Equilibrium", "Anitta", "4.6", listOf(Color(0xFFEBDCC8), Color(0xFFC9473A))),
    AlbumUi("Arirang", "BTS", "4.2", listOf(Color(0xFF454A63), Color(0xFF14172B))),
    AlbumUi("The sin: Bliss", "Enhypen", "3.9", listOf(Color(0xFFF2D93A), Color(0xFF8A7300))),
)
private val reviews = listOf(
    ReviewUi("Bruno B.", 4, "“Equilibrium” prova que o retorno não precisa repetir a fórmula."),
    ReviewUi("Junior G.", 3, "Produção impecável, mas a segunda metade perde o fôlego da abertura."),
)
private val topics = listOf(
    TopicUi("132 comentários", "O novo álbum do Cortis é o melhor do ano?", "iniciado por @açaidoJames"),
    TopicUi("88 comentários", "BTS deixou de ser hit depois que foram cancelados?", "iniciado por @gatinhadoJK"),
)
private val communities = listOf(
    CommunityUi("MPB raiz", UmixYellowBright),
    CommunityUi("Vinil & café", UmixCommunityBlue),
    CommunityUi("Indie BR", UmixYellowBright),
    CommunityUi("Rock clássico", UmixCommunityBlue),
    CommunityUi("Jazz & chill", UmixYellowBright),
)

@Composable
fun HomeScreen(onPremiumClick: () -> Unit) {
    val context = LocalContext.current
    val soon: () -> Unit = { Toast.makeText(context, "Em breve", Toast.LENGTH_SHORT).show() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UmixCream)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 112.dp) // espaço para a barra de navegação flutuante
    ) {
        HomeHeader(onPremiumClick = onPremiumClick, onBellClick = soon)

        Spacer(Modifier.height(10.dp))
        SectionHeader("Na crítica essa semana", onSeeMore = soon)
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            albums.forEach { AlbumCard(it, Modifier.weight(1f)) }
        }

        Spacer(Modifier.height(22.dp))
        SectionHeader("Reviews em alta", onSeeMore = soon)
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            reviews.forEach { ReviewCard(it, Modifier.weight(1f).fillMaxHeight()) }
        }

        Spacer(Modifier.height(22.dp))
        SectionHeader("Tópicos do momento", onSeeMore = soon)
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            topics.forEach { TopicCard(it, Modifier.weight(1f).fillMaxHeight(), onClick = soon) }
        }

        Spacer(Modifier.height(22.dp))
        // No Figma este título está em serifa (os outros estão em sans)
        SectionHeader("Comunidades pra você", onSeeMore = soon, serif = true)
        Spacer(Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(communities) { CommunityBubble(it, onClick = soon) }
        }

        Spacer(Modifier.height(18.dp))
        DiscoverButton(onClick = soon)
    }
}

// ---------- Cabeçalho ----------
@Composable
private fun HomeHeader(onPremiumClick: () -> Unit, onBellClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UmixLogo(width = 40.dp)
        Spacer(Modifier.width(6.dp))
        Text("uMix", style = UmixText.HeaderWordmark)
        Spacer(Modifier.weight(1f))

        // "Seja Premium" -> abre a tela de assinatura
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(UmixNavy)
                .clickable(onClick = onPremiumClick)
                .padding(horizontal = 13.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Seja Premium",
                style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
                color = UmixYellow
            )
        }
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(UmixCardYellow)
                .clickable(onClick = onBellClick)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notificações",
                tint = UmixTextDark,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(19.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 7.dp, end = 8.dp)
                    .size(7.dp)
                    .background(UmixBlue, CircleShape)
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, onSeeMore: () -> Unit, serif: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = if (serif) UmixText.SectionTitleSerif else UmixText.SectionTitle
        )
        Text(
            text = "ver mais",
            style = UmixText.SeeMore,
            modifier = Modifier
                .clickable(onClick = onSeeMore)
                .padding(vertical = 4.dp)
        )
    }
}

// ---------- Cards ----------
@Composable
private fun AlbumCard(album: AlbumUi, modifier: Modifier = Modifier) {
    Column(modifier) {
        AlbumArt(
            colors = album.colors,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(105f / 97f)
        ) {
            Text(
                text = album.rating,
                style = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 10.sp),
                color = UmixYellow,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(UmixNavy)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = album.title,
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 12.sp),
            color = UmixTextDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = album.artist,
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 11.sp),
            color = UmixTextSoft,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ReviewCard(review: ReviewUi, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(UmixCardYellow)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .background(UmixNavy, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = review.author.first().toString(),
                    style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 11.sp),
                    color = UmixYellow
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = review.author,
                style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 12.sp),
                color = UmixTextDark
            )
        }
        Spacer(Modifier.height(4.dp))
        Row {
            repeat(5) { i ->
                Icon(
                    imageVector = if (i < review.stars) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                    contentDescription = null,
                    tint = UmixButtonYellow,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = review.text,
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 15.sp),
            color = UmixTextSoft
        )
    }
}

@Composable
private fun TopicCard(topic: TopicUi, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(UmixNavy)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocalFireDepartment,
                    contentDescription = null,
                    tint = UmixYellow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = topic.comments,
                    style = TextStyle(fontFamily = Mono, fontSize = 10.sp),
                    color = UmixYellow
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = topic.title,
                style = TextStyle(fontFamily = Fraunces, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 18.sp),
                color = UmixWhite
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = topic.author,
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Normal, fontSize = 9.sp),
            color = Color(0xFFB3B9D6)
        )
    }
}

@Composable
private fun CommunityBubble(community: CommunityUi, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(84.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .background(community.color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Group,
                contentDescription = null,
                tint = UmixNavy,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = community.name,
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 12.sp),
            color = UmixTextDark,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DiscoverButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(52.dp)
            .clip(CircleShape)
            .background(UmixAccentBlue)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Descubra mais",
            style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
            color = UmixWhite
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = UmixWhite,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 22.dp)
                .size(20.dp)
        )
    }
}
