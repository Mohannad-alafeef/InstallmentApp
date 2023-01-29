package dev.kkarot.installmentapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.kkarot.installmentapp.database.models.InstallmentInfo


@Dao
interface InstallmentDao {
    @Insert
    suspend fun insert(info:InstallmentInfo):Long

    @Query("SELECT * FROM InstallmentTable WHERE customerId = :id")
    suspend fun getInstallments(id:Long):List<InstallmentInfo>

    @Query("DELETE FROM InstallmentTable WHERE customerId = :cId")
    suspend fun deleteCustomerInst(cId:Long)

    @Query("DELETE FROM InstallmentTable WHERE installmentId = :id")
    suspend fun deleteInstallment(id:Long)

    @Query("SELECT * FROM InstallmentTable WHERE installmentId = :id")
    suspend fun getInstallment(id: Long):InstallmentInfo

    @Update
    suspend fun updateInstallment(info: InstallmentInfo)
}