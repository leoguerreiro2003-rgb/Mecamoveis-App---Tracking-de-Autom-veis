package com.example.projetomecamoveis.view.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.ui.theme.LexendFontFamily


// ─────────────────────────────────────────────
// ECRÃ PRINCIPAL — Menu de entrada da aplicação
// Mostra os botões de navegação para as diferentes áreas
// ─────────────────────────────────────────────
@Composable
fun MenuScreen(navController: NavController) {

    // Gradiente vertical do fundo: preto no topo, castanho dourado escuro em baixo
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1A1A), // topo — quase preto
            Color(0xFF3D2B00)  // baixo — castanho dourado escuro
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.82f) // ocupa 82% da largura — deixa margens laterais
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logotipo_completo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Texto de boas-vindas com fundo semi-transparente
            Text(
                text = "Damos-lhe as Boas-Vindas ao seu novo tracking de automóveis.",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .background(
                        color = Color(0x33FFFFFF), // branco a 20% de opacidade
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(horizontal = 22.dp, vertical = 22.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Cada botão navega para um ecrã diferente
            // BotaoMenu é um Composable reutilizável definido no fundo deste ficheiro
            BotaoMenu(texto = "Login Cliente") {
                navController.navigate("login_cliente")
            }

            Spacer(modifier = Modifier.height(1.dp))

            BotaoMenu(texto = "Login Mecânico") {
                navController.navigate("login_mecanico")
            }

            Spacer(modifier = Modifier.height(1.dp))

            BotaoMenu(texto = "Sobre-Nós") {
                navController.navigate("sobre_nos")
            }

            Spacer(modifier = Modifier.height(1.dp))

            BotaoMenu(texto = "Contactos") {
                navController.navigate("contactos")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMenu() {
    MenuScreen(navController = rememberNavController())
}

// ─────────────────────────────────────────────
// COMPONENTE REUTILIZÁVEL — Botão dourado do menu
// Usado em todos os botões do MenuScreen
// "texto"   → texto que aparece no botão
// "onClick" → ação executada ao carregar
// ─────────────────────────────────────────────
@Composable
fun BotaoMenu(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(50.dp), // forma de pílula
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFBD49) // laranja/dourado
        )
    ) {
        Text(
            text = texto,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = LexendFontFamily
        )
    }
}

