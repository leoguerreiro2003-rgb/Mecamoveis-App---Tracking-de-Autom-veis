package com.example.projetomecamoveis.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.model.VeiculoInfo
import com.example.projetomecamoveis.viewmodel.VeiculoViewModel
import kotlin.math.sin

@Composable
fun DetalhesEstadoReparacaoScreen(
    navController: NavHostController,
    veiculoId: Int,
    clienteNome: String,
    viewModel: VeiculoViewModel = viewModel()
) {
    val veiculoState = viewModel.getVeiculoById(veiculoId).collectAsState(initial = null)
    val veiculo = veiculoState.value
    
    val estados = listOf("Reparação Iniciada", "Em Reparação", "Revisão", "Reparação Concluída")
    val estadoAtual = veiculo?.ultimoEstado ?: "Reparação Iniciada"

    // Calcular progresso (0.25 a 1.0)
    val progresso = when (estadoAtual) {
        "Reparação Iniciada" -> 0.25f
        "Em Reparação" -> 0.50f
        "Revisão" -> 0.75f
        "Reparação Concluída" -> 1.0f
        else -> 0.25f
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
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFFFFA500))) { append("Olá, ") }
                        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) { append(clienteNome) }
                        withStyle(style = SpanStyle(color = Color.White)) { append("!") }
                    },
                    fontSize = 18.sp
                )

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFA500))
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- WAVE ANIMATION CIRCLE ---
            WaveCircle(progress = progresso)

            Spacer(modifier = Modifier.height(20.dp))

            // --- TÍTULO DO VEÍCULO ---
            Text(
                text = if (veiculo != null) "${veiculo.marca} ${veiculo.modelo} do $clienteNome" else "Carregando...",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- TIMELINE DE ESTADOS ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                estados.forEach { estado ->
                    val isSelecionado = estado == estadoAtual
                    val dataEstado = when (estado) {
                        "Reparação Iniciada" -> veiculo?.dataIniciada
                        "Em Reparação" -> veiculo?.dataEmReparacao
                        "Revisão" -> veiculo?.dataRevisao
                        "Reparação Concluída" -> veiculo?.dataConcluida
                        else -> null
                    } ?: ""

                    Column {
                        if (dataEstado.isNotBlank()) {
                            Text(
                                text = dataEstado,
                                color = Color.Gray,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(start = 32.dp, bottom = 4.dp)
                            )
                        }
                        
                        TimelineItem(
                            label = estado,
                            isAtivo = isSelecionado
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WaveCircle(progress: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.Black)
            .border(2.dp, Color(0xFFFFA500), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val fillHeight = height * (1f - progress)

            val wavePath = Path().apply {
                val waveAmplitude = 10f
                val waveLength = width
                
                moveTo(0f, height)
                lineTo(0f, fillHeight)
                
                for (x in 0..width.toInt()) {
                    val y = fillHeight + waveAmplitude * sin((x / waveLength) * 2f * Math.PI.toFloat() + phase)
                    lineTo(x.toFloat(), y)
                }
                
                lineTo(width, height)
                close()
            }

            clipPath(Path().apply { addOval(size.toRect()) }) {
                drawPath(
                    path = wavePath,
                    color = Color(0xFFFFA500)
                )
            }
        }
    }
}

@Composable
fun TimelineItem(label: String, isAtivo: Boolean) {
    val backgroundColor = if (isAtivo) Color(0xFFFFA500) else Color(0xFF5D4037).copy(alpha = 0.6f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(35.dp))
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isAtivo) Color.Black else Color.Black.copy(alpha = 0.1f))
                    .border(1.dp, if (isAtivo) Color.Transparent else Color.Black.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isAtivo) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFFA500)))
                }
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Text(
                text = label,
                color = if (isAtivo) Color.Black else Color.Black.copy(alpha = 0.6f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTimelineAnimated() {
    DetalhesEstadoReparacaoScreen(navController = rememberNavController(), veiculoId = 1, clienteNome = "Manuel")
}
