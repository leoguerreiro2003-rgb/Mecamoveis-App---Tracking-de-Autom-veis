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
import com.example.projetomecamoveis.model.ReparacaoInfo
import com.example.projetomecamoveis.viewmodel.ReparacaoViewModel

@Composable
fun HistoricoReparacoesScreen(
    navController: NavHostController,
    clienteId: Int,
    viewModel: ReparacaoViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        android.util.Log.d("HistoricoScreen", "Ecrã de histórico aberto para o cliente: $clienteId")
    }

    val reparacoesState =
        viewModel.getReparacoesByCliente(clienteId).collectAsState(initial = emptyList())
    val reparacoes = reparacoesState.value

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
                        text = "Histórico de ",
                        color = Color(0xFFFFA500),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Reparações",
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

            if (reparacoes.isEmpty()) {
                // ESTADO VAZIO (Imagem 2)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(35.dp))
                        .padding(32.dp)
                ) {
                    Text(
                        text = "De momento não tem nenhuma Reparação registada.",
                        color = Color.White,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                        lineHeight = 22.sp
                    )
                }
            } else {
                // LISTA DE REPARAÇÕES (Imagem 1)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    reparacoes.forEach { reparacao ->
                        ReparacaoCard(reparacao = reparacao)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ReparacaoCard(reparacao: ReparacaoInfo) {
    var expandido by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Data e Hora acima do card
        Text(
            text = reparacao.dataHora,
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
                // Título da Reparação
                Text(
                    text = reparacao.titulo,
                    color = Color(0xFFFFA500),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                // Campos Comuns (Marca, Modelo, Matrícula)
                HistoricoLinhaInfo(label = "Marca", valor = reparacao.marca)
                HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                HistoricoLinhaInfo(label = "Modelo", valor = reparacao.modelo)
                HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                HistoricoLinhaInfo(label = "Matrícula", valor = reparacao.matricula)

                if (expandido) {
                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    HistoricoLinhaInfo(label = "Ano", valor = reparacao.ano)

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                    Column {
                        Text(text = "Peças Utilizadas", color = Color.White, fontSize = 13.sp)
                        Text(
                            text = reparacao.pecasUtilizadas,
                            color = Color(0xFFFFA500),
                            fontSize = 13.sp
                        )
                    }

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    HistoricoLinhaInfo(label = "Reparação nº", valor = reparacao.numeroReparacao)

                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    HistoricoLinhaInfo(label = "Valor da Reparação", valor = reparacao.valor)
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
fun HistoricoLinhaInfo(label: String, valor: String) {
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
fun PreviewHistoricoVazio() {
    HistoricoReparacoesScreen(navController = rememberNavController(), clienteId = 1)
}
