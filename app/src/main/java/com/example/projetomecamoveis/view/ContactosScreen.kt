package com.example.projetomecamoveis.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projetomecamoveis.R
import com.example.projetomecamoveis.model.LoginMecanicoInfo
import com.example.projetomecamoveis.viewmodel.ContactosViewModel

// ─────────────────────────────────────────────
// ECRÃ DE CONTACTOS — lista dos mecânicos da oficina
// A lista de mecânicos vem do ContactosViewModel (não está hardcoded aqui)
// ─────────────────────────────────────────────
@Composable
fun ContactosScreen(
    navController: NavController,
    viewModel: ContactosViewModel = viewModel() // ViewModel injetado — fornece a lista de mecânicos
) {

    // Vai buscar a lista de mecânicos ao ViewModel
    // Quando tiveres base de dados, o ViewModel atualizará esta lista automaticamente
    val mecanicos = viewModel.mecanicos

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // permite scroll se houver muitos mecânicos
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

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

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Contactos",
                color = Color(0xFFFFA500),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Para cada mecânico da lista, mostra um cartão
            // Quando a lista crescer (mais mecânicos), esta secção cresce automaticamente
            mecanicos.forEach { mecanico ->
                CartaoMecanico(mecanico = mecanico)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────
// COMPONENTE — Cartão de apresentação de um mecânico
// Recebe um MecanicoInfo e mostra os seus dados formatados
// ─────────────────────────────────────────────
@Composable
fun CartaoMecanico(mecanico: LoginMecanicoInfo) { // Alterar aqui
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF2A2A2A), shape = RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Mecânico: ${mecanico.nome}",
                color = Color(0xFFFFA500),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            // Linha separadora fina
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF3D3D3D)))
            LinhaContacto(label = "Número", valor = mecanico.numero)
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF3D3D3D)))
            LinhaContacto(label = "Email", valor = mecanico.email)
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF3D3D3D)))
            LinhaContacto(label = "Contacto", valor = mecanico.contacto)
        }
    }
}

// ─────────────────────────────────────────────
// COMPONENTE — Linha com label à esquerda e valor à direita
// Reutilizado dentro do CartaoMecanico para cada campo
// ─────────────────────────────────────────────
@Composable
fun LinhaContacto(label: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.White, fontSize = 14.sp, modifier = Modifier.width(80.dp))
        Text(text = valor, color = Color(0xFFFFA500), fontSize = 14.sp)
    }
}