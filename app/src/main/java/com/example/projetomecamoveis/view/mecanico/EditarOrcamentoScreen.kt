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
import com.example.projetomecamoveis.model.ReparacaoInfo
import com.example.projetomecamoveis.ui.theme.LexendFontFamily
import com.example.projetomecamoveis.viewmodel.LoginClienteViewModel
import com.example.projetomecamoveis.viewmodel.OrcamentoViewModel

@Composable
fun EditarOrcamentoScreen(
    navController: NavHostController,
    orcamentoId: Int,
    viewModel: OrcamentoViewModel = viewModel(),
    clienteViewModel: LoginClienteViewModel = viewModel()
) {
    val todosClientes by clienteViewModel.todosClientes.collectAsState(initial = emptyList())
    val veiculosDoCliente by viewModel.veiculosDoCliente
    val addSucesso by viewModel.addSucesso
    val errorMessage by viewModel.errorMessage

    val orcamentoExistente by viewModel.getOrcamentoById(orcamentoId).collectAsState(initial = null)

    var clienteIdSelecionado by remember { mutableIntStateOf(0) }

    // Inicializar estados com os dados existentes quando carregarem
    var dataLoaded by remember { mutableStateOf(false) }
    var titulo by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var clienteLabel by remember { mutableStateOf("Carregando...") }
    var veiculoLabel by remember { mutableStateOf("Carregando...") }
    var veiculoIdSelecionado by remember { mutableIntStateOf(0) }
    var reparacaoLabel by remember { mutableStateOf("Carregando...") }
    var reparacaoIdSelecionado by remember { mutableStateOf<Int?>(null) }
    var reparacaoSelecionadaObj by remember { mutableStateOf<ReparacaoInfo?>(null) }

    var showClienteDialog by remember { mutableStateOf(false) }
    var showVeiculoDialog by remember { mutableStateOf(false) }
    var showReparacaoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orcamentoExistente) {
        if (orcamentoExistente != null && !dataLoaded) {
            titulo = orcamentoExistente!!.titulo
            valor = orcamentoExistente!!.valor
            clienteIdSelecionado = orcamentoExistente!!.clienteId
            veiculoIdSelecionado = orcamentoExistente!!.veiculoId
            reparacaoIdSelecionado = orcamentoExistente!!.reparacaoId

            viewModel.carregarVeiculos(clienteIdSelecionado)
            dataLoaded = true
        }
    }

    val reparacoesDoCliente by remember(clienteIdSelecionado) {
        viewModel.getReparacoesByCliente(clienteIdSelecionado)
    }.collectAsState(initial = emptyList())

    LaunchedEffect(addSucesso) {
        if (addSucesso) {
            viewModel.resetarSucesso()
            navController.popBackStack()
        }
    }

    // Atualizar labels e objetos quando os dados carregarem
    LaunchedEffect(todosClientes, clienteIdSelecionado) {
        todosClientes.find { it.id == clienteIdSelecionado }?.let { clienteLabel = it.email }
    }

    LaunchedEffect(veiculosDoCliente, veiculoIdSelecionado) {
        veiculosDoCliente.find { it.id == veiculoIdSelecionado }?.let {
            veiculoLabel = "${it.marca} ${it.modelo} (${it.matricula})"
        }
    }

    LaunchedEffect(reparacoesDoCliente, reparacaoIdSelecionado) {
        if (reparacaoIdSelecionado == null) {
            reparacaoLabel = "Não"
            reparacaoSelecionadaObj = null
        } else {
            reparacoesDoCliente.find { it.id == reparacaoIdSelecionado }?.let {
                reparacaoLabel = "${it.titulo} (${it.numeroReparacao})"
                reparacaoSelecionadaObj = it
            }
        }
    }

    // --- DIÁLOGOS (IGUAIS AO CRIAR) ---
    if (showClienteDialog) {
        AlertDialog(
            onDismissRequest = { showClienteDialog = false },
            title = {
                Text(
                    text = "Selecionar Cliente",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,

                    )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    todosClientes.forEach { cliente ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    clienteLabel = cliente.email
                                    clienteIdSelecionado = cliente.id
                                    viewModel.carregarVeiculos(cliente.id)
                                    veiculoLabel = "Ex: Carro do Cliente"
                                    reparacaoLabel = "Não"
                                    reparacaoIdSelecionado = null
                                    showClienteDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp)) {
                            Text(
                                text = cliente.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = cliente.email, color = Color(0xFFFFBD49), fontSize = 12.sp)
                        }
                        HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showClienteDialog = false }) {
                    Text(
                        "Fechar",
                        color = Color(0xFFFFBD49)
                    )
                }
            },
            containerColor = Color(0xFF2A2A2A)
        )
    }

    if (showVeiculoDialog) {
        AlertDialog(
            onDismissRequest = { showVeiculoDialog = false },
            title = {
                Text(
                    text = "Selecionar Carro",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    veiculosDoCliente.forEach { veiculo ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    veiculoIdSelecionado = veiculo.id
                                    veiculoLabel =
                                        "${veiculo.marca} ${veiculo.modelo} (${veiculo.matricula})"
                                    showVeiculoDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp)) {
                            Text(
                                text = "${veiculo.marca} ${veiculo.modelo}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = veiculo.matricula,
                                color = Color(0xFFFFBD49),
                                fontSize = 12.sp
                            )
                        }
                        HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showVeiculoDialog = false }) {
                    Text(
                        "Fechar",
                        color = Color(0xFFFFBD49)
                    )
                }
            },
            containerColor = Color(0xFF2A2A2A)
        )
    }

    if (showReparacaoDialog) {
        AlertDialog(
            onDismissRequest = { showReparacaoDialog = false },
            title = {
                Text(
                    text = "Selecionar Reparação",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                reparacaoIdSelecionado = null
                                reparacaoLabel = "Não"
                                showReparacaoDialog = false
                            }
                            .padding(vertical = 16.dp, horizontal = 8.dp)) {
                        Text(
                            text = "Não associar a reparação",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider(color = Color.DarkGray)
                    reparacoesDoCliente.forEach { reparacao ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    reparacaoIdSelecionado = reparacao.id
                                    reparacaoLabel =
                                        "${reparacao.titulo} (${reparacao.numeroReparacao})"
                                    showReparacaoDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp)) {
                            Text(
                                text = reparacao.titulo,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Estado: ${reparacao.estado} | nº ${reparacao.numeroReparacao}",
                                color = Color(0xFFFFBD49),
                                fontSize = 12.sp
                            )
                        }
                        HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showReparacaoDialog = false }) {
                    Text(
                        "Fechar",
                        color = Color(0xFFFFBD49)
                    )
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
                        text = "Editar ",
                        color = Color(0xFFFFBD49),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = LexendFontFamily
                    )

                    Text(
                        text = "Orçamento",
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

            // --- FORMULÁRIO (ESTÉTICA IGUAL AO CRIAR) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    EditarOrcamentoSeletorCampo(
                        label = "Orçamento associado ao Cliente",
                        valorMostrado = clienteLabel,
                        estaAtivo = true,
                        onClick = { showClienteDialog = true }
                    )

                    EditarOrcamentoSeletorCampo(
                        label = "Carro do Cliente",
                        valorMostrado = veiculoLabel,
                        estaAtivo = clienteIdSelecionado != 0,
                        onClick = { if (clienteIdSelecionado != 0) showVeiculoDialog = true }
                    )

                    EditarOrcamentoCampoSimples(
                        label = "Título do Orçamento",
                        valor = titulo,
                        placeholder = "Ex: Revisão Geral",
                        onValueChange = { titulo = it }
                    )

                    EditarOrcamentoCampoSimples(
                        label = "Valor do Orçamento",
                        valor = valor,
                        placeholder = "Ex: 123€",
                        onValueChange = { valor = it }
                    )

                    EditarOrcamentoSeletorCampo(
                        label = "Reparação Associada",
                        valorMostrado = reparacaoLabel,
                        estaAtivo = clienteIdSelecionado != 0,
                        onClick = { if (clienteIdSelecionado != 0) showReparacaoDialog = true }
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = {
                            viewModel.atualizarOrcamento(
                                orcamentoId,
                                clienteIdSelecionado,
                                veiculoIdSelecionado,
                                reparacaoIdSelecionado,
                                titulo,
                                valor
                            )
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Atualizar",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = LexendFontFamily
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun EditarOrcamentoSeletorCampo(
    label: String,
    valorMostrado: String,
    estaAtivo: Boolean,
    onClick: () -> Unit
) {
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
                    color = if (!estaAtivo || valorMostrado.contains("Ex:") || valorMostrado == "Carregando...") Color.Gray else Color.White,
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
fun EditarOrcamentoCampoSimples(
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
                focusedBorderColor = Color(0xFFFFBD49),
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
fun PreviewEditarOrcamento() {
    EditarOrcamentoScreen(navController = rememberNavController(), orcamentoId = 1)
}
