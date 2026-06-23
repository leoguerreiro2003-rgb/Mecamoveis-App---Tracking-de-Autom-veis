package com.example.projetomecamoveis.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reparacoes")
data class ReparacaoInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clienteId: Int,
    val dataHora: String,
    val titulo: String,
    val marca: String,
    val modelo: String,
    val matricula: String,
    val ano: String,
    val pecasUtilizadas: String,
    val numeroReparacao: String,
    val valor: String,
    val estado: String
)
