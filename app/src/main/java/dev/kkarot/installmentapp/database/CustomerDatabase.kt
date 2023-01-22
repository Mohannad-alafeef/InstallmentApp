package dev.kkarot.installmentapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.database.models.InstallmentInfo

@Database(entities = [CustomerInfo::class,InstallmentInfo::class], version = 1)
abstract class CustomerDatabase:RoomDatabase() {
    abstract fun customerDao():CustomerDao
    abstract fun installmentDao():InstallmentDao
}