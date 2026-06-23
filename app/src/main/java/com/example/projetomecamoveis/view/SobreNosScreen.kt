package com.example.projetomecamoveis.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projetomecamoveis.R

// ─────────────────────────────────────────────
// ECRÃ SOBRE NÓS — apresentação da empresa
// Ecrã estático, apenas mostra texto informativo
// Não precisa de ViewModel porque não tem lógica nem dados dinâmicos
// ─────────────────────────────────────────────
@Composable
fun SobreNosScreen(navController: NavController) {

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
            Spacer(modifier = Modifier.height(32.dp))

            // Botão de voltar ao ecrã anterior
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left_28_regular),
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = R.drawable.logotipoprototipo),
                contentDescription = "Logo",
                modifier = Modifier.size(135.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Sobre Nós",
                color = Color(0xFFFFA500),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Caixa com o texto descritivo da empresa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Somos uma empresa do setor automóvel, fundada em 1997, com o objetivo de elevar a experiência dos nossos clientes no que diz respeito à assistência em oficina.\n\nTrabalhamos para que tenha acesso, em apenas um clique, a toda a informação essencial sobre o estado do seu veículo e sobre os serviços realizados.",
                    color = Color.White,
                    fontSize = 15.sp,
                    lineHeight = 22.sp // espaçamento entre linhas para melhor leitura
                )
            }
        }
    }
}