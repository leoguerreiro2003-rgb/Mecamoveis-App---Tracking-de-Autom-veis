package com.example.projetomecamoveis.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomecamoveis.data.AppDatabase
import com.example.projetomecamoveis.model.ReparacaoInfo
import com.example.projetomecamoveis.model.VeiculoInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReparacaoViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).reparacaoDao()
    private val veiculoDao = AppDatabase.getDatabase(application).veiculoDao()

    var addSucesso = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var veiculosDoCliente = mutableStateOf<List<VeiculoInfo>>(emptyList())
        private set

    fun getReparacoesByCliente(clienteId: Int): Flow<List<ReparacaoInfo>> {
        return dao.getReparacoesByCliente(clienteId)
    }

    fun carregarVeiculos(clienteId: Int) {
        viewModelScope.launch {
            veiculosDoCliente.value = veiculoDao.getVeiculosByClienteList(clienteId)
        }
    }

    fun adicionarReparacao(
        clienteId: Int,
        veiculo: VeiculoInfo?,
        pecas: String,
        valor: String,
        estado: String
    ) {
        if (clienteId == 0 || veiculo == null || pecas.isBlank() || valor.isBlank() || estado.isBlank()) {
            errorMessage.value = "Por favor, preencha todos os campos e selecione o cliente, veículo e estado."
            return
        }

        viewModelScope.launch {
            val dataFormatada = SimpleDateFormat("dd/MM  HH:mm", Locale.getDefault()).format(Date())
            val numReparacao = (100..999).random().toString()

            val nova = ReparacaoInfo(
                clienteId = clienteId,
                dataHora = dataFormatada,
                titulo = "Reparação ${veiculo.marca}",
                marca = veiculo.marca,
                modelo = veiculo.modelo,
                matricula = veiculo.matricula,
                ano = veiculo.ano,
                pecasUtilizadas = pecas,
                numeroReparacao = numReparacao,
                valor = if (valor.contains("€")) valor else "${valor}€",
                estado = estado
            )

            dao.insert(nova)
            
            // Marcar o veículo como em reparação (se não for Concluída)
            val aindaEmReparacao = estado != "Reparação Concluída"
            veiculoDao.atualizarEstadoReparacao(veiculo.id, aindaEmReparacao, estado)

            // Atualizar data do estado específico
            when (estado) {
                "Reparação Iniciada" -> veiculoDao.atualizarDataIniciada(veiculo.id, dataFormatada)
                "Em Reparação" -> veiculoDao.atualizarDataEmReparacao(veiculo.id, dataFormatada)
                "Revisão" -> veiculoDao.atualizarDataRevisao(veiculo.id, dataFormatada)
                "Reparação Concluída" -> veiculoDao.atualizarDataConcluida(veiculo.id, dataFormatada)
            }

            addSucesso.value = true
        }
    }

    fun atualizarEstadoReparacaoExistente(veiculoId: Int, novoEstado: String) {
        if (veiculoId == 0 || novoEstado.isBlank() || novoEstado.startsWith("Ex:")) {
            errorMessage.value = "Por favor, selecione um estado válido."
            return
        }

        viewModelScope.launch {
            val dataFormatada = SimpleDateFormat("dd/MM  HH:mm", Locale.getDefault()).format(Date())
            
            // Atualizar estado geral e sair da oficina se concluída
            val aindaEmReparacao = novoEstado != "Reparação Concluída"
            veiculoDao.atualizarEstadoReparacao(veiculoId, aindaEmReparacao, novoEstado)

            // Atualizar data do estado específico
            when (novoEstado) {
                "Reparação Iniciada" -> veiculoDao.atualizarDataIniciada(veiculoId, dataFormatada)
                "Em Reparação" -> veiculoDao.atualizarDataEmReparacao(veiculoId, dataFormatada)
                "Revisão" -> veiculoDao.atualizarDataRevisao(veiculoId, dataFormatada)
                "Reparação Concluída" -> veiculoDao.atualizarDataConcluida(veiculoId, dataFormatada)
            }

            addSucesso.value = true
        }
    }

    fun resetarSucesso() {
        addSucesso.value = false
        errorMessage.value = null
    }
}
