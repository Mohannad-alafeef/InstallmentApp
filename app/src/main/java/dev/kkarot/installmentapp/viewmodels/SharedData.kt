package dev.kkarot.installmentapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class SharedData:ViewModel() {


    private val _selectedCustomer = MutableLiveData<CustomerInfo>()
    val selectedCustomer:LiveData<CustomerInfo> = _selectedCustomer

    private val _selectedInstallment = MutableLiveData<InstallmentInfo>()
    val selectedInstallment:LiveData<InstallmentInfo> = _selectedInstallment

    private val _selectedInstallmentId = MutableLiveData<Long>()
    val selectedInstallmentId:LiveData<Long> = _selectedInstallmentId



    fun setCustomer(info:CustomerInfo){
        _selectedCustomer.value = info
    }

    fun setInstallment(info: InstallmentInfo){
        _selectedInstallment.value = info
    }

    fun setInstallmentId(id:Long){
        _selectedInstallmentId.value = id

    }



}