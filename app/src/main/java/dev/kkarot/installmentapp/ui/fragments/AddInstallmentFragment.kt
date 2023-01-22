package dev.kkarot.installmentapp.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.database.models.Payments
import dev.kkarot.installmentapp.databinding.DialogProgressBinding
import dev.kkarot.installmentapp.databinding.FragmentAddInstallmentBinding
import dev.kkarot.installmentapp.viewmodels.SharedViewModel
import dev.kkarot.installmentapp.views.NumberFilter
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "---AddInstallmentFrag"

class AddInstallmentFragment : Fragment() {

    private lateinit var binding: FragmentAddInstallmentBinding
    private val args: AddInstallmentFragmentArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var info: CustomerInfo

    private var total = "0"
    private var rem = 0f
    private var profit = 0f
    private var payment = 0f
    private var iTitle = ""
    private var oPrice = 0f
    private var pRate = 0
    private var pType = "Monthly"
    private lateinit var payments: Payments
    private var period = 0

    private var pos = 1

    private val ca = Calendar.getInstance()
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var sYear = ca[Calendar.YEAR]
    private var sMonth = ca[Calendar.MONTH]
    private var sDay = ca[Calendar.DAY_OF_MONTH]

    private var sDate: Long = ca.timeInMillis
    private var eDate: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info = args.customerInfo
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddInstallmentBinding.inflate(layoutInflater, container, false)

        binding.apply {

            profitRate.editText?.filters = arrayOf(NumberFilter(0, 1000))
            originalPrice.editText?.filters = arrayOf(NumberFilter(0, 1_000_000_000))
            paymentPeriod.editText?.filters = arrayOf(NumberFilter(0, 180))
            endDate.text = formatter.format(ca.time)


            datePicker.init(
                ca[Calendar.YEAR], ca[Calendar.MONTH], ca[Calendar.DAY_OF_MONTH]
            ) { _, year, monthOfYear, dayOfMonth ->
                Log.d(TAG, "onCreateView: year = $year")
                Log.d(TAG, "onCreateView: monthOfYear = $monthOfYear")
                Log.d(TAG, "onCreateView: dayOfMonth = $dayOfMonth")
                ca[Calendar.YEAR] = year
                ca[Calendar.MONTH] = monthOfYear
                ca[Calendar.DAY_OF_MONTH] = dayOfMonth
                sDate = ca.timeInMillis
                setEndDate()


            }

            (paymentType.editText as? AutoCompleteTextView)?.let { autoCompleteTextView ->

                autoCompleteTextView.setText(
                    autoCompleteTextView.adapter?.getItem(1).toString(),
                    false
                )
                autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
                    pType = autoCompleteTextView.text.toString()
                    paymentPeriod.hint = "Period/" + pType.removeSuffix("ly")
                    pos = position
                    val period1 =
                        if (!paymentPeriod.editText?.text.isNullOrEmpty())
                            paymentPeriod.editText?.text?.toString()?.toFloat()!!
                        else
                            return@setOnItemClickListener
                    period = period1.toInt()
                    rem = total.toFloat().mod(period1)
                    payment = (total.toFloat() - rem) / period1
                    paymentAmount.text = "$payment reminder = ${"%.2f".format(rem)}"
                    setEndDate()
                }
            }

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

            paymentPeriod.editText?.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty()) {
                    text.toString().toFloat().let { _period ->
                        rem = total.toFloat().mod(_period)
                        payment = (total.toFloat() - rem) / _period
                        paymentAmount.text = "$payment reminder = ${"%.2f".format(rem)}"
                        period = _period.toInt()
                        setEndDate()


                    }
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
            saveBtn.setOnClickListener {
                if (!title.editText?.text.isNullOrEmpty() && !originalPrice.editText?.text.isNullOrEmpty() &&
                    !profitRate.editText?.text.isNullOrEmpty() && !paymentPeriod.editText?.text.isNullOrEmpty() &&
                    !paymentType.editText?.text.isNullOrEmpty()
                ) {
                    val dialog = showSavingDialog()
                    sharedViewModel.addInstallmentForCustomer(
                        info.customerId,
                        iTitle,
                        payment,
                        rem,
                        sDate,
                        eDate,
                        pType,
                        period,
                        oPrice,
                        profit,
                        pRate,
                        total.toFloat()
                    ) {
                        dialog.dismiss()
                        Toast.makeText(requireContext(), "Saved Successfully", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }

                } else {
                    Toast.makeText(requireContext(), "please fill all fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            cancelBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }


        return binding.root
    }

    private fun showSavingDialog(): AlertDialog {
        val d = AlertDialog.Builder(requireContext()).run {
            setView(DialogProgressBinding.inflate(layoutInflater).root)
            setCancelable(false)

        }.create()
        d.show()
        return d
    }

    private fun resetText() {
        binding.totalPrice.text = "0"
        binding.totalProfit.text = "0"
    }

    private fun setEndDate() {
        ca.timeInMillis = sDate
        when (pos) {
            0 -> {
                ca.add(Calendar.YEAR, period)
                eDate = ca.timeInMillis
            }
            1 -> {
                ca.add(Calendar.MONTH, period)
                eDate = ca.timeInMillis
            }
            2 -> {
                ca.add(Calendar.WEEK_OF_YEAR, period)
                eDate = ca.timeInMillis
            }
        }
        binding.endDate.text = formatter.format(ca.time)
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