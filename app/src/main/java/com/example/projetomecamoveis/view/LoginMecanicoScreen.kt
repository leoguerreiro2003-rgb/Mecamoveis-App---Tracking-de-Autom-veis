package com.example.projetomecamoveis.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.viewmodel.LoginMecanicoViewModel

// ─────────────────────────────────────────────
// ECRÃ DE LOGIN DO MECÂNICO
// Segue exatamente o mesmo padrão do LoginClienteScreen
// A lógica de verificação está no LoginMecanicoViewModel
// ─────────────────────────────────────────────
@Composable
fun LoginMecanicoScreen(
    navController: NavController,
    viewModel: LoginMecanicoViewModel = viewModel() // ViewModel injetado
) {

    // Estados locais dos campos — apenas para o que o utilizador está a escrever
    var email by remember { mutableStateOf("") }
    var palavraPasse by remember { mutableStateOf("") }

    // Observa a mensagem de erro do ViewModel
    // Se o ViewModel atualizar errorMessage, a UI redesenha automaticamente
    val errorMessage by viewModel.errorMessage

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

            Image(
                painter = painterResource(id = R.drawable.logotipoprototipo),
                contentDescription = "Logo",
                modifier = Modifier.size(135.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Login Mecânico",
                color = Color(0xFFFFA500),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Column {

                    // Campo de email
                    Text(text = "Email", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text(text = "Ex: contactomecanico@gmail.com", color = Color(0xFF888888)) },
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

                    // Campo de palavra-passe com caracteres escondidos
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

                    // Mostra mensagem de erro se o login falhar
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = errorMessage!!, color = Color.Red, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botão de entrada
            Button(
                onClick = {
                    viewModel.verificarLoginMecanico(email, palavraPasse) { mecanico ->
                        if (mecanico != null) {
                            val nome = mecanico.nome.trim().split(" ").firstOrNull() ?: mecanico.nome
                            navController.navigate("home_mecanico/$nome")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
            ) {
                Text(
                    text = "Entrar",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
