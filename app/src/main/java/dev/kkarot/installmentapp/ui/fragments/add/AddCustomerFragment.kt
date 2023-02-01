package dev.kkarot.installmentapp.ui.fragments.add

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.databinding.FragmentAddCustomerBinding
import dev.kkarot.installmentapp.viewmodels.SharedData
import dev.kkarot.installmentapp.viewmodels.SharedDataBase
import java.util.*

private const val TAG = "---AddCustomerFragment"

class AddCustomerFragment : Fragment() {

    private lateinit var binding: FragmentAddCustomerBinding
    private val sharedDataBase: SharedDataBase by activityViewModels()
    private val sharedData: SharedData by activityViewModels()

    private var name = ""
    private var phone = ""
    private var address = ""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCustomerBinding.inflate(inflater, container, false)



        binding.apply {

            toolbar.setupWithNavController(findNavController())
            customerName.editText?.doOnTextChanged { text, _, _, _ ->
                name = text?.toString()!!
            }
            customerPhone.editText?.doOnTextChanged { text, _, _, _ ->
                phone = text?.toString()!!
            }
            customerAddress.editText?.doOnTextChanged { text, _, _, _ ->
                address = text?.toString()!!
            }
            saveCustomer.setOnClickListener {
                if (checkError())
                    return@setOnClickListener
                addCustomer(
                    name,
                    phone,
                    address
                )
                AddCustomerFragmentDirections.actionAddCustomerFragmentToHomeFragment().let { directions ->
                    findNavController().navigate(directions)
                }
            }
            addCustomer.setOnClickListener {
                if (checkError())
                    return@setOnClickListener
                sharedData.setCustomer(CustomerInfo(0,name,phone,address))
                AddCustomerFragmentDirections.actionAddCustomerFragmentToAddInstallmentFragment().let { dire->
                    findNavController().navigate(dire)
                }
            }
        }


        return binding.root
    }

    private fun checkError(): Boolean {
        var isError = false
        binding.apply {
            if (name.isEmpty()) {
                isError = true
                customerName.error = getString(R.string.required_field)
            } else
                customerName.error = null

            if (phone.isEmpty()) {
                isError = true
                customerPhone.error = getString(R.string.required_field)
            } else
                customerPhone.error = null
        }
        return isError
    }


    private fun addCustomer(name: String, phone: String, address: String): CustomerInfo {
        val customerInfo = CustomerInfo(0, name, phone, address, Date())
        sharedDataBase.addCustomer(customerInfo)
        return customerInfo
    }

}