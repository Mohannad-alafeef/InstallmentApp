package dev.kkarot.installmentapp.database

import dev.kkarot.installmentapp.database.models.CustomerInfo
import javax.inject.Inject

class CustomerRepo @Inject constructor(private val customerDao: CustomerDao) {

    suspend fun insertCustomer(info: CustomerInfo):Long = customerDao.insertCustomer(info)
    suspend fun customersInfo():List<CustomerInfo> = customerDao.getCustomersInfo()
}