package dev.kkarot.installmentapp.views.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.databinding.CustomerBottomSheetBinding
import dev.kkarot.installmentapp.viewmodels.SharedData
import dev.kkarot.installmentapp.viewmodels.SharedDataBase

class CustomerBottomSheet(val info: CustomerInfo, val pos: Int, val onDelete: (Int) -> Unit) : BottomSheetDialogFragment() {



    private lateinit var binding: CustomerBottomSheetBinding

    private val sharedData:SharedData by activityViewModels()
    private val sharedDataBase:SharedDataBase by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CustomerBottomSheetBinding.inflate(layoutInflater, container, false)


        binding.apply {

            deleteCustomer.setOnClickListener {
                sharedDataBase.deleteCustomer(info){
                    onDelete.invoke(pos)
                }
                dismiss()
            }

            editCustomer.setOnClickListener {
//                HomeFragmentDirections.actionHomeFragmentToAddCustomerFragment()
//                    .let { navDirections ->
//                        findNavController().navigate(navDirections)
//                        dismiss()
//                    }
//                sharedDataBase.edit {
//                    Toast.makeText(requireContext(), "mohannad", Toast.LENGTH_SHORT).show()
//                }
            }
        }



        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    companion object{
        const val TAG = "---CustomerSheet"
    }
}