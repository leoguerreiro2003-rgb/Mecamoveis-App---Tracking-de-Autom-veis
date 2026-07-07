package com.example.projetomecamoveis.view.mecanico

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.History
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
import com.example.projetomecamoveis.viewmodel.VeiculoViewModel

@Composable
fun GerirReparacoesScreen(
    navController: NavHostController,
    viewModel: VeiculoViewModel = viewModel()
) {
    var showAtualizarDialog by remember { mutableStateOf(false) }
    val veiculosEmReparacao by viewModel.veiculosEmReparacaoComCliente.collectAsState(initial = emptyList())

    if (showAtualizarDialog) {
        AlertDialog(
            onDismissRequest = { showAtualizarDialog = false },
            title = {
                Text(
                    text = "Escolha o Veículo para Atualizar",
                    color = Color.White,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (veiculosEmReparacao.isEmpty()) {
                        Text(text = "Não existem veículos em reparação.", color = Color.Gray)
                    } else {
                        veiculosEmReparacao.forEach { item ->
                            val v = item.veiculo
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showAtualizarDialog = false
                                        // Navegar para o ecrã de atualização de estado específica
                                        navController.navigate("atualizar_estado_reparacao/${v.id}")
                                    }
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "${v.marca} ${v.modelo} - ${v.matricula}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Cliente: ${item.clienteNome}",
                                    color = Color(0xFFFFA500),
                                    fontSize = 13.sp
                                )
                            }
                            HorizontalDivider(color = Color.DarkGray)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAtualizarDialog = false }) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
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

                Row {
                    Text(
                        text = "Gerir ",
                        color = Color(0xFFFFA500),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Reparações",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color(0xFFFFA500),
                    modifier = Modifier.size(45.dp)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // --- BOTÕES DE OPÇÃO ---
            GerirReparacoesOptionButton(
                text = "Criar Nova Reparação",
                icon = Icons.Default.Build,
                onClick = { navController.navigate("nova_reparacao") }
            )

            Spacer(modifier = Modifier.height(30.dp))

            GerirReparacoesOptionButton(
                text = "Histórico de reparações",
                icon = Icons.Default.History,
                onClick = { navController.navigate("historico_reparacoes_mecanico") }
            )

            Spacer(modifier = Modifier.height(30.dp))

            GerirReparacoesOptionButton(
                text = "Atualizar Reparações",
                icon = Icons.Default.EditNote,
                onClick = { showAtualizarDialog = true }
            )
        }
    }
}

@Composable
fun GerirReparacoesOptionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = text,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewGerirReparacoesRefined() {
    GerirReparacoesScreen(navController = rememberNavController())
}
