package com.example.projetomecamoveis.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomecamoveis.data.AppDatabase
import com.example.projetomecamoveis.model.LoginMecanicoInfo
import kotlinx.coroutines.launch

class LoginMecanicoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).loginMecanicoDao()
    var errorMessage = mutableStateOf<String?>(null)
        private set

    init {
        // Insere os mecânicos padrão se a base de dados estiver vazia
        viewModelScope.launch {
            if (dao.getCount() == 0) {
                dao.insert(LoginMecanicoInfo(nome = "José Mateus", numero = "967123097", email = "mecanico.josematheus@mecamoveis.pt", contacto = "976 555 647", pass = "meca123"))
                dao.insert(LoginMecanicoInfo(nome = "João Meireles", numero = "907635645", email = "mecanico.joaomeireles@mecamoveis.pt", contacto = "973 937 141", pass = "meca123"))
            }
        }
    }

    fun verificarLoginMecanico(email: String, pass: String, onResultado: (LoginMecanicoInfo?) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            errorMessage.value = "Por favor, preencha todos os campos."
            onResultado(null)
            return
        }
        viewModelScope.launch {
            val mecanico = dao.verifyLogin(email, pass)
            if (mecanico != null) {
                errorMessage.value = null
                onResultado(mecanico)
            } else {
                errorMessage.value = "Dados de mecânico incorretos."
                onResultado(null)
            }
        }
    }
}

