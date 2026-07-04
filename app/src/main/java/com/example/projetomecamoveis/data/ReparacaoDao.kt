package com.example.projetomecamoveis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projetomecamoveis.model.ReparacaoInfo
import com.example.projetomecamoveis.model.ReparacaoComCliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ReparacaoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reparacao: ReparacaoInfo)

    @Query("SELECT * FROM reparacoes WHERE clienteId = :clienteId AND UPPER(estado) LIKE '%CONCLUÍDA%' ORDER BY id DESC")
    fun getReparacoesByCliente(clienteId: Int): Flow<List<ReparacaoInfo>>

    @Query("""
        SELECT r.*, IFNULL(c.name, 'Cliente Desconhecido') as clienteNome 
        FROM reparacoes r 
        LEFT JOIN clientes c ON r.clienteId = c.id 
        WHERE UPPER(r.estado) LIKE '%CONCLUÍDA%'
        ORDER BY r.id DESC
    """)
    fun getAllReparacoesConcluidasComCliente(): Flow<List<ReparacaoComCliente>>

    @Query("SELECT * FROM reparacoes WHERE clienteId = :clienteId AND estado != 'Reparação Concluída' ORDER BY id DESC")
    fun getReparacoesAtivasByCliente(clienteId: Int): Flow<List<ReparacaoInfo>>

    @Query("SELECT * FROM reparacoes WHERE matricula = :matricula ORDER BY id DESC LIMIT 1")
    fun getUltimaReparacaoPorVeiculo(matricula: String): Flow<ReparacaoInfo?>

    @Query("UPDATE reparacoes SET estado = :novoEstado WHERE id = (SELECT id FROM reparacoes WHERE matricula = :matricula ORDER BY id DESC LIMIT 1)")
    suspend fun atualizarEstadoUltimaReparacao(matricula: String, novoEstado: String)

    @androidx.room.Delete
    suspend fun delete(reparacao: ReparacaoInfo)
}
