package com.example.projetomecamoveis.view.cliente

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
import com.example.projetomecamoveis.model.VeiculoInfo
import com.example.projetomecamoveis.ui.theme.LexendFontFamily
import com.example.projetomecamoveis.viewmodel.VeiculoViewModel

@Composable
fun MeusVeiculosScreen(
    navController: NavHostController,
    clienteId: Int,
    viewModel: VeiculoViewModel = viewModel()
) {
    val veiculosState =
        viewModel.getVeiculosByCliente(clienteId).collectAsState(initial = emptyList())
    val veiculos = veiculosState.value

    var showMaxVehiclesDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var veiculoParaDeletar by remember { mutableStateOf<VeiculoInfo?>(null) }

    if (showMaxVehiclesDialog) {
        AlertDialog(
            onDismissRequest = { showMaxVehiclesDialog = false },
            title = { Text(text = "Aviso", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Você já tem o máximo de veículos associados.",
                    color = Color.LightGray
                )
            },
            confirmButton = {
                TextButton(onClick = { showMaxVehiclesDialog = false }) {
                    Text(text = "OK", color = Color(0xFFFFBD49), fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF2A2A2A)
        )
    }

    if (showDeleteConfirmDialog && veiculoParaDeletar != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Text(
                    text = "Apagar Veículo",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Quer mesmo apagar o veículo ${veiculoParaDeletar?.marca} ${veiculoParaDeletar?.modelo}?",
                    color = Color.LightGray
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    veiculoParaDeletar?.let { viewModel.deletarVeiculo(it) }
                    showDeleteConfirmDialog = false
                    veiculoParaDeletar = null
                }) {
                    Text(text = "Sim", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteConfirmDialog = false
                    veiculoParaDeletar = null
                }) {
                    Text(text = "Não", color = Color.White)
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
                        text = "Os Meus ",
                        color = Color(0xFFFFBD49),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = LexendFontFamily
                    )

                    Text(
                        text = "Veículos",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = LexendFontFamily
                    )


                }

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color(0xFFFFBD49),
                    modifier = Modifier.size(45.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (veiculos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(25.dp))
                        .padding(24.dp)
                ) {
                    Text(
                        text = "De momento não tem nenhum veículo registado. Adicione um veículo para começar.",
                        color = Color.White,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                        lineHeight = 22.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    veiculos.forEach { veiculo ->
                        VeiculoItemCard(
                            veiculo = veiculo,
                            onEditClick = {
                                navController.navigate("editar_veiculo_cliente/${veiculo.id}")
                            },
                            onDeleteClick = {
                                veiculoParaDeletar = veiculo
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- BOTÃO ADICIONAR CONFORME IMAGEM ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .background(color = Color(0xFF333333), shape = RoundedCornerShape(35.dp))
                    .clickable {
                        if (veiculos.size >= 3) {
                            showMaxVehiclesDialog = true
                        } else {
                            navController.navigate("novo_veiculo/$clienteId")
                        }
                    }
                    .padding(vertical = 18.dp, horizontal = 28.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Quer adicionar mais veículos?",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Maximo 3 Veículos",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = "+",
                        color = Color(0xFFFFBD49),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraLight
                    )
                }
            }
        }
    }
}

@Composable
fun VeiculoItemCard(
    veiculo: VeiculoInfo,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
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
                    text = "${veiculo.marca} ${veiculo.modelo}",
                    color = Color(0xFFFFBD49),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Ícone de Edição
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFFFFBD49),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onEditClick() }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Ícone de Deletar
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Deletar",
                        tint = Color(0xFFFFBD49),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDeleteClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Círculo Laranja (Veículo)
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFBD49))
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Seção Matrícula
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Matrícula", color = Color.White, fontSize = 13.sp)
                        Text(
                            text = veiculo.matricula,
                            color = Color(0xFFFFBD49),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Linha Divisória
                    HorizontalDivider(
                        color = Color.DarkGray.copy(alpha = 0.5f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Seção Ano
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Ano", color = Color.White, fontSize = 13.sp)
                        Text(
                            text = veiculo.ano,
                            color = Color(0xFFFFBD49),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seção KM (Alinhada à esquerda)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "KM",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = veiculo.kms, color = Color(0xFFFFBD49), fontSize = 13.sp)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMeusVeiculosRefined() {
    MeusVeiculosScreen(navController = rememberNavController(), clienteId = 1)
}
