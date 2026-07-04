package com.example.projetomecamoveis.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.viewmodel.VeiculoViewModel

@Composable
fun NovoVeiculoScreen(
    navController: NavHostController,
    clienteId: Int,
    viewModel: VeiculoViewModel = viewModel()
) {
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var kms by remember { mutableStateOf("") }

    val errorMessage by viewModel.errorMessage
    val matriculaError by viewModel.matriculaError
    val addSucesso by viewModel.addSucesso

    LaunchedEffect(addSucesso) {
        if (addSucesso) {
            viewModel.resetarSucesso()
            navController.popBackStack()
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // --- BARRA SUPERIOR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left_28_regular),
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Text(
                    text = "Novo Veículo",
                    color = Color(0xFFFFA500),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color(0xFFFFA500),
                    modifier = Modifier.size(45.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- CONTENT BOX ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                    .padding(horizontal = 20.dp, vertical = 30.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    
                    CampoTexto(
                        label = "Marca",
                        valor = marca,
                        onValorMuda = { marca = it },
                        placeholder = "Ex: Volvo",
                        keyboardType = KeyboardType.Text
                    )
                    
                    CampoTexto(
                        label = "Modelo",
                        valor = modelo,
                        onValorMuda = { modelo = it },
                        placeholder = "Ex: V40",
                        keyboardType = KeyboardType.Text
                    )
                    
                    CampoTexto(
                        label = "Matrícula",
                        valor = matricula,
                        onValorMuda = { matricula = it },
                        placeholder = "Ex: AA-00-AA",
                        keyboardType = KeyboardType.Text,
                        mensagemErro = matriculaError
                    )
                    
                    CampoTexto(
                        label = "Ano",
                        valor = ano,
                        onValorMuda = { ano = it },
                        placeholder = "Ex: 2012",
                        keyboardType = KeyboardType.Number
                    )
                    
                    CampoTexto(
                        label = "Contagem de KM",
                        valor = kms,
                        onValorMuda = { kms = it },
                        placeholder = "Ex: 100,000 km",
                        keyboardType = KeyboardType.Text
                    )

                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = Color.Red, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // BOTÃO CRIAR (Estilo link/texto no fundo da caixa cinza conforme imagem)
                    TextButton(
                        onClick = {
                            viewModel.adicionarVeiculo(
                                clienteId, marca, modelo, matricula, ano, kms
                            )
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Criar",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewNovoVeiculo() {
    NovoVeiculoScreen(navController = rememberNavController(), clienteId = 1)
}
