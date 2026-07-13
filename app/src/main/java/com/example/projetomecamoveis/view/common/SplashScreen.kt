package com.example.projetomecamoveis.view.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────
// ECRÃ DE SPLASH — aparece quando a app abre
// Mostra o logo durante 3 segundos e navega para o Menu
// ─────────────────────────────────────────────
@Composable
fun SplashScreen(navController: NavController) {

    // LaunchedEffect(Unit) → corre 1 única vez quando o ecrã é criado
    // É o sítio certo para lógica assíncrona como delays ou carregamento inicial
    LaunchedEffect(Unit) {
        delay(3000) // espera 3 segundos
        navController.navigate("menu") {
            // Remove a splash do histórico para o utilizador não voltar a ela
            popUpTo("splash") { inclusive = true }
        }
    }

    // Layout da splash — fundo preto com logo e spinner centrados
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logotipo_completo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Spinner animado em laranja — mesmo esquema de cores da app
            CircularProgressIndicator(
                color = Color(0xFFFFBD49),
                strokeWidth = 4.dp,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSplash() {
    SplashScreen(navController = rememberNavController())
}

