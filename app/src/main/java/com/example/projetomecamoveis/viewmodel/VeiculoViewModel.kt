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
    
    val todosVeiculosComCliente: Flow<List<VeiculoComCliente>> = dao.getAllVeiculosComCliente()

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

    // Estado de erro para o ano
    var anoError = mutableStateOf<String?>(null)
        private set

    // Estado de erro geral (campos vazios)
    var errorMessage = mutableStateOf<String?>(null)
        private set

    // Estado de sucesso para navegação
    var addSucesso = mutableStateOf(false)
        private set

    fun validarDadosVeiculo(
        marca: String,
        modelo: String,
        matricula: String,
        ano: String,
        kms: String
    ): Boolean {
        matriculaError.value = null
        anoError.value = null
        errorMessage.value = null

        var temErro = false

        if (marca.isBlank() || modelo.isBlank() || matricula.isBlank() || ano.isBlank() || kms.isBlank()) {
            errorMessage.value = "Por favor, preencha todos os campos."
            temErro = true
        }

        // Validação de Formato de Matrícula (AA-00-AA)
        val matriculaRegex = "^[A-Z]{2}[0-9]{2}[A-Z]{2}$".toRegex()
        if (matricula.isNotBlank() && !matricula.matches(matriculaRegex)) {
            matriculaError.value = "Formato inválido. Use 2 letras, 2 números e 2 letras (ex: AA00AA)."
            temErro = true
        }

        val anoInt = ano.toIntOrNull()
        val anoAtual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        if (ano.isNotBlank()) {
            if (anoInt == null || anoInt > anoAtual || anoInt < 1886) {
                anoError.value = "Ano inválido. Por favor insira um ano até $anoAtual."
                temErro = true
            }
        }

        return !temErro
    }

    private fun formatarMatrículaParaBD(matricula: String): String {
        return if (matricula.length == 6) {
            "${matricula.substring(0, 2)}-${matricula.substring(2, 4)}-${matricula.substring(4, 6)}"
        } else {
            matricula
        }
    }

    fun adicionarVeiculo(
        clienteId: Int,
        marca: String,
        modelo: String,
        matricula: String,
        ano: String,
        kms: String
    ) {
        if (clienteId == 0) {
            errorMessage.value = "Por favor, selecione um cliente."
            return
        }
        if (!validarDadosVeiculo(marca, modelo, matricula, ano, kms)) return

        viewModelScope.launch {
            // Verificar limite de 3 veículos
            val listaAtual = dao.getVeiculosByCliente(clienteId).firstOrNull() ?: emptyList()
            if (listaAtual.size >= 3) {
                errorMessage.value = "Você já tem o máximo de veículos associados."
                return@launch
            }

            // Verificar se a matrícula já existe
            val veiculoExistente = dao.getVeiculoByMatricula(matricula.trim())
            if (veiculoExistente != null) {
                matriculaError.value = "essa matrícula já está ser utilizada"
                return@launch
            }

            // Inserir na BD
            val novoVeiculo = VeiculoInfo(
                clienteId = clienteId,
                marca = marca,
                modelo = modelo,
                matricula = formatarMatrículaParaBD(matricula),
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

    fun editarVeiculo(
        veiculoId: Int,
        clienteId: Int,
        marca: String,
        modelo: String,
        matricula: String,
        ano: String,
        kms: String
    ) {
        if (clienteId == 0) {
            errorMessage.value = "Por favor, selecione um cliente."
            return
        }
        if (!validarDadosVeiculo(marca, modelo, matricula, ano, kms)) return

        viewModelScope.launch {
            // Se o cliente mudou, verificar limite de 3
            val veiculoAtual = dao.getVeiculoById(veiculoId)
            if (veiculoAtual != null && veiculoAtual.clienteId != clienteId) {
                val listaNovoDono = dao.getVeiculosByClienteList(clienteId)
                if (listaNovoDono.size >= 3) {
                    errorMessage.value = "Este cliente já tem o máximo de veículos associados."
                    return@launch
                }
            }

            // Verificar se a matrícula já existe em OUTRO veículo
            val veiculoComMesmaMatricula = dao.getVeiculoByMatricula(formatarMatrículaParaBD(matricula))
            if (veiculoComMesmaMatricula != null && veiculoComMesmaMatricula.id != veiculoId) {
                matriculaError.value = "esta matrícula já está a ser utilizada por outro veículo"
                return@launch
            }

            val veiculoEditado = VeiculoInfo(
                id = veiculoId,
                clienteId = clienteId,
                marca = marca,
                modelo = modelo,
                matricula = formatarMatrículaParaBD(matricula),
                ano = ano,
                kms = kms,
                emReparacao = veiculoAtual?.emReparacao ?: false,
                ultimoEstado = veiculoAtual?.ultimoEstado ?: "",
                dataIniciada = veiculoAtual?.dataIniciada ?: "",
                dataEmReparacao = veiculoAtual?.dataEmReparacao ?: "",
                dataRevisao = veiculoAtual?.dataRevisao ?: "",
                dataConcluida = veiculoAtual?.dataConcluida ?: ""
            )

            dao.update(veiculoEditado)
            addSucesso.value = true
        }
    }

    fun deletarVeiculo(veiculo: VeiculoInfo) {
        viewModelScope.launch {
            dao.delete(veiculo)
        }
    }
}
