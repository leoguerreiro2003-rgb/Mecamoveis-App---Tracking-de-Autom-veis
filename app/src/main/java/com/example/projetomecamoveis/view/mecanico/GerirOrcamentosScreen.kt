package com.example.projetomecamoveis.view.mecanico

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.ui.theme.LexendFontFamily
import com.example.projetomecamoveis.viewmodel.LoginClienteViewModel
import com.example.projetomecamoveis.viewmodel.OrcamentoViewModel

@Composable
fun GerirOrcamentosScreen(
    navController: NavHostController,
    clienteViewModel: LoginClienteViewModel = viewModel(),
    orcamentoViewModel: OrcamentoViewModel = viewModel()
) {
    var showClienteDialog by remember { mutableStateOf(false) }
    var showEditarDialog by remember { mutableStateOf(false) }

    val todosClientes by clienteViewModel.todosClientes.collectAsState(initial = emptyList())
    val todosOrcamentos by orcamentoViewModel.todosOrcamentosDetalhados.collectAsState(initial = emptyList())

    // Diálogo Histórico (Selecionar Cliente)
    if (showClienteDialog) {
        AlertDialog(
            onDismissRequest = { showClienteDialog = false },
            title = {
                Text(
                    text = "Histórico de Orçamentos",
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
                    if (todosClientes.isEmpty()) {
                        Text(text = "Não existem clientes registados.", color = Color.Gray)
                    } else {
                        todosClientes.forEach { cliente ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showClienteDialog = false
                                        navController.navigate("historico_orcamentos/${cliente.id}")
                                    }
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = cliente.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = cliente.email,
                                    color = Color(0xFFFFBD49),
                                    fontSize = 13.sp
                                )
                            }
                            HorizontalDivider(color = Color.DarkGray)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showClienteDialog = false }) {
                    Text("Fechar", color = Color(0xFFFFBD49))
                }
            },
            containerColor = Color(0xFF2A2A2A)
        )
    }

    // Diálogo Editar (Selecionar Orçamento)
    if (showEditarDialog) {
        AlertDialog(
            onDismissRequest = { showEditarDialog = false },
            title = {
                Text(
                    text = "Escolha o Orçamento para Editar",
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
                    if (todosOrcamentos.isEmpty()) {
                        Text(text = "Não existem orçamentos registados.", color = Color.Gray)
                    } else {
                        todosOrcamentos.forEach { item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showEditarDialog = false
                                        navController.navigate("editar_orcamento/${item.orcamento.id}")
                                    }
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = item.orcamento.titulo,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Carro: ${item.marca} ${item.modelo}",
                                    color = Color(0xFFFFBD49),
                                    fontSize = 13.sp
                                )
                            }
                            HorizontalDivider(color = Color.DarkGray)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showEditarDialog = false }) {
                    Text("Fechar", color = Color(0xFFFFBD49))
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
                        color = Color(0xFFFFBD49),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = LexendFontFamily
                    )

                    Text(
                        text = "Orçamentos",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = LexendFontFamily
                    )
                }

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFBD49))
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // --- BOTÕES DE OPÇÃO ---
            GerirOrcamentosOptionButton(
                text = "Criar Novo Orçamento",
                icon = Icons.Default.Calculate,
                onClick = { navController.navigate("novo_orcamento") }
            )

            Spacer(modifier = Modifier.height(30.dp))

            GerirOrcamentosOptionButton(
                text = "Histórico de Orçamentos",
                icon = Icons.Default.History,
                onClick = { showClienteDialog = true }
            )

            Spacer(modifier = Modifier.height(30.dp))

            GerirOrcamentosOptionButton(
                text = "Editar Orçamentos",
                icon = Icons.Default.EditNote,
                onClick = { showEditarDialog = true }
            )
        }
    }
}

@Composable
fun GerirOrcamentosOptionButton(
    text: String,
    icon: ImageVector,
    showPlus: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBD49))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
                if (showPlus) {
                    Box(
                        modifier = Modifier
                            .offset(x = 10.dp, y = (-10).dp)
                    ) {
                        Text(
                            text = "+",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = text,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = LexendFontFamily
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewGerirOrcamentos() {
    GerirOrcamentosScreen(navController = rememberNavController())
}
