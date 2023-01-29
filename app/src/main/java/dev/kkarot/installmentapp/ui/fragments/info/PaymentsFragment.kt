package dev.kkarot.installmentapp.ui.fragments.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dev.kkarot.installmentapp.adapters.PaymentsAdapter
import dev.kkarot.installmentapp.database.models.PaymentInfo
import dev.kkarot.installmentapp.databinding.FragmentPaymentsBinding
import dev.kkarot.installmentapp.viewmodels.SharedData
import dev.kkarot.installmentapp.viewmodels.SharedDataBase
import dev.kkarot.installmentapp.views.bottomsheets.PaymentSheet


class PaymentsFragment : Fragment() {


    private lateinit var binding: FragmentPaymentsBinding

    private val sharedData: SharedData by activityViewModels()
    private val sharedDatabase: SharedDataBase by activityViewModels()

    private lateinit var paymentList: ArrayList<PaymentInfo>
    private lateinit var paymentsAdapter: PaymentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentsBinding.inflate(inflater, container, false)

        sharedData.selectedInstallmentId.observe(viewLifecycleOwner) {
            sharedDatabase.getInstPayments(it)
        }
        sharedDatabase.instPaymentList.observe(viewLifecycleOwner) {
            paymentList = it
            initRecView(paymentList)
        }

        return binding.root
    }

    private fun initRecView(list: ArrayList<PaymentInfo>) {
        binding.paymentsRecView.apply {
            paymentsAdapter = PaymentsAdapter(list, onLongClick)
            adapter = paymentsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        }
    }

    private val onLongClick: (PaymentInfo,Int) -> Unit = { info,pos ->
        if (!info.isPaid) {
            val sheet = PaymentSheet(info, onReceivedClick, pos)
            sheet.show(childFragmentManager, PaymentSheet.TAG)
        }

    }
    private val onReceivedClick:(PaymentInfo,Int)->Unit ={info,pos ->
        val snackbar = Snackbar.make(
            binding.root,
            "Payment $pos Received",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo"){
            info.isPaid = false
            updateValues(info, pos)
        }
        snackbar.show()
        updateValues(info,pos)
    }

    private fun updateValues(info: PaymentInfo, pos: Int) {
        sharedDatabase.updatePayment(info)
        paymentsAdapter.changeState(pos)
    }

    companion object {
        private const val TAG = "---PaymentsFrag"
    }
}