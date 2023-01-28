package dev.kkarot.installmentapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kkarot.installmentapp.cons.PaymentOpt
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.database.models.PaymentInfo
import dev.kkarot.installmentapp.database.repos.CustomerRepo
import dev.kkarot.installmentapp.database.repos.InstallmentRepo
import dev.kkarot.installmentapp.database.repos.PaymentRepo
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class SharedDataBase @Inject constructor(
    private val customerRepo: CustomerRepo,
    private val installmentRepo: InstallmentRepo,
    private val paymentRepo: PaymentRepo
) : ViewModel() {

    private val _infoLiveData: MutableLiveData<ArrayList<CustomerInfo>> = MutableLiveData()
    val infoLiveData: LiveData<ArrayList<CustomerInfo>> = _infoLiveData

    private val _customerInstallments: MutableLiveData<ArrayList<InstallmentInfo>> = MutableLiveData()
    val customerInstallments: LiveData<ArrayList<InstallmentInfo>> = _customerInstallments

    private val _instPaymentList: MutableLiveData<ArrayList<PaymentInfo>> = MutableLiveData()
    val instPaymentList: LiveData<ArrayList<PaymentInfo>> = _instPaymentList

    private val _isDone: MutableLiveData<Boolean> = MutableLiveData()
    val isDone: LiveData<Boolean> = _isDone


    fun onActivity() {
        getCustomersInfo()
    }

    fun addCustomer(info: CustomerInfo) {
        viewModelScope.launch {
            customerRepo.insertCustomer(info)
            val cInfo = customerRepo.customersInfo()
            _infoLiveData.value = cInfo as ArrayList<CustomerInfo>

        }
    }

    private fun getCustomersInfo() {
        viewModelScope.launch {
            val cInfo = customerRepo.customersInfo()

            _infoLiveData.value = cInfo as ArrayList<CustomerInfo>

        }
    }

    fun getCustomerInstallments(id: Long) {
        viewModelScope.launch {
            val iForId = installmentRepo.getInstallmentsForID(id)
            _customerInstallments.value = iForId as ArrayList<InstallmentInfo>

        }
    }


    fun insertInstallment(
        installmentInfo: InstallmentInfo,
        paymentOpt: PaymentOpt,
        function: () -> Unit
    ) {
        viewModelScope.launch {
            val installmentId = installmentRepo.insert(installmentInfo)

            if (installmentId > 0) {

                installmentInfo.installmentId = installmentId
                val paymentIds = paymentRepo.insertPayments(installmentInfo,paymentOpt)

                if (paymentIds.size == installmentInfo.period) {
                    function.invoke()
                }
            }
        }
    }

    fun getInstPayments(id:Long){
        viewModelScope.launch {
           _instPaymentList.value =  paymentRepo.getPayments(id) as ArrayList<PaymentInfo>
        }
    }

    fun deleteCustomer(info: CustomerInfo, onComplete: () -> Unit) {
        viewModelScope.launch {

            customerRepo.deleteCustomer(info.customerId)

            val list = installmentRepo.getInstallmentsForID(info.customerId)

            installmentRepo.deleteCustomerInst(info.customerId)

            list.forEach { installmentInfo ->
                paymentRepo.deletePayments(installmentInfo.installmentId)
            }

            onComplete.invoke()


        }
    }

    fun deleteInstallment(info: InstallmentInfo,onComplete: () -> Unit){
        viewModelScope.launch {
            installmentRepo.deleteInstallment(info.installmentId)
            paymentRepo.deletePayments(info.installmentId)
            onComplete.invoke()
        }
    }

    fun updatePayment(info: PaymentInfo) {
        viewModelScope.launch {
            paymentRepo.updatePayment(info)
        }
    }


}