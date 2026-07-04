package com.example.projetomecamoveis.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orcamentos")
data class OrcamentoInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clienteId: Int,
    val veiculoId: Int,
    val reparacaoId: Int?, // ID da reparação associada (opcional)
    val titulo: String,
    val valor: String
)
