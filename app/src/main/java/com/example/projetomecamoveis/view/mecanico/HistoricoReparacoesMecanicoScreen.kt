package com.example.projetomecamoveis.view.mecanico

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
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
import com.example.projetomecamoveis.model.ReparacaoComCliente
import com.example.projetomecamoveis.model.ReparacaoInfo
import com.example.projetomecamoveis.viewmodel.ReparacaoViewModel

@Composable
fun HistoricoReparacoesMecanicoScreen(
    navController: NavHostController,
    viewModel: ReparacaoViewModel = viewModel()
) {
    val reparacoesState = viewModel.todasReparacoesConcluidas.collectAsState(initial = emptyList())
    val reparacoes = reparacoesState.value

    var reparacaoParaDeletar by remember { mutableStateOf<ReparacaoInfo?>(null) }

    if (reparacaoParaDeletar != null) {
        AlertDialog(
            onDismissRequest = { reparacaoParaDeletar = null },
            title = {
                Text(
                    text = "Eliminar Reparação",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Tem a certeza que deseja eliminar esta reparação do histórico? Esta ação não pode ser desfeita.",
                    color = Color.LightGray
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    reparacaoParaDeletar?.let { viewModel.deletarReparacao(it) }
                    reparacaoParaDeletar = null
                }) {
                    Text(text = "Eliminar", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { reparacaoParaDeletar = null }) {
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

                Text(
                    text = "Histórico de Oficinas",
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

            if (reparacoes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                        .padding(32.dp)
                ) {
                    Text(
                        text = "Ainda não existem reparações concluídas no sistema.",
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    reparacoes.forEach { item ->
                        ReparacaoMecanicoPremiumCard(
                            item = item,
                            onDeleteClick = { reparacaoParaDeletar = item.reparacao }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ReparacaoMecanicoPremiumCard(
    item: ReparacaoComCliente,
    onDeleteClick: () -> Unit
) {
    val r = item.reparacao
    var expandido by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Data e Hora acima do card
        Text(
            text = r.dataHora,
            color = Color.LightGray,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Título e Botão de Lixo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reparação ${r.marca}",
                        color = Color(0xFFFFA500),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color(0xFFFFA500)
                        )
                    }
                }

                // Campo Especial: Cliente
                HistoricoMecanicoLinha(label = "Cliente", valor = item.clienteNome)
                HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                // Campos Comuns
                HistoricoMecanicoLinha(label = "Marca", valor = r.marca)
                HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                HistoricoMecanicoLinha(label = "Modelo", valor = r.modelo)
                HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                HistoricoMecanicoLinha(label = "Matrícula", valor = r.matricula)

                if (expandido) {
                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    HistoricoMecanicoLinha(label = "Ano", valor = r.ano)

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                    Column {
                        Text(text = "Peças Utilizadas", color = Color.White, fontSize = 13.sp)
                        Text(text = r.pecasUtilizadas, color = Color(0xFFFFA500), fontSize = 13.sp)
                    }

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    HistoricoMecanicoLinha(label = "Reparação nº", valor = r.numeroReparacao)

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    HistoricoMecanicoLinha(label = "Valor da Reparação", valor = r.valor)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botão Ver Mais/Menos
                TextButton(
                    onClick = { expandido = !expandido },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (expandido) "Ver Menos" else "Ver Mais",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun HistoricoMecanicoLinha(label: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.White, fontSize = 13.sp)
        Text(
            text = valor,
            color = Color(0xFFFFA500),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHistoricoMecanicoPremiumDelete() {
    HistoricoReparacoesMecanicoScreen(navController = rememberNavController())
}
