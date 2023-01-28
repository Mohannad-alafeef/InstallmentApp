package dev.kkarot.installmentapp.ui.fragments.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.kkarot.installmentapp.adapters.CustomerInstallmentAdapter
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.databinding.FragmentCustomerDetailsBinding
import dev.kkarot.installmentapp.viewmodels.SharedData
import dev.kkarot.installmentapp.viewmodels.SharedDataBase
import dev.kkarot.installmentapp.views.bottomsheets.InstallmentSheet
import java.text.SimpleDateFormat
import java.util.*



class CustomerInfoFragment : Fragment() {

    companion object{
        private const val TAG = "---CDetailsFragment"

    }
    private val sharedDataBase: SharedDataBase by activityViewModels()
    private val sharedData: SharedData by activityViewModels()

    private lateinit var instList: ArrayList<InstallmentInfo>

    private lateinit var installmentAdapter: CustomerInstallmentAdapter



    private lateinit var binding: FragmentCustomerDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerDetailsBinding.inflate(inflater, container, false)

        sharedData.selectedCustomer.observe(viewLifecycleOwner){ info ->
            sharedDataBase.getCustomerInstallments(info.customerId)
            binding.apply {
                customerName.text = info.customerName
                address.text = info.customerAddress
                phoneNumber.text = info.customerPhone
                date.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(info.addDate)
                addInstallment.setOnClickListener {
                    CustomerInfoFragmentDirections.actionCustomerInfoFragmentToAddInstallmentFragment().let { navDirection ->
                        findNavController().navigate(navDirection)
                    }
                }
            }
        }




        sharedDataBase.customerInstallments.observe(viewLifecycleOwner) { installmentInfoList ->
            binding.apply {
                installmentCount.text = installmentInfoList.size.toString()
            }
            instList = installmentInfoList
            iniRecView(instList)
        }




        return binding.root
    }

    private fun iniRecView(installmentInfoList: List<InstallmentInfo>) {
        binding.installmentRec.apply {
            setHasFixedSize(true)
            installmentAdapter = CustomerInstallmentAdapter(installmentInfoList,onLongClick,onClick)
            adapter = installmentAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }
    private val onClick:(Long) ->Unit = {installmentId ->
        sharedData.setInstallmentId(installmentId)
        CustomerInfoFragmentDirections.actionCustomerInfoFragmentToPaymentsFragment().let { direction ->
            findNavController().navigate(direction)
        }
    }
    private val onLongClick:(InstallmentInfo,Int)->Unit ={ installmentInfo, pos ->
        val sheet = InstallmentSheet(installmentInfo,pos,onDelete)
        sheet.show(childFragmentManager,InstallmentSheet.TAG)
    }
    private val onDelete:(InstallmentInfo,Int)->Unit = { info , pos ->
        sharedDataBase.deleteInstallment(info){
            instList.removeAt(pos)
            installmentAdapter.remove(pos)
            Toast.makeText(requireContext(), "Delete Complete", Toast.LENGTH_SHORT).show()
        }
    }


}