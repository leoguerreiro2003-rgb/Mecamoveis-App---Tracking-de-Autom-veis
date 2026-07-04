package com.example.projetomecamoveis.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.R

@Composable
fun HomeMecanicoScreen(navController: NavHostController, mecanicoNome: String) {
    val primeiroNome = mecanicoNome.trim().split(" ").firstOrNull() ?: mecanicoNome

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

                Row {
                    Text(text = "Olá, ", color = Color(0xFFFFA500), fontSize = 18.sp)
                    Text(text = "$primeiroNome!", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color(0xFFFFA500),
                    modifier = Modifier.size(45.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.logotipoprototipo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            MenuButton(text = "Gerir Clientes") { navController.navigate("gerir_clientes") }
            Spacer(modifier = Modifier.height(16.dp))
            MenuButton(text = "Gerir Veículos") { navController.navigate("gerir_veiculos") }
            Spacer(modifier = Modifier.height(16.dp))
            MenuButton(text = "Gerir Orçamentos") { /* Navegar */ }
            Spacer(modifier = Modifier.height(16.dp))
            MenuButton(text = "Gerir Reparações") { navController.navigate("gerir_reparacoes") }
            Spacer(modifier = Modifier.height(16.dp))
            MenuButton(text = "Gerir Prazos") { /* Navegar */ }
        }
    }
}

