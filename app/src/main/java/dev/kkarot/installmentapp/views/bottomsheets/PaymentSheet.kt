package dev.kkarot.installmentapp.views.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.kkarot.installmentapp.database.models.PaymentInfo
import dev.kkarot.installmentapp.databinding.PaymentSheetBinding

class PaymentSheet(
    val info: PaymentInfo,
    private val onReceivedClick: (PaymentInfo,Int) -> Unit,
    val pos: Int
) :BottomSheetDialogFragment() {

    private lateinit var binding:PaymentSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PaymentSheetBinding.inflate(inflater,container,false)

        binding.apply {
            cancel.setOnClickListener {
                dismiss()
            }
            receivedBtn.setOnClickListener {
                info.isPaid = true
                onReceivedClick.invoke(info,pos)
                dismiss()
            }
        }

        return binding.root
    }
    companion object{
        const val TAG = "---PaymentSheet"
    }
}