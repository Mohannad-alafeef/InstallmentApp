package dev.kkarot.installmentapp.views.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.databinding.InstBottomSheetBinding

class InstallmentSheet(
    private val info: InstallmentInfo,
    private val pos: Int,
    private val onDelete: (InstallmentInfo, Int) -> Unit,
    val onPaymentReceived: (InstallmentInfo) -> Unit
):BottomSheetDialogFragment() {

    private lateinit var binding:InstBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InstBottomSheetBinding.inflate(inflater,container,false)

        binding.apply {
            deleteInst.setOnClickListener {
                onDelete.invoke(info,pos)
                dismiss()
            }
            cancel.setOnClickListener {
                dismiss()
            }
            addReceived.setOnClickListener {
                onPaymentReceived.invoke(info)
                dismiss()
            }
        }

        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    companion object{
        const val TAG = "---InstallmentSheet"
    }

}