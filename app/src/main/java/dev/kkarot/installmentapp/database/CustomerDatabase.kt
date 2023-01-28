package dev.kkarot.installmentapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.kkarot.installmentapp.database.dao.CustomerDao
import dev.kkarot.installmentapp.database.dao.InstallmentDao
import dev.kkarot.installmentapp.database.dao.PaymentDao
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.database.models.PaymentInfo

@Database(entities = [CustomerInfo::class,InstallmentInfo::class,PaymentInfo::class], version = 1)
abstract class CustomerDatabase:RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun installmentDao(): InstallmentDao
    abstract fun paymentDao():PaymentDao
}