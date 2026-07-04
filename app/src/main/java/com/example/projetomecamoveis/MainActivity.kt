package com.example.projetomecamoveis

import android.os.Bundle
import com.google.android.gms.ads.MobileAds
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projetomecamoveis.view.ContactosScreen
import com.example.projetomecamoveis.view.CriarContaClienteScreen
import com.example.projetomecamoveis.view.HomeClienteScreen
import com.example.projetomecamoveis.view.HomeMecanicoScreen
import com.example.projetomecamoveis.view.LoginClienteScreen
import com.example.projetomecamoveis.view.LoginMecanicoScreen
import com.example.projetomecamoveis.view.MeusVeiculosScreen
import com.example.projetomecamoveis.view.NovoVeiculoScreen
import com.example.projetomecamoveis.view.NovoVeiculoMecanicoScreen
import com.example.projetomecamoveis.view.EditarVeiculoScreen
import com.example.projetomecamoveis.view.HistoricoReparacoesScreen
import com.example.projetomecamoveis.view.HistoricoOrcamentosScreen
import com.example.projetomecamoveis.view.GerirClientesScreen
import com.example.projetomecamoveis.view.GerirReparacoesScreen
import com.example.projetomecamoveis.view.GerirVeiculosScreen
import com.example.projetomecamoveis.view.ListaVeiculosMecanicoScreen
import com.example.projetomecamoveis.view.NovaReparacaoScreen
import com.example.projetomecamoveis.view.AtualizarEstadoReparacaoScreen
import com.example.projetomecamoveis.view.HistoricoReparacoesMecanicoScreen
import com.example.projetomecamoveis.view.DetalhesEstadoReparacaoScreen
import com.example.projetomecamoveis.view.GerirOrcamentosScreen
import com.example.projetomecamoveis.view.NovoOrcamentoScreen
import com.example.projetomecamoveis.view.EditarOrcamentoScreen
import com.example.projetomecamoveis.view.ListaClientesScreen
import com.example.projetomecamoveis.view.MenuScreen
import com.example.projetomecamoveis.view.SobreNosScreen
import com.example.projetomecamoveis.view.SplashScreen

// ─────────────────────────────────────────────
// ACTIVITY PRINCIPAL — ponto de entrada da app
// ─────────────────────────────────────────────
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa o SDK do Google Mobile Ads
        MobileAds.initialize(this) {}
        setContent {
            AppNavigation() // lança o sistema de navegação
        }
    }
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // rememberNavController() → cria e guarda o controlador de navegação

    NavHost(
        navController = navController,
        startDestination = "splash" // primeiro ecrã a aparecer
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("menu") { MenuScreen(navController) }
        composable("login_cliente") { LoginClienteScreen(navController) }
        composable("login_mecanico") { LoginMecanicoScreen(navController) }
        composable("sobre_nos") { SobreNosScreen(navController) }
        composable("contactos") { ContactosScreen(navController) }
        composable("criar_conta_cliente") { CriarContaClienteScreen(navController) }
        composable("home_cliente/{clienteId}/{clienteNome}") { backStackEntry -> 
            val id = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull() ?: 0
            val nome = backStackEntry.arguments?.getString("clienteNome") ?: "Cliente"
            HomeClienteScreen(navController, id, nome) 
        }
        composable("home_mecanico/{mecanicoNome}") { backStackEntry -> val nome = backStackEntry.arguments?.getString("mecanicoNome") ?: "Mecânico"
            HomeMecanicoScreen(navController, nome)
            }
        composable("meus_veiculos/{clienteId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull() ?: 0
            MeusVeiculosScreen(navController, id) 
        }
        composable("novo_veiculo/{clienteId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull() ?: 0
            NovoVeiculoScreen(navController, id)
        }
        composable("historico_reparacoes/{clienteId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull() ?: 0
            HistoricoReparacoesScreen(navController, id)
        }
        composable("historico_orcamentos/{clienteId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull() ?: 0
            HistoricoOrcamentosScreen(navController, id)
        }
        composable("gerir_clientes") { GerirClientesScreen(navController) }
        composable("gerir_orcamentos") { GerirOrcamentosScreen(navController) }
        composable("novo_orcamento") { NovoOrcamentoScreen(navController) }
        composable("editar_orcamento/{orcamentoId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("orcamentoId")?.toIntOrNull() ?: 0
            EditarOrcamentoScreen(navController, id)
        }
        composable("gerir_reparacoes") { GerirReparacoesScreen(navController) }
        composable("gerir_veiculos") { GerirVeiculosScreen(navController) }
        composable("novo_veiculo_mecanico") { NovoVeiculoMecanicoScreen(navController) }
        composable("editar_veiculo/{veiculoId}") { backStackEntry ->
            val veiculoId = backStackEntry.arguments?.getString("veiculoId")?.toIntOrNull() ?: 0
            EditarVeiculoScreen(navController, veiculoId)
        }
        composable("lista_veiculos_mecanico") { ListaVeiculosMecanicoScreen(navController) }
        composable("historico_reparacoes_mecanico") { HistoricoReparacoesMecanicoScreen(navController) }
        composable("nova_reparacao") { NovaReparacaoScreen(navController) }
        composable("atualizar_estado_reparacao/{veiculoId}") { backStackEntry ->
            val veiculoId = backStackEntry.arguments?.getString("veiculoId")?.toIntOrNull() ?: 0
            AtualizarEstadoReparacaoScreen(navController, veiculoId)
        }
        composable("detalhes_estado_reparacao/{veiculoId}/{clienteNome}") { backStackEntry ->
            val veiculoId = backStackEntry.arguments?.getString("veiculoId")?.toIntOrNull() ?: 0
            val clienteNome = backStackEntry.arguments?.getString("clienteNome") ?: "Cliente"
            DetalhesEstadoReparacaoScreen(navController, veiculoId, clienteNome)
        }
        composable("lista_clientes") { ListaClientesScreen(navController) }
        }
}
