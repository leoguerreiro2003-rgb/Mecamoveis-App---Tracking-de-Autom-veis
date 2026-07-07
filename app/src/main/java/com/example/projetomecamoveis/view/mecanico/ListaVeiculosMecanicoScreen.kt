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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.model.VeiculoComCliente
import com.example.projetomecamoveis.model.VeiculoInfo
import com.example.projetomecamoveis.viewmodel.VeiculoViewModel

@Composable
fun ListaVeiculosMecanicoScreen(
    navController: NavHostController,
    viewModel: VeiculoViewModel = viewModel()
) {
    val veiculos by viewModel.todosVeiculosComCliente.collectAsState(initial = emptyList())

    var veiculoParaDeletar by remember { mutableStateOf<VeiculoInfo?>(null) }

    if (veiculoParaDeletar != null) {
        AlertDialog(
            onDismissRequest = { veiculoParaDeletar = null },
            title = {
                Text(
                    text = "Eliminar Veículo",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Tem a certeza que deseja eliminar este veículo? Esta ação apagará todos os dados associados a ele permanentemente.",
                    color = Color.LightGray
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    veiculoParaDeletar?.let { viewModel.deletarVeiculo(it) }
                    veiculoParaDeletar = null
                }) {
                    Text(text = "Eliminar", color = Color(0xFFFFA500), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { veiculoParaDeletar = null }) {
                    Text(text = "Cancelar", color = Color.White)
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
                        text = "Ver ",
                        color = Color(0xFFFFA500),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Veículos",
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

            Spacer(modifier = Modifier.height(30.dp))

            if (veiculos.isEmpty()) {
                // ESTADO VAZIO (Imagem 1)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF2A2A2A),
                                shape = RoundedCornerShape(35.dp)
                            )
                            .padding(32.dp)
                    ) {
                        Text(
                            text = "De momento não tem nenhum veículo registado. Adicione um Veículo para começar.",
                            color = Color.White,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = { navController.navigate("novo_veiculo_mecanico") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        shape = RoundedCornerShape(35.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "+", color = Color.Black, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Adicionar",
                                color = Color.Black,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                // LISTA DE VEÍCULOS (Imagem 2)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    veiculos.forEach { item ->
                        VeiculoMecanicoCard(
                            item = item,
                            onEditClick = { navController.navigate("editar_veiculo/${item.veiculo.id}") },
                            onDeleteClick = { veiculoParaDeletar = item.veiculo }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun VeiculoMecanicoCard(
    item: VeiculoComCliente,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val v = item.veiculo
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
            .padding(24.dp)
    ) {
        Column {
            // Título e Ícones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${v.marca} ${v.modelo}",
                    color = Color(0xFFFFA500),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFFFFA500),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { onEditClick() }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFFFA500),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { onDeleteClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Círculo Laranja
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFA500))
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Proprietário
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Proprietário", color = Color.White, fontSize = 13.sp)
                        Text(text = item.clienteNome, color = Color(0xFFFFA500), fontSize = 13.sp)
                    }
                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                    // Matrícula
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Matrícula", color = Color.White, fontSize = 13.sp)
                        Text(
                            text = v.matricula,
                            color = Color(0xFFFFA500),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KM e Ano
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "KM",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = v.kms, color = Color(0xFFFFA500), fontSize = 13.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ano", color = Color.White, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = v.ano,
                        color = Color(0xFFFFA500),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewListaVeiculosMecanicoFull() {
    ListaVeiculosMecanicoScreen(navController = rememberNavController())
}
