package com.example.projetomecamoveis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projetomecamoveis.model.LoginClienteInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface LoginClienteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cliente: LoginClienteInfo)

    @Query("SELECT * FROM clientes WHERE LOWER(email) = LOWER(:email) AND pass = :pass LIMIT 1")
    suspend fun verifyLogin(email: String, pass: String): LoginClienteInfo?

    @Query("SELECT * FROM clientes")
    fun getAllClientes(): Flow<List<LoginClienteInfo>>

    @Query("SELECT * FROM clientes WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getClienteByEmail(email: String): LoginClienteInfo?

    @Query("SELECT * FROM clientes WHERE number = :number LIMIT 1")
    suspend fun getClienteByNumber(number: Int): LoginClienteInfo?

    @Query("SELECT * FROM clientes WHERE pass = :pass LIMIT 1")
    suspend fun getClienteByPassword(pass: String): LoginClienteInfo?
}
