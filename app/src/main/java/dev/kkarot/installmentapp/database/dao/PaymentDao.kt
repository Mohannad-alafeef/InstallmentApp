package dev.kkarot.installmentapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.kkarot.installmentapp.database.models.PaymentInfo


@Dao
interface PaymentDao {

    @Insert
    suspend fun insertPayments(list: List<PaymentInfo>):List<Long>

    @Query("SELECT * FROM PaymentTable WHERE installmentId = :installmentId")
    suspend fun getPayments(installmentId:Long):List<PaymentInfo>

    @Query("DELETE FROM PaymentTable WHERE installmentId = :instId")
    suspend fun deletePayments(instId:Long)

    @Update
    suspend fun updatePayment(info: PaymentInfo)

    @Update
    suspend fun updatePayments(paymentInfoList: List<PaymentInfo>)
}