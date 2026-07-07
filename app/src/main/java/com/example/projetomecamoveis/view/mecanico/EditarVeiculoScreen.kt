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
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import com.example.projetomecamoveis.viewmodel.LoginClienteViewModel
import com.example.projetomecamoveis.viewmodel.VeiculoViewModel

@Composable
fun EditarVeiculoScreen(
    navController: NavHostController,
    veiculoId: Int,
    viewModel: VeiculoViewModel = viewModel(),
    clienteViewModel: LoginClienteViewModel = viewModel()
) {
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var kms by remember { mutableStateOf("") }

    var clienteSelecionadoNome by remember { mutableStateOf("Ex: Nome Sobrenome") }
    var clienteIdSelecionado by remember { mutableIntStateOf(0) }
    var showClienteDialog by remember { mutableStateOf(false) }

    val veiculoOriginal by viewModel.getVeiculoById(veiculoId).collectAsState(initial = null)
    val todosClientes by clienteViewModel.todosClientes.collectAsState(initial = emptyList())
    val addSucesso by viewModel.addSucesso
    val errorMessage by viewModel.errorMessage

    // Carregar dados originais
    LaunchedEffect(veiculoOriginal) {
        veiculoOriginal?.let { v ->
            marca = v.marca
            modelo = v.modelo
            matricula = v.matricula
            ano = v.ano
            kms = v.kms
            clienteIdSelecionado = v.clienteId

            // Tentar encontrar o nome do cliente na lista
            val dono = todosClientes.find { it.id == v.clienteId }
            if (dono != null) {
                clienteSelecionadoNome = dono.name
            }
        }
    }

    // Atualizar nome do cliente quando a lista carregar (se necessário)
    LaunchedEffect(todosClientes) {
        if (clienteIdSelecionado != 0 && clienteSelecionadoNome == "Ex: Nome Sobrenome") {
            todosClientes.find { it.id == clienteIdSelecionado }?.let {
                clienteSelecionadoNome = it.name
            }
        }
    }

    LaunchedEffect(addSucesso) {
        if (addSucesso) {
            viewModel.resetarSucesso()
            navController.popBackStack()
        }
    }

    if (showClienteDialog) {
        AlertDialog(
            onDismissRequest = { showClienteDialog = false },
            title = { Text(text = "Selecionar Novo Proprietário", color = Color.White) },
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
                                    clienteSelecionadoNome = cliente.name
                                    showClienteDialog = false
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            Column {
                                Text(
                                    text = cliente.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
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
                        text = "Editar ",
                        color = Color(0xFFFFA500),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Veículo",
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

            // --- FORMULÁRIO ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    CampoEdicao(
                        label = "Marca",
                        valor = marca,
                        placeholder = "Ex: Volvo",
                        onValueChange = { marca = it })
                    CampoEdicao(
                        label = "Modelo",
                        valor = modelo,
                        placeholder = "Ex: V40",
                        onValueChange = { modelo = it })
                    CampoEdicao(
                        label = "Matrícula",
                        valor = matricula,
                        placeholder = "Ex: AA-00-AA",
                        onValueChange = { matricula = it })
                    CampoEdicao(
                        label = "Ano",
                        valor = ano,
                        placeholder = "Ex: 2012",
                        onValueChange = { ano = it })
                    CampoEdicao(
                        label = "Contagem de KM",
                        valor = kms,
                        placeholder = "Ex: 100,000 km",
                        onValueChange = { kms = it })

                    // Seleção de Cliente
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Reparação associada ao Cliente",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .background(
                                    color = Color(0xFF3D3D3D),
                                    shape = RoundedCornerShape(28.dp)
                                )
                                .clickable { showClienteDialog = true }
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = clienteSelecionadoNome,
                                    color = Color.White,
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
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = {
                            viewModel.editarVeiculo(
                                veiculoId, clienteIdSelecionado, marca, modelo, matricula, ano, kms
                            )
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Editar",
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
fun CampoEdicao(
    label: String,
    valor: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 14.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF3D3D3D),
                unfocusedContainerColor = Color(0xFF3D3D3D),
                focusedBorderColor = Color(0xFFFFA500),
                unfocusedBorderColor = Color(0xFF555454),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewEditarVeiculo() {
    EditarVeiculoScreen(navController = rememberNavController(), veiculoId = 1)
}
