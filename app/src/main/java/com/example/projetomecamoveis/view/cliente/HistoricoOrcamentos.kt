package com.example.projetomecamoveis.view.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.model.OrcamentoDetalhado
import com.example.projetomecamoveis.viewmodel.OrcamentoViewModel

@Composable
fun HistoricoOrcamentosScreen(
    navController: NavHostController,
    clienteId: Int,
    viewModel: OrcamentoViewModel = viewModel()
) {
    val orcamentosState =
        viewModel.getOrcamentosDetalhadosByCliente(clienteId).collectAsState(initial = emptyList())
    val orcamentos = orcamentosState.value

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
                        text = "Histórico",
                        color = Color(0xFFFFA500),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " Orçamentos",
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

            if (orcamentos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                        .padding(32.dp)
                ) {
                    Text(
                        text = "De momento não tem nenhum Orçamento registado.",
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
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    orcamentos.forEach { orcamento ->
                        OrcamentoCard(orcamento = orcamento)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun OrcamentoCard(orcamento: OrcamentoDetalhado) {
    var expandido by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = orcamento.orcamento.titulo,
                        color = Color(0xFFFFA500),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Orçamento Nº ${orcamento.orcamento.id}",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinhaInfoOrcamento(label = "Marca", valor = orcamento.marca)
                HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                LinhaInfoOrcamento(label = "Modelo", valor = orcamento.modelo)
                HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                LinhaInfoOrcamento(label = "Matrícula", valor = orcamento.matricula)

                if (expandido) {
                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    LinhaInfoOrcamento(label = "Ano", valor = orcamento.ano)

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    LinhaInfoOrcamento(label = "Valor Total", valor = orcamento.orcamento.valor)

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    Column {
                        Text(
                            text = "Reparação associada ao Orçamento",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (orcamento.numeroReparacao != null)
                                "Reparação Nº ${orcamento.numeroReparacao}"
                            else
                                "Não está associado a uma reparação.",
                            color = Color(0xFFFFA500),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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
fun LinhaInfoOrcamento(label: String, valor: String) {
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
fun PreviewHistoricoOrcamentosVazio() {
    HistoricoOrcamentosScreen(navController = rememberNavController(), clienteId = 1)
}
