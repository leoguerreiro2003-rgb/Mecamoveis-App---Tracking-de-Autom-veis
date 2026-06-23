package com.example.projetomecamoveis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projetomecamoveis.model.LoginMecanicoInfo

@Dao
interface LoginMecanicoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mecanico: LoginMecanicoInfo)

    @Query("SELECT * FROM mecanicos WHERE email = :email AND pass = :pass LIMIT 1")
    suspend fun verifyLogin(email: String, pass: String): LoginMecanicoInfo?

    @Query("SELECT COUNT(*) FROM mecanicos")
    suspend fun getCount(): Int
}