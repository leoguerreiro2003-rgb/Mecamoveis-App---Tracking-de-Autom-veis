package com.example.projetomecamoveis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projetomecamoveis.model.VeiculoInfo
import com.example.projetomecamoveis.model.VeiculoComCliente
import kotlinx.coroutines.flow.Flow

@Dao
interface VeiculoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(veiculo: VeiculoInfo)

    @Query("SELECT * FROM veiculos WHERE UPPER(matricula) = UPPER(:matricula) LIMIT 1")
    suspend fun getVeiculoByMatricula(matricula: String): VeiculoInfo?

    @Query("SELECT * FROM veiculos WHERE clienteId = :clienteId")
    fun getVeiculosByCliente(clienteId: Int): Flow<List<VeiculoInfo>>

    @Query("SELECT * FROM veiculos WHERE clienteId = :clienteId")
    suspend fun getVeiculosByClienteList(clienteId: Int): List<VeiculoInfo>

    @Query("SELECT * FROM veiculos WHERE id = :veiculoId LIMIT 1")
    suspend fun getVeiculoById(veiculoId: Int): VeiculoInfo?

    @Query("""
        SELECT v.*, c.name as clienteNome 
        FROM veiculos v 
        INNER JOIN clientes c ON v.clienteId = c.id 
        WHERE v.emReparacao = 1
    """)
    fun getVeiculosEmReparacaoComCliente(): Flow<List<VeiculoComCliente>>

    @Query("""
        SELECT v.*, c.name as clienteNome 
        FROM veiculos v 
        INNER JOIN clientes c ON v.clienteId = c.id 
        ORDER BY v.id DESC
    """)
    fun getAllVeiculosComCliente(): Flow<List<VeiculoComCliente>>

    @Query("UPDATE veiculos SET emReparacao = :emReparacao, ultimoEstado = :estado WHERE id = :veiculoId")
    suspend fun atualizarEstadoReparacao(veiculoId: Int, emReparacao: Boolean, estado: String)

    @Query("UPDATE veiculos SET dataIniciada = :data WHERE id = :veiculoId")
    suspend fun atualizarDataIniciada(veiculoId: Int, data: String)

    @Query("UPDATE veiculos SET dataEmReparacao = :data WHERE id = :veiculoId")
    suspend fun atualizarDataEmReparacao(veiculoId: Int, data: String)

    @Query("UPDATE veiculos SET dataRevisao = :data WHERE id = :veiculoId")
    suspend fun atualizarDataRevisao(veiculoId: Int, data: String)

    @Query("UPDATE veiculos SET dataConcluida = :data WHERE id = :veiculoId")
    suspend fun atualizarDataConcluida(veiculoId: Int, data: String)

    @androidx.room.Update
    suspend fun update(veiculo: VeiculoInfo)

    @androidx.room.Delete
    suspend fun delete(veiculo: VeiculoInfo)
}
