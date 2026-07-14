package com.example.projetomecamoveis.view.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.ui.theme.LexendFontFamily
import com.example.projetomecamoveis.viewmodel.VeiculoViewModel
import com.example.projetomecamoveis.view.mecanico.CampoEdicao
import com.example.projetomecamoveis.util.MatriculaVisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

@Composable
fun EditarVeiculoClienteScreen(
    navController: NavHostController,
    veiculoId: Int,
    viewModel: VeiculoViewModel = viewModel()
) {
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var kms by remember { mutableStateOf("") }
    var clienteId by remember { mutableIntStateOf(0) }

    val veiculoOriginal by viewModel.getVeiculoById(veiculoId).collectAsState(initial = null)
    val addSucesso by viewModel.addSucesso
    val errorMessage by viewModel.errorMessage
    val matriculaError by viewModel.matriculaError
    val anoError by viewModel.anoError

    var showConfirmDialog by remember { mutableStateOf(false) }

    // Carregar dados originais
    LaunchedEffect(veiculoOriginal) {
        veiculoOriginal?.let { v ->
            marca = v.marca
            modelo = v.modelo
            // Remove os traços ao carregar para o estado interno
            matricula = v.matricula.replace("-", "")
            ano = v.ano
            kms = v.kms
            clienteId = v.clienteId
        }
    }

    LaunchedEffect(addSucesso) {
        if (addSucesso) {
            viewModel.resetarSucesso()
            navController.popBackStack()
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "Confirmar Atualização",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Tem a certeza que quer atualizar o carro?",
                    color = Color.LightGray
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.editarVeiculo(
                        veiculoId, clienteId, marca, modelo, matricula, ano, kms
                    )
                    showConfirmDialog = false
                }) {
                    Text(text = "Sim", color = Color(0xFFFFBD49), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
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
                        color = Color(0xFFFFBD49),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = LexendFontFamily
                    )

                    Text(
                        text = "Veículo",
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
                        placeholder = "Ex: AA00AA",
                        visualTransformation = MatriculaVisualTransformation(),
                        onValueChange = { input ->
                            val cleanInput = input.toUpperCase(Locale.current).filter { it.isLetterOrDigit() }
                            if (cleanInput.length <= 6) {
                                matricula = cleanInput
                            }
                        },
                        mensagemErro = matriculaError)
                    CampoEdicao(
                        label = "Ano",
                        valor = ano,
                        placeholder = "Ex: 2012",
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 4) {
                                ano = input
                            }
                        },
                        mensagemErro = anoError)
                    CampoEdicao(
                        label = "Contagem de KM",
                        valor = kms,
                        placeholder = "Ex: 1,000 km",
                        onValueChange = { input ->
                            val digitsOnly = input.filter { it.isDigit() }
                            if (digitsOnly.isNotEmpty()) {
                                val number = digitsOnly.toLong()
                                val symbols = DecimalFormatSymbols(java.util.Locale.US)
                                symbols.groupingSeparator = ','
                                val formatter = DecimalFormat("#,###", symbols)
                                kms = formatter.format(number)
                            } else {
                                kms = ""
                            }
                        })

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
                            if (viewModel.validarDadosVeiculo(marca, modelo, matricula, ano, kms)) {
                                showConfirmDialog = true
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Guardar",
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

@Preview(showSystemUi = true)
@Composable
fun PreviewEditarVeiculoCliente() {
    EditarVeiculoClienteScreen(navController = rememberNavController(), veiculoId = 1)
}
