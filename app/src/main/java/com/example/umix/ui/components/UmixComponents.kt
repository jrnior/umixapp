package com.example.umix.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.umix.R
import com.example.umix.ui.theme.*

/** Logo vetorial do uMix (convertida do logo-umix.svg). */
@Composable
fun UmixLogo(modifier: Modifier = Modifier, width: Dp = 160.dp) {
    Image(
        painter = painterResource(R.drawable.ic_umix_logo),
        contentDescription = "Logo uMix",
        modifier = modifier
            .width(width)
            .aspectRatio(170f / 79f)
    )
}

/** Botão principal azul-marinho em formato de pílula. */
@Composable
fun UmixButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = UmixNavy,
            contentColor = UmixWhite
        )
    ) {
        Text(text = text, style = UmixText.Button)
    }
}

/** Campo de texto amarelo com rótulo em cima ("Email:", "Senha:" ...). */
@Composable
fun UmixTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    centered: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = UmixText.FieldLabel,
            modifier = Modifier.padding(start = 10.dp, bottom = 4.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = UmixText.FieldInput.copy(
                textAlign = if (centered) TextAlign.Center else TextAlign.Start
            ),
            cursorBrush = SolidColor(UmixNavy),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UmixYellowField, CircleShape)
                        .padding(horizontal = 24.dp),
                    contentAlignment = if (centered) Alignment.Center else Alignment.CenterStart
                ) {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = UmixText.FieldInput.copy(color = UmixYellowHint),
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

enum class AuthTab { SignIn, SignUp }

/** Seletor "Entrar | Cadastrar-se" das telas de autenticação. */
@Composable
fun AuthTabSwitch(
    selected: AuthTab,
    onSelect: (AuthTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(CircleShape)
            .border(2.dp, UmixNavy, CircleShape)
    ) {
        AuthTabItem("Entrar", selected == AuthTab.SignIn, { onSelect(AuthTab.SignIn) }, Modifier.weight(1f))
        AuthTabItem("Cadastrar-se", selected == AuthTab.SignUp, { onSelect(AuthTab.SignUp) }, Modifier.weight(1f))
    }
}

@Composable
private fun AuthTabItem(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(if (selected) UmixNavy else UmixYellowField)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = UmixText.Tab,
            color = if (selected) UmixYellow else UmixNavy
        )
    }
}

/** Máscara "DD / MM / AAAA" aplicada sobre os dígitos digitados (até 8). */
object BirthDateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = buildString {
            digits.forEachIndexed { i, c ->
                if (i == 2 || i == 4) append(" / ")
                append(c)
            }
        }
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = when {
                offset <= 2 -> offset
                offset <= 4 -> offset + 3
                else -> offset + 6
            }

            override fun transformedToOriginal(offset: Int): Int = when {
                offset <= 2 -> offset
                offset <= 5 -> 2
                offset <= 7 -> offset - 3
                offset <= 10 -> 4
                else -> minOf(offset - 6, digits.length)
            }
        }
        return TransformedText(AnnotatedString(formatted), mapping)
    }
}
