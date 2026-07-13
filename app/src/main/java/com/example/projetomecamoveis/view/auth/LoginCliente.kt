package com.example.projetomecamoveis.view.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.ui.theme.LexendFontFamily
import com.example.projetomecamoveis.viewmodel.LoginClienteViewModel

@Composable
fun LoginCampoTexto(
    label: String,
    valor: String,
    onValorMuda: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    esconderTexto: Boolean = false,
    mensagemErro: String? = null
) {
    Column {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorMuda,
            placeholder = { Text(text = placeholder, color = Color(0xFF888888)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(28.dp),
            singleLine = true,
            visualTransformation = if (esconderTexto) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = mensagemErro != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3D3D3D),
                unfocusedContainerColor = Color(0xFF3D3D3D),
                focusedBorderColor = Color(0xFFFFBD49),
                unfocusedBorderColor = Color(0xFF555454),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                errorBorderColor = Color.Red
            )
        )
        if (mensagemErro != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mensagemErro,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun LoginClienteScreen(
    navController: NavHostController,
    viewModel: LoginClienteViewModel = viewModel()
) {
    val errorMessage by viewModel.errorMessage

    LoginClienteContent(
        navController = navController,
        errorMessage = errorMessage,
        onLoginClick = { email, pass ->
            viewModel.verificarLogin(email, pass) { cliente ->
                if (cliente != null) {
                    val nome = cliente.name.trim().split(" ").firstOrNull() ?: cliente.name
                    navController.navigate("home_cliente/${cliente.id}/$nome")
                }
            }
        }
    )
}

@Composable
fun LoginClienteContent(
    navController: NavHostController,
    errorMessage: String?,
    onLoginClick: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var palavraPasse by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- SETA VOLTAR ---
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left_28_regular),
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- LOGO ---
            Image(
                painter = painterResource(id = R.drawable.logotipo_completo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Inside
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- TÍTULO ---

            Row {
                Text(
                    text = "Login ",
                    color = Color(0xFFFFBD49),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = LexendFontFamily

                )

                Text(
                    text = "Cliente",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = LexendFontFamily
                )

            }

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(40.dp))
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    LoginCampoTexto(
                        label = "Email",
                        valor = email,
                        onValorMuda = { email = it },
                        placeholder = "Ex: joao123@gmail.com",
                        keyboardType = KeyboardType.Email
                    )

                    LoginCampoTexto(
                        label = "Palavra-Passe",
                        valor = palavraPasse,
                        onValorMuda = { palavraPasse = it },
                        placeholder = "",
                        keyboardType = KeyboardType.Password,
                        esconderTexto = true
                    )

                    if (errorMessage != null) {
                        Text(text = errorMessage, color = Color.Red, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- LINK CRIAR CONTA ---
            TextButton(onClick = { navController.navigate("criar_conta_cliente") }) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.White, fontSize = 14.sp)) {
                            append("Ainda não tem conta? ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFFFFBD49),
                                fontSize = 14.sp,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Criar Conta Cliente")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- BOTÃO ENTRAR ---
            Button(
                onClick = { onLoginClick(email, palavraPasse) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBD49))
            ) {
                Text(
                    text = "Entrar",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = LexendFontFamily
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLogin() {
    LoginClienteContent(
        navController = rememberNavController(),
        errorMessage = null,
        onLoginClick = { _, _ -> }
    )
}
