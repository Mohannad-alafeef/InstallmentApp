package dev.kkarot.installmentapp.database.repos

import dev.kkarot.installmentapp.cons.DeductType
import dev.kkarot.installmentapp.database.dao.InstallmentDao
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InstallmentRepo @Inject constructor(private val dao: InstallmentDao) {

    suspend fun insert(
        info: InstallmentInfo
    ): Long = dao.insert(info)

    suspend fun getInstallmentsForID(id: Long): List<InstallmentInfo> =
        withContext(Dispatchers.IO) { dao.getInstallments(id) }

    suspend fun deleteCustomerInst(cId: Long) =
        withContext(Dispatchers.IO) {
            dao.deleteCustomerInst(cId)
        }

    suspend fun deleteInstallment(id: Long) =
        withContext(Dispatchers.IO) {
            dao.deleteInstallment(id)
        }

    suspend fun updateInstallment(id: Long, update: Int) = withContext(Dispatchers.IO){
       val installment = dao.getInstallment(id)
        installment.received +=update
        dao.updateInstallment(installment)
    }
    suspend fun updateInstallment(info: InstallmentInfo) = dao.updateInstallment(info)




}