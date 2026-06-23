package com.example.projetomecamoveis.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "veiculos")
data class VeiculoInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clienteId: Int, // Associado ao ID do cliente
    val marca: String,
    val modelo: String,
    val matricula: String,
    val ano: String,
    val kms: String,
    val emReparacao: Boolean = false,
    val ultimoEstado: String = "",
    val dataIniciada: String = "",
    val dataEmReparacao: String = "",
    val dataRevisao: String = "",
    val dataConcluida: String = ""
)
