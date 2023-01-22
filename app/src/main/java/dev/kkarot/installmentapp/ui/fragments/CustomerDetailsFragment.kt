package dev.kkarot.installmentapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dev.kkarot.installmentapp.adapters.CustomerInstallmentAdapter
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.databinding.FragmentCustomerDetailsBinding
import dev.kkarot.installmentapp.viewmodels.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "---CDetailsFragment"

class CustomerDetailsFragment : Fragment() {

    private val args: CustomerDetailsFragmentArgs by navArgs()
    private lateinit var info: CustomerInfo

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info = args.customerInfo
    }

    private lateinit var binding: FragmentCustomerDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerDetailsBinding.inflate(inflater, container, false)

        sharedViewModel.getCustomerInstallments(info.customerId)

        sharedViewModel.customerInstallments.observe(viewLifecycleOwner) {
            binding.apply {
                installmentCount.text = it.size.toString()
            }
            iniRecView(it)
        }
        binding.apply {
            customerName.text = info.customerName
            address.text = info.customerAddress
            phoneNumber.text = info.customerPhone
            date.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(info.addDate)
            addInstallment.setOnClickListener {
                CustomerDetailsFragmentDirections.actionCustomerDetailsFragmentToAddInstallmentFragment(info).let { navDirection ->
                    findNavController().navigate(navDirection)
                }
            }
        }



        return binding.root
    }

    private fun iniRecView(installmentInfoList: List<InstallmentInfo>) {
        binding.installmentRec.apply {
            setHasFixedSize(true)
            val installmentAdapter = CustomerInstallmentAdapter(installmentInfoList)
            adapter = installmentAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }


}