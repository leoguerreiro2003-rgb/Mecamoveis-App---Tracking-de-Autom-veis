package com.example.projetomecamoveis.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomecamoveis.data.AppDatabase
import com.example.projetomecamoveis.model.LoginClienteInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// Usamos AndroidViewModel para ter acesso ao 'application' (contexto) para a base de dados
class LoginClienteViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).loginClienteDao()

    val todosClientes: Flow<List<LoginClienteInfo>> = dao.getAllClientes()

    // Estado de erro visível para a UI
    var errorMessage = mutableStateOf<String?>(null)
        private set

    // Função para verificar login (agora assíncrona com Room)
    // Altera a assinatura da função para retornar o objeto LoginClienteInfo
    fun verificarLogin(email: String, pass: String, onResultado: (LoginClienteInfo?) -> Unit) {
        // .trim() remove espaços acidentais no início ou fim
        val emailLimpo = email.trim()
        val passLimpa = pass.trim()

        if (emailLimpo.isBlank() || passLimpa.isBlank()) {
            errorMessage.value = "Por favor, preencha todos os campos."
            onResultado(null)
            return
        }

        viewModelScope.launch {
            // Tentamos encontrar o cliente (o email passará a ser comparado sem ligar a maiúsculas)
            val cliente = dao.verifyLogin(emailLimpo, passLimpa)

            if (cliente != null) {
                errorMessage.value = null
                onResultado(cliente)
            } else {
                errorMessage.value = "Email ou palavra-passe incorretos."
                onResultado(null)
            }
        }
    }

    // Função para adicionar cliente à base de dados
    fun adicionarCliente(cliente: LoginClienteInfo) {
        viewModelScope.launch {
            dao.insert(cliente)
        }
    }


}


