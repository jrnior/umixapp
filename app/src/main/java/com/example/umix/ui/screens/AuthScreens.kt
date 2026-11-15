package com.example.umix.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.umix.R
import com.example.umix.ui.components.*
import com.example.umix.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// -----------------------------------------------------------------------------
// 1. TELA DE LOGIN (boas-vindas)  -> frame "tela de login"
// -----------------------------------------------------------------------------
@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onGoogleClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UmixCream)
            .safeDrawingPadding()
            .padding(horizontal = 52.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(56.dp))
        UmixLogo(width = 160.dp)
        Text(text = "uMix", style = UmixText.Wordmark)

        Spacer(Modifier.weight(1f))
        Text(
            text = "Ouça, avalie e descubra!",
            style = UmixText.Tagline,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1.6f))

        UmixButton(text = "Cadastrar-se", onClick = onSignUpClick)

        Spacer(Modifier.height(18.dp))
        Text(text = "Já possui uma conta?", style = UmixText.Small)
        Spacer(Modifier.height(6.dp))
        HorizontalDivider(
            modifier = Modifier.width(170.dp),
            thickness = 1.dp,
            color = UmixInk
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Entrar",
            style = UmixText.Small,
            modifier = Modifier
                .clickable(onClick = onSignInClick)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )

        Spacer(Modifier.height(6.dp))
        GoogleButton(onClick = onGoogleClick)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun GoogleButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .shadow(3.dp, CircleShape)
            .background(UmixWhite, CircleShape)
            .border(1.dp, UmixInk.copy(alpha = 0.08f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_google),
            contentDescription = "Continuar com Google",
            modifier = Modifier.size(30.dp)
        )
    }
}

// -----------------------------------------------------------------------------
// 2. TELA "ENTRAR"  -> frame "entrar"
// -----------------------------------------------------------------------------
@Composable
fun SignInScreen(
    onLoginSuccess: () -> Unit,
    onGoToSignUp: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    AuthScaffold {
        Text(text = "bem-vindo de volta!", style = UmixText.ScreenTitle)
        Spacer(Modifier.height(36.dp))

        AuthTabSwitch(
            selected = AuthTab.SignIn,
            onSelect = { if (it == AuthTab.SignUp) onGoToSignUp() },
            modifier = Modifier.padding(horizontal = 46.dp)
        )
        Spacer(Modifier.height(62.dp))

        Column(Modifier.padding(horizontal = 52.dp)) {
            UmixTextField(
                label = "Email:",
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(38.dp))
            UmixTextField(
                label = "Senha:",
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Text(
                text = "Esqueci minha senha",
                style = UmixText.Link.copy(textDecoration = TextDecoration.Underline),
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        Toast.makeText(context, "Recuperação de senha em breve", Toast.LENGTH_SHORT).show()
                    }
                    .padding(top = 6.dp, bottom = 4.dp)
            )
            Spacer(Modifier.height(40.dp))
            UmixButton(
                text = "Entrar",
                onClick = {
                    // Credenciais do protótipo original: junior / junior123
                    val user = email.trim()
                    if ((user == "junior" || user == "junior@umix.com") && password == "junior123") {
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, "Use: junior@umix.com / junior123", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 3. TELA "CADASTRO"  -> frame "cadastro"
// -----------------------------------------------------------------------------
@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onGoToSignIn: () -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthDigits by remember { mutableStateOf("") }
    val context = LocalContext.current

    AuthScaffold {
        Text(text = "Crie sua conta aqui!", style = UmixText.ScreenTitle)
        Spacer(Modifier.height(36.dp))

        AuthTabSwitch(
            selected = AuthTab.SignUp,
            onSelect = { if (it == AuthTab.SignIn) onGoToSignIn() },
            modifier = Modifier.padding(horizontal = 46.dp)
        )
        Spacer(Modifier.height(16.dp))

        Column(Modifier.padding(horizontal = 52.dp)) {
            UmixTextField(
                label = "Usuário:",
                value = username,
                onValueChange = { username = it }
            )
            Spacer(Modifier.height(22.dp))
            UmixTextField(
                label = "Email:",
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(22.dp))
            UmixTextField(
                label = "Senha:",
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(Modifier.height(22.dp))
            UmixTextField(
                label = "Data de nascimento:",
                value = birthDigits,
                onValueChange = { new -> birthDigits = new.filter { it.isDigit() }.take(8) },
                placeholder = "DD / MM / AAAA",
                centered = true,
                visualTransformation = BirthDateTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(38.dp))
            UmixButton(
                text = "Criar",
                onClick = {
                    val error = validateSignUp(username, email, password, birthDigits)
                    if (error == null) {
                        Toast.makeText(context, "Conta criada!", Toast.LENGTH_SHORT).show()
                        onSignUpSuccess()
                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

// -----------------------------------------------------------------------------
// Base comum das telas "Entrar" e "Cadastro": fundo, logo e rolagem
// -----------------------------------------------------------------------------
@Composable
private fun AuthScaffold(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UmixCream)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(56.dp))
        UmixLogo(width = 160.dp)
        Spacer(Modifier.height(20.dp))
        content()
        Spacer(Modifier.height(32.dp))
    }
}

private fun validateSignUp(
    username: String,
    email: String,
    password: String,
    birthDigits: String
): String? {
    if (username.isBlank()) return "Informe um nome de usuário"
    if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) return "Informe um email válido"
    if (password.length < 6) return "A senha precisa ter ao menos 6 caracteres"
    if (!isValidBirthDate(birthDigits)) return "Informe uma data de nascimento válida"
    return null
}

private fun isValidBirthDate(digits: String): Boolean {
    if (digits.length != 8) return false
    val format = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).apply { isLenient = false }
    val date = runCatching { format.parse(digits) }.getOrNull() ?: return false
    return !date.after(Date())
}
