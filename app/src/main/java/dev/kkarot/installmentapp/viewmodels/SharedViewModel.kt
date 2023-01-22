package dev.kkarot.installmentapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kkarot.installmentapp.database.CustomerRepo
import dev.kkarot.installmentapp.database.InstallmentRepo
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.database.models.PaymentInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val customerRepo: CustomerRepo,
    private val installmentRepo: InstallmentRepo
) : ViewModel() {

    private val _infoLiveData: MutableLiveData<List<CustomerInfo>> = MutableLiveData()
    val infoLiveData: LiveData<List<CustomerInfo>> = _infoLiveData

    private val _customerInstallments: MutableLiveData<List<InstallmentInfo>> = MutableLiveData()
    val customerInstallments: LiveData<List<InstallmentInfo>> = _customerInstallments

    private val _customerInstallList: MutableLiveData<List<PaymentInfo>> = MutableLiveData()
    val customerInstallmentList: LiveData<List<PaymentInfo>> = _customerInstallList

    private val _isDone: MutableLiveData<Boolean> = MutableLiveData()
    val isDone: LiveData<Boolean> = _isDone


    fun onActivity() {
        getCustomersInfo()
    }

    fun addCustomer(info: CustomerInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepo.insertCustomer(info)
            val cInfo = customerRepo.customersInfo()
            withContext(Dispatchers.Main) {
                _infoLiveData.value = cInfo
            }
        }
    }

    private fun getCustomersInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val cInfo = customerRepo.customersInfo()
            withContext(Dispatchers.Main) {
                _infoLiveData.value = cInfo
            }
        }
    }

    fun getCustomerInstallments(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val iForId = installmentRepo.getInstallmentsForID(id)
            withContext(Dispatchers.Main) {
                _customerInstallments.value = iForId
            }
        }
    }

    fun addInstallmentForCustomer(
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
        total: Float,
        doOnComplete:()->Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val insertJob = async {
               installmentRepo.insert(
                    customerId,
                    iTitle,
                    payment,
                    rem,
                    sDate,
                    eDate,
                    pType,
                    period,
                    oPrice,
                    profit,
                    pRate,
                    total
                )
            }
            if (insertJob.await()>0){
                withContext(Dispatchers.Main){
                    doOnComplete.invoke()
                }
            }


        }
    }


}