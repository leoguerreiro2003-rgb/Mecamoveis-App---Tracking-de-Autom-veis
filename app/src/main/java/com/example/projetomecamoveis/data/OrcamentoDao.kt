package com.example.projetomecamoveis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projetomecamoveis.model.OrcamentoInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface OrcamentoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orcamento: OrcamentoInfo)

    @Update
    suspend fun update(orcamento: OrcamentoInfo)

    @Query("SELECT * FROM orcamentos WHERE clienteId = :clienteId ORDER BY id DESC")
    fun getOrcamentosByCliente(clienteId: Int): Flow<List<OrcamentoInfo>>

    @Query("SELECT * FROM orcamentos WHERE id = :id")
    suspend fun getOrcamentoById(id: Int): OrcamentoInfo?

    @Query("SELECT * FROM orcamentos WHERE veiculoId = :veiculoId ORDER BY id DESC")
    fun getOrcamentosByVeiculo(veiculoId: Int): Flow<List<OrcamentoInfo>>

    @Query("""
        SELECT o.*, v.marca, v.modelo, v.matricula, v.ano, r.numeroReparacao 
        FROM orcamentos o 
        INNER JOIN veiculos v ON o.veiculoId = v.id
        LEFT JOIN reparacoes r ON o.reparacaoId = r.id
        ORDER BY o.id DESC
    """)
    fun getAllOrcamentosDetalhados(): Flow<List<com.example.projetomecamoveis.model.OrcamentoDetalhado>>

    @Query("""
        SELECT o.*, v.marca, v.modelo, v.matricula, v.ano, r.numeroReparacao 
        FROM orcamentos o 
        INNER JOIN veiculos v ON o.veiculoId = v.id
        LEFT JOIN reparacoes r ON o.reparacaoId = r.id
        WHERE o.clienteId = :clienteId
        ORDER BY o.id DESC
    """)
    fun getOrcamentosDetalhadosByCliente(clienteId: Int): Flow<List<com.example.projetomecamoveis.model.OrcamentoDetalhado>>
}
