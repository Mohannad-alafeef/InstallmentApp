package dev.kkarot.installmentapp.ui.fragments.add

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.databinding.FragmentAddInstallmentBinding
import dev.kkarot.installmentapp.viewmodels.SharedData
import dev.kkarot.installmentapp.views.NumberFilter
import java.math.BigDecimal
import java.util.*

private const val TAG = "---AddInstallmentFrag"

class AddInstallmentFragment : Fragment() {

    private lateinit var binding: FragmentAddInstallmentBinding
    private val sharedData: SharedData by activityViewModels()


    private lateinit var info: CustomerInfo

    private var total = "0"
    private var profit = 0f
    private var iTitle = ""
    private var oPrice = 0f
    private var pRate = 0


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddInstallmentBinding.inflate(layoutInflater, container, false)

        sharedData.selectedCustomer.observe(viewLifecycleOwner) {
            info = it
        }

        binding.apply {

            toolbar.setupWithNavController(findNavController())

            profitRate.editText?.filters = arrayOf(NumberFilter(0, 1000))
            originalPrice.editText?.filters = arrayOf(NumberFilter(0, 1_000_000_000))


            originalPrice.editText?.doOnTextChanged { text, _, _, _ ->
                if (!profitRate.editText?.text.isNullOrEmpty() && !text.isNullOrEmpty()) {
                    text.toString().let { _oPrice ->
                        profitRate.editText?.text?.toString()?.let { pRate ->

                            getTotal(_oPrice, pRate)
                            getProfit(_oPrice, pRate)

                        }
                    }
                } else {
                    resetText()
                }
            }

            profitRate.editText?.doOnTextChanged { text, _, _, _ ->
                if (!originalPrice.editText?.text.isNullOrEmpty() && !text.isNullOrEmpty()) {
                    text.toString().let { _pRate ->

                        originalPrice.editText?.text?.toString()?.let { _oPrice ->
                            getTotal(_oPrice, _pRate)
                            getProfit(_oPrice, _pRate)

                        }
                    }
                } else {
                    resetText()
                }
            }

            title.editText?.doOnTextChanged { text, _, _, _ ->
                iTitle = if (text.isNullOrEmpty())
                    ""
                else
                    text.toString()
            }
        }

        binding.apply {
            nextBtn.setOnClickListener {
                if (checkError()) return@setOnClickListener
                val installmentInfo = InstallmentInfo(
                    0,
                    info.customerId,
                    iTitle,
                    oPrice,
                    pRate,
                    profit,
                    total.toFloat()
                )
                sharedData.setInstallment(installmentInfo)
                AddInstallmentFragmentDirections.actionAddInstallmentFragmentToAddPaymentsFragment()
                    .let { navDir ->
                        findNavController().navigate(navDir)
                    }
            }

        }


        return binding.root
    }

    private fun checkError(): Boolean {
        var isError = false
        if (iTitle.isEmpty()) {
            binding.title.error = getString(R.string.required_field)
            isError = true
        }else
            binding.title.error = null

        if (binding.originalPrice.editText?.text.isNullOrEmpty()) {
            binding.originalPrice.error = getString(R.string.required_field)
            isError = true
        }else
            binding.originalPrice.error = null

        if (binding.profitRate.editText?.text.isNullOrEmpty()) {
            binding.profitRate.error = getString(R.string.required_field)
            isError = true
        }else
            binding.profitRate.error = null
        return isError
    }



    private fun resetText() {
        binding.totalPrice.text = "0"
        binding.totalProfit.text = "0"
    }


    private fun getProfit(_oPrice: String, _pRate: String): String {
        oPrice = _oPrice.toFloat()
        val rate = BigDecimal(_pRate).divide(BigDecimal(100))
        profit = (BigDecimal(_oPrice) * rate).toFloat()
        val stringProfit = "%.2f".format(profit)
        binding.totalProfit.text = stringProfit
        return stringProfit
    }

    private fun getTotal(_oPrice: String, _pRate: String): String {
        val rate = BigDecimal(_pRate).divide(BigDecimal(100))
        pRate = _pRate.toInt()
        total = "%.02f".format(BigDecimal(_oPrice) * (BigDecimal(1) + rate))
        binding.totalPrice.text = total
        return total
    }


}