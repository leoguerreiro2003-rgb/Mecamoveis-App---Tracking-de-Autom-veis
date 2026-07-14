package com.example.projetomecamoveis.model

import androidx.room.Embedded

data class OrcamentoDetalhado(
    @Embedded val orcamento: OrcamentoInfo,
    val marca: String,
    val modelo: String,
    val matricula: String,
    val ano: String,
    val numeroReparacao: String?
)
