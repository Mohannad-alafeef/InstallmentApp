package dev.kkarot.installmentapp.ui.fragments.add

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.databinding.FragmentAddCustomerBinding
import dev.kkarot.installmentapp.viewmodels.SharedDataBase
import java.util.*

private const val TAG = "---AddCustomerFragment"
class AddCustomerFragment : Fragment() {

    private lateinit var binding: FragmentAddCustomerBinding
    private val sharedDataBase: SharedDataBase by activityViewModels()

    var isError = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCustomerBinding.inflate(inflater, container, false)



        binding.apply {

//            customerName.editText?.filters = arrayOf(LengthFilter(50))
//            customerPhone.editText?.filters = arrayOf(LengthFilter(20))
//            customerAddress.editText?.filters = arrayOf(LengthFilter(50))


            toolbar.setupWithNavController(findNavController())
            addCustomer.setOnClickListener {
                isError = false
                if (customerName.editText?.text.isNullOrEmpty()) {
                    isError = true
                    customerName.error = getString(R.string.required_field)
                }else
                    customerName.error = null

                if (customerPhone.editText?.text.isNullOrEmpty()) {
                    isError = true
                    customerPhone.error = getString(R.string.required_field)
                }else
                    customerPhone.error = null

                if (isError)
                    return@setOnClickListener
                AddCustomerFragmentDirections.actionAddCustomerFragmentToHomeFragment().let {
                    addCustomer(
                        customerName.editText?.text?.toString()!!,
                        customerPhone.editText?.text?.toString()!!,
                        customerAddress.editText?.text?.toString()?:""
                    )
                    findNavController().navigate(it)
                }
            }
        }


        return binding.root
    }


    private fun addCustomer(name: String, phone: String, address: String): CustomerInfo {
        Log.d(TAG, "addCustomer: yes ")
        val customerInfo = CustomerInfo(0, name, phone, address, Date())
        sharedDataBase.addCustomer(customerInfo)
        return customerInfo
    }

}