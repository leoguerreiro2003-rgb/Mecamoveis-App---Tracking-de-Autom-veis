package com.example.projetomecamoveis.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetomecamoveis.model.LoginMecanicoInfo

class ContactosViewModel : ViewModel() {

    // Agora usamos LoginMecanicoInfo
    val mecanicos: List<LoginMecanicoInfo> = listOf(
        LoginMecanicoInfo(
            id = 1,
            nome = "José Mateus",
            numero = "967123097",
            email = "mecanico.josematheus@mecamoveis.pt",
            contacto = "976 555 647",
            pass = "meca123"
        ),
        LoginMecanicoInfo(
            id = 2,
            nome = "João Meireles",
            numero = "907635645",
            email = "mecanico.joaomeireles@mecamoveis.pt",
            contacto = "973 937 141",
            pass = "meca123"
        )
    )
}