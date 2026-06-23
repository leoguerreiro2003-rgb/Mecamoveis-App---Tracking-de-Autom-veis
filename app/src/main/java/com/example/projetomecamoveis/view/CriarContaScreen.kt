package com.example.projetomecamoveis.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.viewmodel.CriarContaClienteViewModel
import com.example.projetomecamoveis.viewmodel.LoginClienteViewModel

// ─────────────────────────────────────────────
// ECRÃ DE CRIAÇÃO DE CONTA DO CLIENTE
// Usa dois ViewModels:
//   - CriarContaClienteViewModel → valida os campos do formulário
//   - LoginClienteViewModel      → adiciona o cliente à lista após validação
// ─────────────────────────────────────────────
@Composable
fun CriarContaClienteScreen(
    navController: NavController,
    criarViewModel: CriarContaClienteViewModel = viewModel(),
    loginViewModel: LoginClienteViewModel = viewModel()
) {

    // Estados locais dos campos de texto — só vivem enquanto este ecrã está ativo
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var palavraPasse by remember { mutableStateOf("") }
    var confirmarPalavraPasse by remember { mutableStateOf("") }

    // Observa as mensagens de erro do ViewModel
    val errorMessage by criarViewModel.errorMessage
    val emailError by criarViewModel.emailError
    val contactoError by criarViewModel.contactoError
    val passwordError by criarViewModel.passwordError

    // Observa o estado de sucesso — quando true, navega para o login
    val registoSucesso by criarViewModel.registoSucesso

    // LaunchedEffect observa registoSucesso — navega quando o registo é bem-sucedido
    LaunchedEffect(registoSucesso) {
        if (registoSucesso) {
            criarViewModel.resetarSucesso() // limpa o flag para não navegar outra vez
            navController.navigate("login_cliente") {
                popUpTo("criar_conta_cliente") { inclusive = true } // remove este ecrã do histórico
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // scroll necessário porque há muitos campos
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

            Row {
                Text(text = "Criação de Conta:  ", color = Color(0xFFFFA500), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Cliente", color = Color(0xFFFFA500), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    // Cada CampoTexto é um campo reutilizável definido no fundo deste ficheiro
                    CampoTexto(
                        label = "Nome",
                        valor = nome,
                        onValorMuda = { nome = it },
                        placeholder = "Ex: Nome e Sobrenome",
                        keyboardType = KeyboardType.Text
                    )
                    CampoTexto(
                        label = "Email",
                        valor = email,
                        onValorMuda = { email = it },
                        placeholder = "Ex: joao123@gmail.com",
                        keyboardType = KeyboardType.Email,
                        mensagemErro = emailError
                    )
                    CampoTexto(
                        label = "Contacto Telefónico",
                        valor = contacto,
                        onValorMuda = { contacto = it },
                        placeholder = "Ex: 123 456 789",
                        keyboardType = KeyboardType.Phone,
                        mensagemErro = contactoError
                    )
                    CampoTexto(
                        label = "Data de Nascimento",
                        valor = dataNascimento,
                        onValorMuda = { dataNascimento = it },
                        placeholder = "Ex: 11/05/2001",
                        keyboardType = KeyboardType.Number
                    )
                    CampoTexto(
                        label = "Palavra Passe",
                        valor = palavraPasse,
                        onValorMuda = { palavraPasse = it },
                        placeholder = "",
                        keyboardType = KeyboardType.Password,
                        esconderTexto = true,
                        mensagemErro = passwordError
                    )
                    CampoTexto(
                        label = "Confirmar Palavra Passe",
                        valor = confirmarPalavraPasse,
                        onValorMuda = { confirmarPalavraPasse = it },
                        placeholder = "",
                        keyboardType = KeyboardType.Password,
                        esconderTexto = true
                    )

                    // Mensagem de erro geral (ex: campos vazios)
                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = Color.Red, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Link para voltar ao login se já tiver conta
            TextButton(onClick = { navController.navigate("login_cliente") }) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.White, fontSize = 14.sp)) { append("Já tem conta? ") }
                        withStyle(style = SpanStyle(color = Color(0xFFFFA500), fontSize = 14.sp, textDecoration = TextDecoration.Underline)) { append("Login") }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão de criar conta — delega toda a lógica ao ViewModel
            Button(
                onClick = {
                    criarViewModel.validarECriarCliente(
                        nome, email, contacto, dataNascimento, palavraPasse, confirmarPalavraPasse
                    ) { novoCliente ->
                        loginViewModel.adicionarCliente(novoCliente)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
            ) {
                Text(text = "Criar Conta de Cliente", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────
// COMPONENTE REUTILIZÁVEL — Campo de texto do formulário
// Usado em todos os ecrãs com formulários (criar conta, editar perfil, etc.)
// "esconderTexto" → se true, esconde os caracteres (para passwords)
// ─────────────────────────────────────────────
@Composable
fun CampoTexto(
    label: String,
    valor: String,
    onValorMuda: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    esconderTexto: Boolean = false,
    mensagemErro: String? = null
) {
    Column {
        Text(text = label, color = Color.White, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorMuda,
            placeholder = { Text(text = placeholder, color = Color(0xFF888888)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            // Se esconderTexto for true usa PasswordVisualTransformation, caso contrário mostra o texto normal
            visualTransformation = if (esconderTexto) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = mensagemErro != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3D3D3D),
                unfocusedContainerColor = Color(0xFF3D3D3D),
                focusedBorderColor = Color(0xFFFFA500),
                unfocusedBorderColor = Color.Transparent,
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
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
