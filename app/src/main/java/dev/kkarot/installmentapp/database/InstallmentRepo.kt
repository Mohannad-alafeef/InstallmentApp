package dev.kkarot.installmentapp.database

import dev.kkarot.installmentapp.cons.InstallmentType
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.database.models.PaymentInfo
import dev.kkarot.installmentapp.database.models.Payments
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class InstallmentRepo @Inject constructor(private val dao: InstallmentDao) {

    suspend fun insert(
        customerId: Long,
        iTitle: String,
        payment: Float,
        rem: Float,
        sDate: Long,
        eDate: Long,
        pType: String,
        period: Int,
        oPrice: Float,
        profit: Float,
        pRate: Int,
        total: Float
    ):Long {
        val list = ArrayList<PaymentInfo>()
        val ca = Calendar.getInstance()
            ca.timeInMillis = sDate
        for (x in 1..period){
            when(pType){
                InstallmentType.monthly ->{
                    ca.add(Calendar.MONTH,1)
                    list.add(
                        PaymentInfo(
                            isDue = false,
                            isPaid = false,
                            paymentDate = ca.time
                            )
                    )
                }
                InstallmentType.annually ->{
                    ca.add(Calendar.YEAR,1)
                    list.add(
                        PaymentInfo(
                            isDue = false,
                            isPaid = false,
                            paymentDate = ca.time
                        )
                    )
                }
                InstallmentType.weekly ->{
                    ca.add(Calendar.MONTH,1)
                    list.add(
                        PaymentInfo(
                            isDue = false,
                            isPaid = false,
                            paymentDate = ca.time
                        )
                    )
                }
            }
        }
        val info = InstallmentInfo(
            0, customerId, iTitle, oPrice, pRate, profit, total, Date(sDate),
            Date(eDate), pType, payment, rem,
            Payments(
                list
            )
        )
       return dao.insert(info)
    }

    suspend fun getInstallmentsForID(id: Long): List<InstallmentInfo> = dao.getInstallments(id)

}