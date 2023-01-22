package dev.kkarot.installmentapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.databinding.CustomerInstallmentBinding
import java.text.SimpleDateFormat

class CustomerInstallmentAdapter(private var installmentList: List<InstallmentInfo>) :
    RecyclerView.Adapter<CustomerInstallmentAdapter.ItemHolder>() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    inner class ItemHolder(private val binding: CustomerInstallmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(p: Int) {
            binding.apply {
                title.text = installmentList[p].installmentTitle
                start.text = formatter.format(installmentList[p].startDate)
                end.text = formatter.format(installmentList[p].endDate)
                due.text = installmentList[p].payments.paymentList.size.toString()
                received.text = installmentList[p].payments.paymentList
                    .filter {
                        it.isPaid
                    }.size.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder = ItemHolder(
        CustomerInstallmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(holder.adapterPosition)
    }

    override fun getItemCount(): Int = installmentList.size
}