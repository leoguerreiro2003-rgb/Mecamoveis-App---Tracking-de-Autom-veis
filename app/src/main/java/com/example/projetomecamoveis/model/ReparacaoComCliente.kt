package com.example.projetomecamoveis.model

import androidx.room.Embedded

data class ReparacaoComCliente(
    @Embedded val reparacao: ReparacaoInfo,
    val clienteNome: String
)
