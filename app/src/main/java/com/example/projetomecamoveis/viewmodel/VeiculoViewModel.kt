package com.example.projetomecamoveis.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomecamoveis.data.AppDatabase
import com.example.projetomecamoveis.model.VeiculoInfo
import com.example.projetomecamoveis.model.VeiculoComCliente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class VeiculoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).veiculoDao()

    val veiculosEmReparacaoComCliente: Flow<List<VeiculoComCliente>> = dao.getVeiculosEmReparacaoComCliente()

    fun getVeiculosByCliente(clienteId: Int): Flow<List<VeiculoInfo>> {
        return dao.getVeiculosByCliente(clienteId)
    }

    fun getVeiculosEmReparacao(clienteId: Int): Flow<List<VeiculoInfo>> {
        return dao.getVeiculosByCliente(clienteId)
            .map { lista ->
                val filtrados = lista.filter { it.emReparacao }
                Log.d("VeiculoViewModel", "Cliente $clienteId: Total=${lista.size}, Filtrados=${filtrados.size}")
                filtrados
            }
    }

    fun getVeiculoById(veiculoId: Int): Flow<VeiculoInfo?> {
        return dao.getVeiculosByCliente(0).map { _ -> // Apenas para disparar o flow se necessário
            dao.getVeiculoById(veiculoId)
        }
    }

    // Estado de erro para a matrícula
    var matriculaError = mutableStateOf<String?>(null)
        private set

    // Estado de erro geral (campos vazios)
    var errorMessage = mutableStateOf<String?>(null)
        private set

    // Estado de sucesso para navegação
    var addSucesso = mutableStateOf(false)
        private set

    fun adicionarVeiculo(
        clienteId: Int,
        marca: String,
        modelo: String,
        matricula: String,
        ano: String,
        kms: String
    ) {
        // Limpar erros
        matriculaError.value = null
        errorMessage.value = null

        // 1. Validação de campos vazios
        if (marca.isBlank() || modelo.isBlank() || matricula.isBlank() || ano.isBlank() || kms.isBlank()) {
            errorMessage.value = "Por favor, preencha todos os campos."
            return
        }

        viewModelScope.launch {
            // Verificar limite de 3 veículos
            val listaAtual = dao.getVeiculosByCliente(clienteId).firstOrNull() ?: emptyList()
            if (listaAtual.size >= 3) {
                errorMessage.value = "Você já tem o máximo de veículos associados."
                return@launch
            }

            // 2. Verificar se a matrícula já existe
            val veiculoExistente = dao.getVeiculoByMatricula(matricula.trim())
            if (veiculoExistente != null) {
                matriculaError.value = "essa matrícula já está ser utilizada"
                return@launch
            }

            // 3. Inserir na BD
            val novoVeiculo = VeiculoInfo(
                clienteId = clienteId,
                marca = marca,
                modelo = modelo,
                matricula = matricula.trim(),
                ano = ano,
                kms = kms
            )
            
            dao.insert(novoVeiculo)
            addSucesso.value = true
        }
    }

    fun resetarSucesso() {
        addSucesso.value = false
    }
}
