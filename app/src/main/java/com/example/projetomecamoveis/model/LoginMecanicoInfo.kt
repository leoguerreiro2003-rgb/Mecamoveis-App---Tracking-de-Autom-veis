package com.example.projetomecamoveis.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mecanicos")
data class LoginMecanicoInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val numero: String,
    val email: String,
    val contacto: String,
    val pass: String
)