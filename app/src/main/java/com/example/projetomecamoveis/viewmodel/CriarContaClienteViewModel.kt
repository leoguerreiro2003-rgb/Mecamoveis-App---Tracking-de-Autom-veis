package com.example.projetomecamoveis.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomecamoveis.data.AppDatabase
import com.example.projetomecamoveis.model.LoginClienteInfo
import com.example.projetomecamoveis.model.LoginMecanicoInfo
import kotlinx.coroutines.launch

class CriarContaClienteViewModel(application: Application) : AndroidViewModel(application) {
    
    private val dao = AppDatabase.getDatabase(application).loginClienteDao()

    private val _list = mutableStateListOf<LoginMecanicoInfo>(
        LoginMecanicoInfo(
            1,
            "José Mateus",
            "967123097",
            "mecanico.josematheus@mecamoveis.pt",
            "976 555 647",
            "meca123"
        ),
        LoginMecanicoInfo(2, "João Meireles", "907635645", "mecanico.joaomeireles@mecamoveis.pt", "973 937 141", "meca123")
    )

    fun verificarLoginMecanico(email: String, pass: String, onResultado: (LoginMecanicoInfo?) -> Unit) {
        val mecanico = _list.find { it.email == email && it.pass == pass }
        if (mecanico != null) {
            errorMessage.value = null
            onResultado(mecanico)
        } else {
            errorMessage.value = "Dados de mecânico incorretos."
            onResultado(null)
        }
    }

    // Estado de erro geral (para campos vazios ou inconsistências)
    var errorMessage = mutableStateOf<String?>(null)
        private set

    // Estados de erro específicos por campo
    var emailError = mutableStateOf<String?>(null)
        private set
    var contactoError = mutableStateOf<String?>(null)
        private set
    var passwordError = mutableStateOf<String?>(null)
        private set

    // Estado de sucesso — a View observa isto para navegar depois do registo
    var registoSucesso = mutableStateOf(false)
        private set

    // Valida e devolve um objeto LoginClienteInfo pronto a adicionar
    // Retorna null se alguma validação falhar
    fun validarECriarCliente(
        nome: String,
        email: String,
        contacto: String,
        dataNascimento: String,
        palavraPasse: String,
        confirmarPalavraPasse: String,
        onSucesso: (LoginClienteInfo) -> Unit
    ) {
        // Limpar erros anteriores
        errorMessage.value = null
        emailError.value = null
        contactoError.value = null
        passwordError.value = null

        var localHasError = false

        // 1. Verificar se todos os campos estão preenchidos
        if (nome.isBlank() || email.isBlank() || contacto.isBlank() || dataNascimento.isBlank() || palavraPasse.isBlank() || confirmarPalavraPasse.isBlank()) {
            errorMessage.value = "Por favor, preencha todos os campos."
            localHasError = true
        }

        // 2. Validação básica de email (deve conter @ e .)
        if (email.isNotBlank() && (!email.contains("@") || !email.contains("."))) {
            emailError.value = "Email inválido."
            localHasError = true
        }

        // 3. Verificar se as passwords coincidem
        if (palavraPasse.isNotBlank() && confirmarPalavraPasse.isNotBlank() && palavraPasse != confirmarPalavraPasse) {
            passwordError.value = "As palavras-passe não coincidem."
            localHasError = true
        }

        // Se houver erros de formato ou campos vazios, paramos aqui antes de ir à BD
        if (localHasError) return

        viewModelScope.launch {
            var dbHasError = false
            
            // 4. Verificar duplicados na Base de Dados - Fazemos todos sem dar 'return' para mostrar todos de uma vez
            
            // Email
            val emailExistente = dao.getClienteByEmail(email.trim())
            if (emailExistente != null) {
                emailError.value = "esse email ja esta ser utilizado"
                dbHasError = true
            }

            // Contacto
            val contactoInt = contacto.replace(" ", "").toIntOrNull()
            if (contactoInt == null) {
                contactoError.value = "Número de contacto inválido."
                dbHasError = true
            } else {
                val numeroExistente = dao.getClienteByNumber(contactoInt)
                if (numeroExistente != null) {
                    contactoError.value = "esse numero ja esta ser utilizado"
                    dbHasError = true
                }
            }

            // Password
            val passwordExistente = dao.getClienteByPassword(palavraPasse)
            if (passwordExistente != null) {
                passwordError.value = "tente uma palavra pass diferente"
                dbHasError = true
            }

            // Só prosseguimos se não houver NENHUM erro na BD
            if (dbHasError) return@launch

            // Se tudo estiver OK
            val novoCliente = LoginClienteInfo(
                id = 0,
                name = nome,
                email = email.trim(),
                number = contactoInt!!,
                pass = palavraPasse
            )
            
            registoSucesso.value = true
            onSucesso(novoCliente)
        }
    }

    fun resetarSucesso() {
        registoSucesso.value = false
    }
}
