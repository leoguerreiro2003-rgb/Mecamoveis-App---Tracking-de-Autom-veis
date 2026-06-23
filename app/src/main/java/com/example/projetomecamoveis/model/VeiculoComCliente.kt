package com.example.projetomecamoveis.model

import androidx.room.Embedded

data class VeiculoComCliente(
    @Embedded val veiculo: VeiculoInfo,
    val clienteNome: String
)
