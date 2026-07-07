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
import com.example.projetomecamoveis.model.VeiculoInfo
import com.example.projetomecamoveis.model.ReparacaoInfo
import com.example.projetomecamoveis.viewmodel.LoginClienteViewModel
import com.example.projetomecamoveis.viewmodel.OrcamentoViewModel

@Composable
fun NovoOrcamentoScreen(
    navController: NavHostController,
    viewModel: OrcamentoViewModel = viewModel(),
    clienteViewModel: LoginClienteViewModel = viewModel()
) {
    val todosClientes by clienteViewModel.todosClientes.collectAsState(initial = emptyList())
    val veiculosDoCliente by viewModel.veiculosDoCliente
    val addSucesso by viewModel.addSucesso
    val errorMessage by viewModel.errorMessage

    // Estado centralizado para o ID do cliente selecionado
    var clienteIdSelecionado by remember { mutableIntStateOf(0) }

    // Buscar reparações ativas sempre que o cliente mudar
    val reparacoesDoCliente by remember(clienteIdSelecionado) {
        viewModel.getReparacoesByCliente(clienteIdSelecionado)
    }.collectAsState(initial = emptyList())

    LaunchedEffect(addSucesso) {
        if (addSucesso) {
            viewModel.resetarSucesso()
            navController.popBackStack()
        }
    }

    NovoOrcamentoContent(
        navController = navController,
        todosClientes = todosClientes,
        veiculosDoCliente = veiculosDoCliente,
        reparacoesDoCliente = reparacoesDoCliente,
        errorMessage = errorMessage,
        clienteIdSelecionadoGlobal = clienteIdSelecionado,
        onClienteSelecionado = { id -> 
            clienteIdSelecionado = id
            viewModel.carregarVeiculos(id)
        },
        onCriarClick = { idCliente, veiculo, reparacao, titulo, valor ->
            viewModel.adicionarOrcamento(idCliente, veiculo, reparacao, titulo, valor)
        }
    )
}

@Composable
fun NovoOrcamentoContent(
    navController: NavHostController,
    todosClientes: List<com.example.projetomecamoveis.model.LoginClienteInfo>,
    veiculosDoCliente: List<VeiculoInfo>,
    reparacoesDoCliente: List<ReparacaoInfo>,
    errorMessage: String?,
    clienteIdSelecionadoGlobal: Int,
    onClienteSelecionado: (Int) -> Unit,
    onCriarClick: (Int, VeiculoInfo?, ReparacaoInfo?, String, String) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    
    // Labels para os campos (mantêm o texto do item selecionado)
    var clienteLabel by remember { mutableStateOf("Ex: Email do Cliente") }
    var showClienteDialog by remember { mutableStateOf(false) }

    var veiculoLabel by remember { mutableStateOf("Ex: Carro do Cliente") }
    var veiculoSelecionadoObj by remember { mutableStateOf<VeiculoInfo?>(null) }
    var showVeiculoDialog by remember { mutableStateOf(false) }

    var reparacaoLabel by remember { mutableStateOf("Ex: Selecione a Reparação") }
    var reparacaoSelecionadaObj by remember { mutableStateOf<ReparacaoInfo?>(null) }
    var showReparacaoDialog by remember { mutableStateOf(false) }

    // --- DIÁLOGO SELECIONAR CLIENTE ---
    if (showClienteDialog) {
        AlertDialog(
            onDismissRequest = { showClienteDialog = false },
            title = { Text(text = "Selecionar Cliente", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (todosClientes.isEmpty()) {
                        Text(text = "Nenhum cliente registado no sistema.", color = Color.Gray, modifier = Modifier.padding(16.dp))
                    } else {
                        todosClientes.forEach { cliente ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        clienteLabel = cliente.email
                                        onClienteSelecionado(cliente.id)
                                        // Resetar seleções dependentes
                                        veiculoLabel = "Ex: Carro do Cliente"
                                        veiculoSelecionadoObj = null
                                        reparacaoLabel = "Ex: Selecione a Reparação"
                                        reparacaoSelecionadaObj = null
                                        showClienteDialog = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 8.dp)
                            ) {
                                Text(text = cliente.name, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(text = cliente.email, color = Color(0xFFFFA500), fontSize = 12.sp)
                            }
                            HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f))
                        }
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

    // --- DIÁLOGO SELECIONAR CARRO ---
    if (showVeiculoDialog) {
        AlertDialog(
            onDismissRequest = { showVeiculoDialog = false },
            title = { Text(text = "Selecionar Carro", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (veiculosDoCliente.isEmpty()) {
                        Text(text = "Este cliente não tem veículos registados.", color = Color.Gray, modifier = Modifier.padding(16.dp))
                    } else {
                        veiculosDoCliente.forEach { veiculo ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        veiculoSelecionadoObj = veiculo
                                        veiculoLabel = "${veiculo.marca} ${veiculo.modelo} (${veiculo.matricula})"
                                        showVeiculoDialog = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 8.dp)
                            ) {
                                Text(text = "${veiculo.marca} ${veiculo.modelo}", color = Color.White, fontWeight = FontWeight.Bold)
                                Text(text = veiculo.matricula, color = Color(0xFFFFA500), fontSize = 12.sp)
                            }
                            HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f))
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

    // --- DIÁLOGO SELECIONAR REPARAÇÃO ---
    if (showReparacaoDialog) {
        AlertDialog(
            onDismissRequest = { showReparacaoDialog = false },
            title = { Text(text = "Selecionar Reparação", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Opção "Não"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                reparacaoSelecionadaObj = null
                                reparacaoLabel = "Não"
                                showReparacaoDialog = false
                            }
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                    ) {
                        Text(text = "Não associar a reparação", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    HorizontalDivider(color = Color.DarkGray)

                    if (reparacoesDoCliente.isEmpty()) {
                        Text(text = "Sem reparações em curso para este cliente.", color = Color.Gray, modifier = Modifier.padding(16.dp))
                    } else {
                        reparacoesDoCliente.forEach { reparacao ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        reparacaoSelecionadaObj = reparacao
                                        reparacaoLabel = "${reparacao.titulo} (${reparacao.numeroReparacao})"
                                        showReparacaoDialog = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 8.dp)
                            ) {
                                Text(text = reparacao.titulo, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(text = "Estado: ${reparacao.estado} | nº ${reparacao.numeroReparacao}", color = Color(0xFFFFA500), fontSize = 12.sp)
                            }
                            HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showReparacaoDialog = false }) {
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

                Row {

                    Text(
                        text = "Novo ",
                        color = Color(0xFFFFA500),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Orçamento",
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
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    
                    // CAMPO CLIENTE
                    OrcamentoSeletorCampo(
                        label = "Orçamento associado ao Cliente",
                        valorMostrado = clienteLabel,
                        estaAtivo = true,
                        onClick = { showClienteDialog = true }
                    )

                    // CAMPO VEÍCULO
                    OrcamentoSeletorCampo(
                        label = "Carro do Cliente",
                        valorMostrado = veiculoLabel,
                        estaAtivo = clienteIdSelecionadoGlobal != 0,
                        onClick = { if (clienteIdSelecionadoGlobal != 0) showVeiculoDialog = true }
                    )

                    // CAMPO TÍTULO
                    OrcamentoCampoSimples(
                        label = "Título do Orçamento",
                        valor = titulo,
                        placeholder = "Ex: Revisão Geral",
                        onValueChange = { titulo = it }
                    )

                    // CAMPO VALOR
                    OrcamentoCampoSimples(
                        label = "Valor do Orçamento",
                        valor = valor,
                        placeholder = "Ex: 123€",
                        onValueChange = { valor = it }
                    )

                    // CAMPO REPARAÇÃO
                    OrcamentoSeletorCampo(
                        label = "Reparação Associada",
                        valorMostrado = reparacaoLabel,
                        estaAtivo = clienteIdSelecionadoGlobal != 0,
                        onClick = { if (clienteIdSelecionadoGlobal != 0) showReparacaoDialog = true }
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = {
                            onCriarClick(clienteIdSelecionadoGlobal, veiculoSelecionadoObj, reparacaoSelecionadaObj, titulo, valor)
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
fun PreviewNovoOrcamento() {
    NovoOrcamentoContent(
        navController = rememberNavController(),
        todosClientes = emptyList(),
        veiculosDoCliente = emptyList(),
        reparacoesDoCliente = emptyList(),
        errorMessage = null,
        clienteIdSelecionadoGlobal = 0,
        onClienteSelecionado = {},
        onCriarClick = { _, _, _, _, _ -> }
    )
}

@Composable
fun OrcamentoSeletorCampo(label: String, valorMostrado: String, estaAtivo: Boolean, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
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
                    color = if (estaAtivo) Color(0xFF3D3D3D) else Color(0xFF2A2A2A),
                    shape = RoundedCornerShape(28.dp)
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
fun OrcamentoCampoSimples(label: String, valor: String, placeholder: String, onValueChange: (String) -> Unit) {
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
