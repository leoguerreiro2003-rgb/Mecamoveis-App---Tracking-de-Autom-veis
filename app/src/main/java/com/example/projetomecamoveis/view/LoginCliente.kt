package com.example.projetomecamoveis.view

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.viewmodel.LoginClienteViewModel

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
                painter = painterResource(id = R.drawable.logotipoprototipo),
                contentDescription = "Logo",
                modifier = Modifier.size(135.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- TÍTULO ---
            Text(
                text = "Login Cliente",
                color = Color(0xFFFFA500),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            // --- CAIXA DOS CAMPOS ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(text = "Email", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text(text = "Ex: joao123@gmail.com", color = Color(0xFF888888)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF3D3D3D),
                            unfocusedContainerColor = Color(0xFF3D3D3D),
                            focusedBorderColor = Color(0xFFFFA500),
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Palavra-Passe", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = palavraPasse,
                        onValueChange = { palavraPasse = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF3D3D3D),
                            unfocusedContainerColor = Color(0xFF3D3D3D),
                            focusedBorderColor = Color(0xFFFFA500),
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
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
                        withStyle(style = SpanStyle(color = Color(0xFFFFA500), fontSize = 14.sp, textDecoration = TextDecoration.Underline)) {
                            append("Criar Conta Cliente")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- BOTÃO ENTRAR ---
            Button(
                onClick = { onLoginClick(email, palavraPasse) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
            ) {
                Text(text = "Entrar", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLogin() {
    LoginClienteContent(navController = rememberNavController(), errorMessage = null, onLoginClick = { _, _ -> })
}
