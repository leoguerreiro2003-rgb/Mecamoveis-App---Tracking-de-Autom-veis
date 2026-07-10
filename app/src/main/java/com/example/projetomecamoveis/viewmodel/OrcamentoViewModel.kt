package com.example.projetomecamoveis.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomecamoveis.data.AppDatabase
import com.example.projetomecamoveis.model.OrcamentoInfo
import com.example.projetomecamoveis.model.ReparacaoInfo
import com.example.projetomecamoveis.model.VeiculoInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OrcamentoViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).orcamentoDao()
    private val reparacaoDao = AppDatabase.getDatabase(application).reparacaoDao()
    private val veiculoDao = AppDatabase.getDatabase(application).veiculoDao()

    var addSucesso = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var veiculosDoCliente = mutableStateOf<List<VeiculoInfo>>(emptyList())
        private set

    fun getOrcamentosByCliente(clienteId: Int): Flow<List<OrcamentoInfo>> {
        return dao.getOrcamentosByCliente(clienteId)
    }

    fun getOrcamentosDetalhadosByCliente(clienteId: Int) =
        dao.getOrcamentosDetalhadosByCliente(clienteId)

    fun carregarVeiculos(clienteId: Int) {
        viewModelScope.launch {
            veiculosDoCliente.value = veiculoDao.getVeiculosByClienteList(clienteId)
        }
    }

    fun getReparacoesByCliente(clienteId: Int): Flow<List<ReparacaoInfo>> {
        return reparacaoDao.getReparacoesAtivasByCliente(clienteId)
    }

    val todosOrcamentosDetalhados = dao.getAllOrcamentosDetalhados()

    fun getOrcamentoById(id: Int): Flow<OrcamentoInfo?> {
        return kotlinx.coroutines.flow.flow {
            emit(dao.getOrcamentoById(id))
        }
    }

    fun adicionarOrcamento(
        clienteId: Int,
        veiculo: VeiculoInfo?,
        reparacao: ReparacaoInfo?,
        titulo: String,
        valor: String
    ) {
        if (clienteId == 0 || veiculo == null || titulo.isBlank() || valor.isBlank()) {
            errorMessage.value = "Por favor, preencha todos os campos."
            return
        }

        viewModelScope.launch {
            val novo = OrcamentoInfo(
                clienteId = clienteId,
                veiculoId = veiculo.id,
                reparacaoId = reparacao?.id,
                titulo = titulo,
                valor = if (valor.contains("€")) valor else "${valor}€"
            )

            dao.insert(novo)
            addSucesso.value = true
        }
    }

    fun atualizarOrcamento(
        id: Int,
        clienteId: Int,
        veiculoId: Int,
        reparacaoId: Int?,
        titulo: String,
        valor: String
    ) {
        if (clienteId == 0 || veiculoId == 0 || titulo.isBlank() || valor.isBlank()) {
            errorMessage.value = "Por favor, preencha todos os campos."
            return
        }

        viewModelScope.launch {
            val orcamento = OrcamentoInfo(
                id = id,
                clienteId = clienteId,
                veiculoId = veiculoId,
                reparacaoId = reparacaoId,
                titulo = titulo,
                valor = if (valor.contains("€")) valor else "${valor}€"
            )
            dao.update(orcamento)
            addSucesso.value = true
        }
    }

    fun resetarSucesso() {
        addSucesso.value = false
        errorMessage.value = null
    }
}
