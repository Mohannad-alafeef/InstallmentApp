package dev.kkarot.installmentapp.database.repos

import dev.kkarot.installmentapp.database.dao.CustomerDao
import dev.kkarot.installmentapp.database.models.CustomerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CustomerRepo @Inject constructor(private val customerDao: CustomerDao) {

    suspend fun insertCustomer(info: CustomerInfo): Long =
        withContext(Dispatchers.IO) { customerDao.insertCustomer(info) }

    suspend fun customersInfo(): List<CustomerInfo> =
        withContext(Dispatchers.IO) { customerDao.getCustomersInfo() }

    suspend fun deleteCustomer(id: Long) = withContext(Dispatchers.IO) {
        customerDao.deleteCustomer(id)
    }
}