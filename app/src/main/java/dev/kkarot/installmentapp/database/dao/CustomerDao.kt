package dev.kkarot.installmentapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.kkarot.installmentapp.database.models.CustomerInfo

@Dao
interface CustomerDao {

    @Insert
    suspend fun insertCustomer(info: CustomerInfo):Long

    @Query("Select * From CustomerTable")
    suspend fun getCustomersInfo():List<CustomerInfo>

    @Query("DELETE FROM CustomerTable WHERE customerId = :id")
    suspend fun deleteCustomer(id:Long)
}