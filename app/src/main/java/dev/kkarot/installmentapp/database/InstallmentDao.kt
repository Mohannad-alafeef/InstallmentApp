package dev.kkarot.installmentapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.kkarot.installmentapp.database.models.InstallmentInfo


@Dao
interface InstallmentDao {
    @Insert
    suspend fun insert(info:InstallmentInfo):Long

    @Query("SELECT * FROM InstallmentInfo WHERE customer_id = :id")
    suspend fun getInstallments(id:Long):List<InstallmentInfo>
}