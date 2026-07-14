package com.example.projetomecamoveis.view.mecanico

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.ui.theme.LexendFontFamily

@Composable
fun HomeMecanicoScreen(navController: NavHostController, mecanicoNome: String) {
    HomeMecanicoContent(
        navController = navController,
        mecanicoNome = mecanicoNome
    )
}

@Composable
fun HomeMecanicoContent(navController: NavHostController, mecanicoNome: String) {
    val primeiroNome = mecanicoNome.trim().split(" ").firstOrNull() ?: mecanicoNome
    var showExitDialog by remember { mutableStateOf(false) }

    // Interceptar botão "voltar" do sistema
    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = "Terminar Sessão",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Deseja mesmo sair? A sua sessão será finalizada.",
                    color = Color.LightGray
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    navController.navigate("menu") {
                        popUpTo("home_mecanico") { inclusive = true }
                    }
                }) {
                    Text(text = "Sair", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(text = "Cancelar", color = Color(0xFFFFBD49))
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
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { showExitDialog = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left_28_regular),
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Row {
                    Text(text = "Olá, ", color = Color(0xFFFFBD49), fontSize = 18.sp)
                    Text(
                        text = "$primeiroNome!",
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

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.logotipo_completo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            HomeMecanicoMenuButton(text = "Gerir Clientes") { navController.navigate("gerir_clientes") }

            Spacer(modifier = Modifier.height(32.dp))

            HomeMecanicoMenuButton(text = "Gerir Veículos") { navController.navigate("gerir_veiculos") }

            Spacer(modifier = Modifier.height(32.dp))

            HomeMecanicoMenuButton(text = "Gerir Orçamentos") { navController.navigate("gerir_orcamentos") }

            Spacer(modifier = Modifier.height(32.dp))

            HomeMecanicoMenuButton(text = "Gerir Reparações") { navController.navigate("gerir_reparacoes") }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHomeMecanico() {
    HomeMecanicoContent(navController = rememberNavController(), mecanicoNome = "José")
}

@Composable
fun HomeMecanicoMenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBD49))
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = LexendFontFamily
        )
    }
}


