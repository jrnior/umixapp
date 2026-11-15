package com.example.umix

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.umix.ui.components.NavTab
import com.example.umix.ui.components.UmixBottomBar
import com.example.umix.ui.screens.HomeScreen
import com.example.umix.ui.screens.PremiumScreen
import com.example.umix.ui.screens.SearchScreen
import com.example.umix.ui.screens.SearchUiState
import com.example.umix.ui.screens.SignInScreen
import com.example.umix.ui.screens.SignUpScreen
import com.example.umix.ui.screens.WelcomeScreen
import com.example.umix.ui.theme.UmixCream
import com.example.umix.ui.theme.UmixTheme

sealed class Screen {
    object Welcome : Screen()
    object SignIn : Screen()
    object SignUp : Screen()
    object Main : Screen()        // Início / Buscar / Perfil (com a barra de navegação)
    object Premium : Screen()
    object Evaluation : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UmixTheme {
                UMixApp()
            }
        }
    }
}

@Composable
fun UMixApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
    var tab by remember { mutableStateOf(NavTab.Home) }
    val searchState = remember { SearchUiState() }
    val context = LocalContext.current

    // Botão voltar do sistema
    BackHandler(enabled = currentScreen !is Screen.Welcome && !(currentScreen is Screen.Main && tab == NavTab.Home)) {
        when (currentScreen) {
            is Screen.Main -> tab = NavTab.Home
            is Screen.Premium -> currentScreen = Screen.Main
            is Screen.Evaluation -> {
                tab = NavTab.Profile
                currentScreen = Screen.Main
            }
            else -> currentScreen = Screen.Welcome
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = UmixCream) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { screenTransition() },
            label = "screens"
        ) { screen ->
            when (screen) {
                is Screen.Welcome -> WelcomeScreen(
                    onSignUpClick = { currentScreen = Screen.SignUp },
                    onSignInClick = { currentScreen = Screen.SignIn },
                    onGoogleClick = {
                        Toast.makeText(context, "Login com Google em breve", Toast.LENGTH_SHORT).show()
                    }
                )
                is Screen.SignIn -> SignInScreen(
                    onLoginSuccess = { tab = NavTab.Home; currentScreen = Screen.Main },
                    onGoToSignUp = { currentScreen = Screen.SignUp }
                )
                is Screen.SignUp -> SignUpScreen(
                    onSignUpSuccess = { tab = NavTab.Home; currentScreen = Screen.Main },
                    onGoToSignIn = { currentScreen = Screen.SignIn }
                )
                is Screen.Main -> MainShell(
                    tab = tab,
                    onTabChange = { tab = it },
                    searchState = searchState,
                    onPremiumClick = { currentScreen = Screen.Premium },
                    onNavigateToEvaluation = { currentScreen = Screen.Evaluation },
                    onLogout = { currentScreen = Screen.Welcome }
                )
                is Screen.Premium -> PremiumScreen(
                    onClose = { currentScreen = Screen.Main },
                    onSubscribe = { plan ->
                        Toast.makeText(
                            context,
                            "Plano ${plan.label} selecionado. Pagamento em breve!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
                is Screen.Evaluation -> EvaluationScreen(
                    onBack = {
                        tab = NavTab.Profile
                        currentScreen = Screen.Main
                    }
                )
            }
        }
    }
}

/**
 * Casca do app logado: o conteúdo troca de aba com animação e a barra de navegação
 * fica fixa por cima, animando só a pílula selecionada.
 */
@Composable
fun MainShell(
    tab: NavTab,
    onTabChange: (NavTab) -> Unit,
    searchState: SearchUiState,
    onPremiumClick: () -> Unit,
    onNavigateToEvaluation: () -> Unit,
    onLogout: () -> Unit,
) {
    Box(Modifier.fillMaxSize().background(UmixCream)) {
        AnimatedContent(
            targetState = tab,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                val dir = if (targetState.ordinal > initialState.ordinal) 1 else -1
                (slideInHorizontally(tween(340, easing = FastOutSlowInEasing)) { (it * 0.18f).toInt() * dir } +
                    fadeIn(tween(300, delayMillis = 50))) togetherWith
                    (slideOutHorizontally(tween(340, easing = FastOutSlowInEasing)) { -(it * 0.18f).toInt() * dir } +
                        fadeOut(tween(160)))
            },
            label = "tabs"
        ) { current ->
            when (current) {
                NavTab.Home -> HomeScreen(onPremiumClick = onPremiumClick)
                NavTab.Search -> SearchScreen(state = searchState)
                NavTab.Profile -> ProfileScreen(
                    onNavigateToEvaluation = onNavigateToEvaluation,
                    onLogout = onLogout
                )
            }
        }
        UmixBottomBar(
            selected = tab,
            onSelect = onTabChange,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        )
    }
}

// ----------------------------- Transições entre telas -----------------------------
private fun Screen.level(): Int = when (this) {
    is Screen.Welcome -> 0
    is Screen.SignIn -> 1
    is Screen.SignUp -> 2
    is Screen.Main -> 3
    is Screen.Premium -> 4
    is Screen.Evaluation -> 4
}

private fun AnimatedContentTransitionScope<Screen>.screenTransition(): ContentTransform {
    val from = initialState
    val to = targetState
    val ease = FastOutSlowInEasing
    return when {
        // Premium sobe de baixo como um "modal"; a tela de trás recua levemente
        to is Screen.Premium ->
            (slideInVertically(tween(420, easing = ease)) { it / 4 } + fadeIn(tween(300))) togetherWith
                (scaleOut(tween(420, easing = ease), targetScale = 0.94f) + fadeOut(tween(300)))

        // Fechar Premium: ele desce por cima enquanto a tela de trás volta ao tamanho normal
        from is Screen.Premium ->
            ((scaleIn(tween(420, easing = ease), initialScale = 0.94f) + fadeIn(tween(300))) togetherWith
                (slideOutVertically(tween(380, easing = ease)) { it / 4 } + fadeOut(tween(300))))
                .apply { targetContentZIndex = -1f }

        // Entrar no app depois do login/cadastro: zoom suave
        to is Screen.Main && (from is Screen.SignIn || from is Screen.SignUp) ->
            (fadeIn(tween(380, delayMillis = 60)) + scaleIn(tween(420, easing = ease), initialScale = 0.92f)) togetherWith
                fadeOut(tween(220))

        // Sair (logout): esmaece de volta para a tela inicial
        to is Screen.Welcome && from is Screen.Main ->
            fadeIn(tween(350)) togetherWith (fadeOut(tween(250)) + scaleOut(tween(350), targetScale = 1.05f))

        // Demais: deslize horizontal na direção da navegação
        else -> {
            val dir = if (to.level() > from.level()) 1 else -1
            (slideInHorizontally(tween(380, easing = ease)) { (it * 0.25f).toInt() * dir } +
                fadeIn(tween(300, delayMillis = 60))) togetherWith
                (slideOutHorizontally(tween(380, easing = ease)) { -(it * 0.25f).toInt() * dir } +
                    fadeOut(tween(200)))
        }
    }
}

// -----------------------------------------------------------------------------
// TELA DE PERFIL (PROTÓTIPO)
// -----------------------------------------------------------------------------
@Composable
fun ProfileScreen(onNavigateToEvaluation: () -> Unit, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Perfil do Usuário")
            Button(onClick = onLogout) {
                Text(text = "Sair")
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(text = "Usuário: Junior")
        Text(text = "Status: Membro Premium")
        Text(text = "Artista do Mês: Radiohead")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToEvaluation,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Avaliar Novo Álbum")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Histórico de Avaliações:")

        val userReviews = remember {
            listOf(
                ReviewItem("OK Computer", "Radiohead", 5, "Uma verdadeira obra-prima."),
                ReviewItem("Currents", "Tame Impala", 4, "Produção muito boa.")
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(userReviews) { review ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "${review.albumTitle} - ${review.artist}")
                        Text(text = "Nota: ${review.rating}/5")
                        Text(text = review.comment)
                    }
                }
            }
        }
    }
}

data class ReviewItem(val albumTitle: String, val artist: String, val rating: Int, val comment: String)

// -----------------------------------------------------------------------------
// TELA DE AVALIAÇÃO (PROTÓTIPO)
// -----------------------------------------------------------------------------
@Composable
fun EvaluationScreen(onBack: () -> Unit) {
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) {
                Text(text = "Voltar")
            }
            Text(text = "Tela de Avaliação")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Álbum: Pablo Honey")
        Text(text = "Artista: Radiohead (1993)")
        Text(text = "ISRC: GBAYE9200070")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Selecione a nota (1 a 5):")
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (i in 1..5) {
                Text(
                    text = "[$i]",
                    modifier = Modifier.clickable { rating = i }
                )
            }
        }
        Text(text = "Nota selecionada: $rating")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { Text("Escreva seu comentário...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (rating == 0) {
                    Toast.makeText(context, "Selecione uma nota!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Avaliação enviada!", Toast.LENGTH_SHORT).show()
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Salvar")
        }
    }
}