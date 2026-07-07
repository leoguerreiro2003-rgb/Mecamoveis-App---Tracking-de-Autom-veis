package com.example.projetomecamoveis.view.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.viewmodel.VeiculoViewModel

@Composable
fun HomeClienteScreen(
    navController: NavHostController,
    clienteId: Int,
    clienteNome: String,
    viewModel: VeiculoViewModel = viewModel()
) {
    val primeiroNome = clienteNome.trim().split(" ").firstOrNull() ?: clienteNome

    var showStatusDialog by remember { mutableStateOf(false) }
    val veiculosEmReparacaoState =
        viewModel.getVeiculosEmReparacao(clienteId).collectAsState(initial = emptyList())
    val veiculosEmReparacao = veiculosEmReparacaoState.value

    if (showStatusDialog) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = {
                Text(
                    text = "Estado da Reparação",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (veiculosEmReparacao.isEmpty()) {
                        Text(
                            text = "Não tem veículos em reparação no momento.",
                            color = Color.LightGray
                        )
                    } else {
                        veiculosEmReparacao.forEach { veiculo ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF3D3D3D), RoundedCornerShape(12.dp))
                                    .clickable {
                                        showStatusDialog = false
                                        navController.navigate("detalhes_estado_reparacao/${veiculo.id}/$primeiroNome")
                                    }
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${veiculo.marca} ${veiculo.modelo}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = veiculo.matricula,
                                        color = Color(0xFFFFA500),
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = veiculo.ultimoEstado,
                                    color = Color(0xFFFFA500),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text(text = "Fechar", color = Color(0xFFFFA500))
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
            horizontalAlignment = Alignment.CenterHorizontally,

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
                    Text(text = "Olá, ", color = Color(0xFFFFA500), fontSize = 18.sp)
                    Text(
                        text = "$primeiroNome!",
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

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.logotipoprototipo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- BOTÕES DE MENU ---
            HomeClienteMenuButton(text = "Os Meus Veículos") { navController.navigate("meus_veiculos/$clienteId") }

            Spacer(modifier = Modifier.height(32.dp))

            HomeClienteMenuButton(text = "Estado Reparação") { showStatusDialog = true }

            Spacer(modifier = Modifier.height(32.dp))

            HomeClienteMenuButton(text = "Histórico De Orçamentos") { navController.navigate("historico_orcamentos/$clienteId") }

            Spacer(modifier = Modifier.height(32.dp))

            HomeClienteMenuButton(text = "Histórico de Reparações") {
                android.util.Log.d("HomeCliente", "Navegando para histórico do cliente: $clienteId")
                navController.navigate("historico_reparacoes/$clienteId")
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun HomeClienteMenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}



@Preview(showSystemUi = true)
@Composable
fun PreviewHomeCliente() {
    HomeClienteScreen(
        navController = rememberNavController(),
        clienteId = 1,
        clienteNome = "Leandro"
    )
}
