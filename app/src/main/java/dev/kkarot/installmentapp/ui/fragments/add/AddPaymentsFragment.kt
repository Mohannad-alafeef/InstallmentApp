package dev.kkarot.installmentapp.ui.fragments.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.cons.InstallmentType
import dev.kkarot.installmentapp.cons.PaymentOpt
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.databinding.FragmentAddPaymentsBinding
import dev.kkarot.installmentapp.viewmodels.SharedData
import dev.kkarot.installmentapp.viewmodels.SharedDataBase
import dev.kkarot.installmentapp.views.NumberFilter
import java.text.SimpleDateFormat
import java.util.*


class AddPaymentsFragment : Fragment() {
    companion object {
        private const val TAG = "---AddPayFrag"
    }

    private lateinit var binding: FragmentAddPaymentsBinding
    private val sharedDatabase: SharedDataBase by activityViewModels()
    private val sharedData: SharedData by activityViewModels()

    private var pos = 1

    private lateinit var info: InstallmentInfo

    private val ca = Calendar.getInstance()
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var sDate: Long = ca.timeInMillis
    private var eDate: Long = 0L

    private var period = 0f
    private var reminder = 0f
    private var payment = 0f
    private var prePaymentValue = 0f

    private var tReminder: Float = 0f
        get() = this.reminder
    private var tPayment: Float = 0f
        get() = this.payment

    private var isFraction = false
    private var paymentOpt: PaymentOpt = PaymentOpt.FirstPayment

    private lateinit var customer:CustomerInfo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPaymentsBinding.inflate(layoutInflater, container, false)


        sharedData.selectedInstallment.observe(viewLifecycleOwner) { info ->
            this.info = info
            info.paymentType = InstallmentType.Monthly.name
        }
        sharedData.selectedCustomer.observe(viewLifecycleOwner){customer ->
            this.customer = customer
        }


        binding.apply {

            toolbar.setupWithNavController(findNavController())

            (radioGroup[1] as RadioButton ).isChecked = true
            paymentPeriod.editText?.filters = arrayOf(NumberFilter(0, 180))
            formatter.format(ca.time).let {
                endDate.text = it
                firstPaymentDate.text = it
            }
            editDate.setOnClickListener {
                showDateDialog()
            }

            fraction.setOnCheckedChangeListener { _, isChecked ->
                isFraction = isChecked
                radioGroup.children.forEach { child ->
                    child.isEnabled = !isChecked
                }
                if (isChecked) {
                    paymentOpt = PaymentOpt.Fraction
                } else {
                    setPaymentOpt(
                        radioGroup.checkedRadioButtonId
                    )
                }
                updateValues()

            }
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                setPaymentOpt(checkedId)
            }


            prePayment.editText?.doOnTextChanged { text, _, _, _ ->
                prePaymentValue = if (!text.isNullOrEmpty()) {
                    text.toString().toFloat()
                } else
                    0f
                updateValues()
            }


            (paymentType.editText as? AutoCompleteTextView)?.let { autoCompleteTextView ->

                autoCompleteTextView.setText(
                    autoCompleteTextView.adapter?.getItem(1).toString(),
                    false
                )
                autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
                    info.paymentType = autoCompleteTextView.text.toString()
                    paymentPeriod.hint = "Period/" + info.paymentType.removeSuffix("ly")
                    pos = position
                    val period1 =
                        if (!paymentPeriod.editText?.text.isNullOrEmpty())
                            paymentPeriod.editText?.text?.toString()?.toFloat()!!
                        else
                            return@setOnItemClickListener
                    period = period1
                    updateValues()

                }
            }
            paymentPeriod.editText?.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty()) {
                    text.toString().toFloat().let { _period ->
                        period = _period
                        updateValues()

                    }
                }
            }
            saveBtn.setOnClickListener {
                if (checkError()) return@setOnClickListener
                info.startDate = Date(sDate)
                info.endDate = Date(eDate)
                info.period = period.toInt()
                info.reminder = reminder
                info.payment = payment
                info.prePayment = prePaymentValue

                if (customer.customerId == 0L){
                    sharedDatabase.add(customer,sharedData.selectedInstallment.value!!,paymentOpt){
                        sharedData.setCustomer(customer)
                        navigate()
                    }
                }else{
                    sharedDatabase.insertInstallment(sharedData.selectedInstallment.value!!,paymentOpt) {
                        navigate()
                    }
                }




            }
        }

        return binding.root
    }

    private fun checkError(): Boolean {
        var isError = false
        if (binding.paymentPeriod.editText?.text.isNullOrEmpty()){
            binding.paymentPeriod.error = getString(R.string.required_field)
            isError = true
        }else
            binding.paymentPeriod.error = null

        return isError
    }

    private fun navigate() {
        Toast.makeText(requireContext(), getString(R.string.saved_successfully), Toast.LENGTH_SHORT)
            .show()
        AddPaymentsFragmentDirections.actionAddPaymentsFragmentToHomeFragment()
            .let { navDir ->
                findNavController().navigate(navDir)
            }
    }

    private fun setPaymentOpt(id: Int) {
        when (id) {
            1 -> {
                paymentOpt = PaymentOpt.FirstPayment
            }
            2 -> {
                paymentOpt = PaymentOpt.LastPayment
            }
        }
    }


    private fun updateValues() {
        setEndDate()
        if (isFraction) {
            reminder = 0f
            payment = (info.totalPrice - prePaymentValue) / period
            binding.paymentAmount.text = "${"%.2f".format(payment)}"
            return
        }
        reminder = (info.totalPrice - prePaymentValue).mod(period)
        payment = (info.totalPrice - prePaymentValue - reminder) / period
        binding.paymentAmount.text = "$payment reminder = ${"%.2f".format(reminder)}"
    }

    private fun showDateDialog() {
        val dialog = DatePickerDialog(requireContext())
        Log.d(TAG, "showDateDialog: ${ca[Calendar.MONTH]}")
        ca.clear()
        ca.time = Date()
        dialog.updateDate(ca[Calendar.YEAR], ca[Calendar.MONTH], ca[Calendar.DAY_OF_MONTH])
        dialog.show()
        dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            ca[Calendar.YEAR] = year
            ca[Calendar.MONTH] = month
            ca[Calendar.DAY_OF_MONTH] = dayOfMonth
            sDate = ca.timeInMillis
            setEndDate()
        }
    }

    private fun setEndDate() {
        ca.timeInMillis = sDate
        binding.firstPaymentDate.text = formatter.format(ca.time)
        when (pos) {
            0 -> {
                ca.add(Calendar.YEAR, period.toInt())
                eDate = ca.timeInMillis
            }
            1 -> {
                ca.add(Calendar.MONTH, period.toInt())
                eDate = ca.timeInMillis
            }
            2 -> {
                ca.add(Calendar.WEEK_OF_YEAR, period.toInt())
                eDate = ca.timeInMillis
            }
        }
        binding.endDate.text = formatter.format(ca.time)
    }


}