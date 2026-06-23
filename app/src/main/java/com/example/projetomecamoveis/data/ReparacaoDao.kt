package com.example.projetomecamoveis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projetomecamoveis.model.ReparacaoInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface ReparacaoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reparacao: ReparacaoInfo)

    @Query("SELECT * FROM reparacoes WHERE clienteId = :clienteId AND estado = 'Reparação Concluída' ORDER BY id DESC")
    fun getReparacoesByCliente(clienteId: Int): Flow<List<ReparacaoInfo>>

    @Query("SELECT * FROM reparacoes WHERE matricula = :matricula ORDER BY id DESC LIMIT 1")
    fun getUltimaReparacaoPorVeiculo(matricula: String): Flow<ReparacaoInfo?>
}
