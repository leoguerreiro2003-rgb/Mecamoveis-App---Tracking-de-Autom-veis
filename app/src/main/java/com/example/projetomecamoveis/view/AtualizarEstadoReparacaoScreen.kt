package com.example.projetomecamoveis.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.viewmodel.ReparacaoViewModel

@Composable
fun AtualizarEstadoReparacaoScreen(
    navController: NavHostController,
    veiculoId: Int,
    viewModel: ReparacaoViewModel = viewModel()
) {
    var estadoSelecionado by remember { mutableStateOf("Ex: O Estado de Reparação atual") }
    var showEstadoDialog by remember { mutableStateOf(false) }
    val listaEstados = listOf("Reparação Iniciada", "Em Reparação", "Revisão", "Reparação Concluída")

    val addSucesso by viewModel.addSucesso
    val errorMessage by viewModel.errorMessage

    LaunchedEffect(addSucesso) {
        if (addSucesso) {
            viewModel.resetarSucesso()
            navController.popBackStack()
        }
    }

    if (showEstadoDialog) {
        AlertDialog(
            onDismissRequest = { showEstadoDialog = false },
            title = { Text(text = "Selecionar Novo Estado", color = Color.White) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    listaEstados.forEach { status ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    estadoSelecionado = status
                                    showEstadoDialog = false
                                }
                                .padding(vertical = 16.dp)
                        ) {
                            Text(text = status, color = Color.White, fontSize = 15.sp)
                        }
                        HorizontalDivider(color = Color.DarkGray)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showEstadoDialog = false }) {
                    Text("Fechar", color = Color(0xFFFFA500))
                }
            },
            containerColor = Color(0xFF2A2A2A)
        )
    }

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
                    text = "Atualizar Reparação",
                    color = Color(0xFFFFA500),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFA500))
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- CARD DE ATUALIZAÇÃO ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    
                    // Seletor de Estado
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Estado da Reparação", 
                            color = Color.White, 
                            fontSize = 14.sp, 
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(color = Color(0xFF3D3D3D), shape = RoundedCornerShape(25.dp))
                                .clickable { showEstadoDialog = true }
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = estadoSelecionado,
                                    color = if (estadoSelecionado.startsWith("Ex:")) Color.Gray else Color.White,
                                    fontSize = 14.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = Color.Red, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                    TextButton(
                        onClick = {
                            viewModel.atualizarEstadoReparacaoExistente(veiculoId, estadoSelecionado)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Atualizar",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAtualizarEstado() {
    AtualizarEstadoReparacaoScreen(navController = rememberNavController(), veiculoId = 1)
}
