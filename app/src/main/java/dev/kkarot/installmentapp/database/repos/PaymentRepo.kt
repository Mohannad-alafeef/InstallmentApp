package dev.kkarot.installmentapp.database.repos

import dev.kkarot.installmentapp.cons.InstallmentType
import dev.kkarot.installmentapp.cons.PaymentOpt
import dev.kkarot.installmentapp.database.dao.PaymentDao
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.database.models.PaymentInfo
import kotlinx.coroutines.*
import java.util.Calendar
import javax.inject.Inject

class PaymentRepo @Inject constructor(private val paymentDao: PaymentDao) {

    suspend fun insertPayments(info: InstallmentInfo, paymentOpt: PaymentOpt): List<Long> = withContext(Dispatchers.IO) {
        val list = ArrayList<PaymentInfo>()
        val ca = Calendar.getInstance()
        ca.time = info.startDate!!
        for (p in 1..info.period) {
            when (info.paymentType) {
                InstallmentType.Annually.name -> {
                    ca.add(Calendar.YEAR, 1)
                }
                InstallmentType.Monthly.name -> {
                    ca.add(Calendar.MONTH, 1)

                }
                InstallmentType.Weekly.name -> {
                    ca.add(Calendar.WEEK_OF_YEAR, 1)
                }
            }
            var payment = info.payment
           if (paymentOpt == PaymentOpt.FirstPayment && p == 1 ){
               payment += info.reminder
           }
            if (paymentOpt == PaymentOpt.LastPayment && p == info.period ){
                payment += info.reminder
            }
            if (paymentOpt == PaymentOpt.Fraction ){
                payment = info.payment
            }
            list.add(
                PaymentInfo(
                    0,
                    info.installmentId,
                    payment,
                    ca.time,
                    false,
                    isDue = false
                )
            )
        }
        paymentDao.insertPayments(list)

    }

    suspend fun getPayments(installmentId: Long) =
        withContext(Dispatchers.IO) { paymentDao.getPayments(installmentId) }

    suspend fun deletePayments(instId: Long) = withContext(Dispatchers.IO) {
        paymentDao.deletePayments(instId)
    }

    suspend fun updatePayment(info: PaymentInfo) = withContext(Dispatchers.IO) {
        paymentDao.updatePayment(info)
    }
}