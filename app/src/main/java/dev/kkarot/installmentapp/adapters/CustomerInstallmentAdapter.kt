package dev.kkarot.installmentapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kkarot.installmentapp.database.models.InstallmentInfo
import dev.kkarot.installmentapp.databinding.CustomerInstallmentBinding
import java.text.SimpleDateFormat
import java.util.*

class CustomerInstallmentAdapter(
    private var installmentList: List<InstallmentInfo>,
    val onLongClick: (InstallmentInfo, Int) -> Unit,
    val onClick: (InstallmentInfo) -> Unit
) :
    RecyclerView.Adapter<CustomerInstallmentAdapter.ItemHolder>() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    inner class ItemHolder(private val binding: CustomerInstallmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(p: Int) {
            binding.apply {
                title.text = installmentList[p].installmentTitle
                start.text = formatter.format(installmentList[p].startDate!!)
                end.text = formatter.format(installmentList[p].endDate!!)
                received.text = installmentList[p].received.toString()
                due.text = installmentList[p].period.toString()
                container.setOnLongClickListener {
                    onLongClick.invoke(installmentList[p],p)
                    false
                }
                container.setOnClickListener {
                    onClick.invoke(installmentList[p])
                }
                editInstallment.setOnClickListener {
                    onLongClick.invoke(installmentList[p],p)
                }
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
    fun remove(pos: Int) {
        notifyItemRemoved(pos)
    }
}