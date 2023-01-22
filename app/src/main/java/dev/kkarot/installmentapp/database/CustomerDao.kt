package dev.kkarot.installmentapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.kkarot.installmentapp.database.models.CustomerInfo

@Dao
interface CustomerDao {

    @Insert
    suspend fun insertCustomer(info: CustomerInfo):Long

    @Query("Select * From CustomerInfo")
    suspend fun getCustomersInfo():List<CustomerInfo>

}