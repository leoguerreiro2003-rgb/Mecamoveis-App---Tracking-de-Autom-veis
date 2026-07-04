package com.example.projetomecamoveis.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.example.projetomecamoveis.model.VeiculoInfo
import com.example.projetomecamoveis.viewmodel.LoginClienteViewModel
import com.example.projetomecamoveis.viewmodel.ReparacaoViewModel

@Composable
fun NovaReparacaoScreen(
    navController: NavHostController,
    viewModel: ReparacaoViewModel = viewModel(),
    clienteViewModel: LoginClienteViewModel = viewModel()
) {
    var pecas by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    
    var clienteEmailSelecionado by remember { mutableStateOf("Ex: Email do Cliente") }
    var clienteIdSelecionado by remember { mutableIntStateOf(0) }
    var showClienteDialog by remember { mutableStateOf(false) }

    var veiculoSelecionadoDesc by remember { mutableStateOf("Ex: Carro do Cliente") }
    var veiculoSelecionadoObj by remember { mutableStateOf<VeiculoInfo?>(null) }
    var showVeiculoDialog by remember { mutableStateOf(false) }

    var estadoSelecionado by remember { mutableStateOf("Ex: Selecione o Estado") }
    var showEstadoDialog by remember { mutableStateOf(false) }
    val listaEstados = listOf("Reparação Iniciada", "Em Reparação", "Revisão", "Reparação Concluída")

    val todosClientes by clienteViewModel.todosClientes.collectAsState(initial = emptyList())
    val veiculosDoCliente by viewModel.veiculosDoCliente
    val addSucesso by viewModel.addSucesso
    val errorMessage by viewModel.errorMessage

    LaunchedEffect(addSucesso) {
        if (addSucesso) {
            viewModel.resetarSucesso()
            navController.popBackStack()
        }
    }

    // Pop-up para Selecionar Cliente
    if (showClienteDialog) {
        AlertDialog(
            onDismissRequest = { showClienteDialog = false },
            title = { Text(text = "Selecionar Cliente", color = Color.White) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    todosClientes.forEach { cliente ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    clienteIdSelecionado = cliente.id
                                    clienteEmailSelecionado = cliente.email
                                    veiculoSelecionadoDesc = "Ex: Carro do Cliente"
                                    veiculoSelecionadoObj = null
                                    viewModel.carregarVeiculos(cliente.id)
                                    showClienteDialog = false
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            Column {
                                Text(text = cliente.name, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(text = cliente.email, color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                        HorizontalDivider(color = Color.DarkGray)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showClienteDialog = false }) {
                    Text("Fechar", color = Color(0xFFFFA500))
                }
            },
            containerColor = Color(0xFF2A2A2A)
        )
    }

    // Pop-up para Selecionar Veículo
    if (showVeiculoDialog) {
        AlertDialog(
            onDismissRequest = { showVeiculoDialog = false },
            title = { Text(text = "Selecionar Carro", color = Color.White) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (veiculosDoCliente.isEmpty()) {
                        Text(text = "Este cliente não tem carros registados.", color = Color.Gray, modifier = Modifier.padding(16.dp))
                    } else {
                        veiculosDoCliente.forEach { veiculo ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        veiculoSelecionadoObj = veiculo
                                        veiculoSelecionadoDesc = "${veiculo.marca} ${veiculo.modelo} (${veiculo.matricula})"
                                        showVeiculoDialog = false
                                    }
                                    .padding(vertical = 12.dp)
                            ) {
                                Column {
                                    Text(text = "${veiculo.marca} ${veiculo.modelo}", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text(text = veiculo.matricula, color = Color(0xFFFFA500), fontSize = 12.sp)
                                }
                            }
                            HorizontalDivider(color = Color.DarkGray)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showVeiculoDialog = false }) {
                    Text("Fechar", color = Color(0xFFFFA500))
                }
            },
            containerColor = Color(0xFF2A2A2A)
        )
    }

    // Pop-up para Selecionar Estado
    if (showEstadoDialog) {
        AlertDialog(
            onDismissRequest = { showEstadoDialog = false },
            title = { Text(text = "Estado da Reparação", color = Color.White) },
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
                    text = "Nova Reparação",
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

            // --- FORMULÁRIO ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    
                    SeletorCampo(
                        label = "Reparação associada ao Cliente",
                        valorMostrado = clienteEmailSelecionado,
                        estaAtivo = true,
                        onClick = { showClienteDialog = true }
                    )

                    SeletorCampo(
                        label = "Carro do Cliente",
                        valorMostrado = veiculoSelecionadoDesc,
                        estaAtivo = clienteIdSelecionado != 0,
                        onClick = { if (clienteIdSelecionado != 0) showVeiculoDialog = true }
                    )

                    CampoSimples(label = "Valor da Reparação", valor = valor, placeholder = "Ex: 123€", onValueChange = { valor = it })

                    CampoSimples(label = "Peças Utilizadas", valor = pecas, placeholder = "Ex: Farol Esquerdo", onValueChange = { pecas = it })

                    // NOVO: Seleção de Estado
                    SeletorCampo(
                        label = "Estado da Reparação",
                        valorMostrado = estadoSelecionado,
                        estaAtivo = true,
                        onClick = { showEstadoDialog = true }
                    )

                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = Color.Red, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = {
                            viewModel.adicionarReparacao(
                                clienteIdSelecionado, veiculoSelecionadoObj, pecas, valor, estadoSelecionado
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

@Composable
fun SeletorCampo(label: String, valorMostrado: String, estaAtivo: Boolean, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = if (estaAtivo) Color(0xFF3D3D3D) else Color(0xFF2A2A2A), 
                    shape = RoundedCornerShape(25.dp)
                )
                .then(if (estaAtivo) Modifier.clickable { onClick() } else Modifier)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = valorMostrado,
                    color = if (!estaAtivo || valorMostrado.startsWith("Ex:")) Color.Gray else Color.White,
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
}

@Composable
fun CampoSimples(label: String, valor: String, placeholder: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3D3D3D),
                unfocusedContainerColor = Color(0xFF3D3D3D),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewNovaReparacaoStatus() {
    NovaReparacaoScreen(navController = rememberNavController())
}
