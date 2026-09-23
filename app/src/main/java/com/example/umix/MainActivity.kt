package com.example.umix

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

sealed class Screen {
    object Login : Screen()
    object Profile : Screen()
    object Evaluation : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                UMixApp()
            }
        }
    }
}

@Composable
fun UMixApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            is Screen.Login -> LoginScreen(onLoginSuccess = { currentScreen = Screen.Profile })
            is Screen.Profile -> ProfileScreen(
                onNavigateToEvaluation = { currentScreen = Screen.Evaluation },
                onLogout = { currentScreen = Screen.Login }
            )
            is Screen.Evaluation -> EvaluationScreen(
                onBack = { currentScreen = Screen.Profile }
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 1. TELA DE LOGIN (PROTÓTIPO)
// -----------------------------------------------------------------------------
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "uMix - Protótipo")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username.trim() == "junior" && password == "junior123") {
                    onLoginSuccess()
                } else {
                    Toast.makeText(context, "Use: junior / junior123", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Entrar")
        }
    }
}

// -----------------------------------------------------------------------------
// 2. TELA DE PERFIL (PROTÓTIPO)
// -----------------------------------------------------------------------------
@Composable
fun ProfileScreen(onNavigateToEvaluation: () -> Unit, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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

        Divider(modifier = Modifier.padding(vertical = 8.dp))

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
// 3. TELA DE AVALIAÇÃO (PROTÓTIPO)
// -----------------------------------------------------------------------------
@Composable
fun EvaluationScreen(onBack: () -> Unit) {
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
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