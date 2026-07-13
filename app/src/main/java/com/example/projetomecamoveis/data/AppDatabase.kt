package com.example.projetomecamoveis.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projetomecamoveis.model.LoginClienteInfo
import com.example.projetomecamoveis.model.LoginMecanicoInfo
import com.example.projetomecamoveis.model.VeiculoInfo
import com.example.projetomecamoveis.model.ReparacaoInfo
import com.example.projetomecamoveis.model.OrcamentoInfo

@Database(entities = [LoginClienteInfo::class, LoginMecanicoInfo::class, VeiculoInfo::class, ReparacaoInfo::class, OrcamentoInfo::class], version = 10, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loginClienteDao(): LoginClienteDao
    abstract fun loginMecanicoDao(): LoginMecanicoDao
    abstract fun veiculoDao(): VeiculoDao
    abstract fun reparacaoDao(): ReparacaoDao
    abstract fun orcamentoDao(): OrcamentoDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "mecamoveis_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
