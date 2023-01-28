package dev.kkarot.installmentapp.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.database.models.PaymentInfo
import dev.kkarot.installmentapp.databinding.PaymentItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PaymentsAdapter(val list: ArrayList<PaymentInfo>, val onLongClick: (PaymentInfo,Int) -> Unit):RecyclerView.Adapter<PaymentsAdapter.ItemHolder>() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    inner class ItemHolder(val binding: PaymentItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(p:Int){
            binding.apply {
                paymentNo.text = (p+1).toString()
                paymentAmount.text = "%.02f".format(list[p].value)
                paymentDate.text = formatter.format(list[p].paymentDate)
                paymentState.setPayment(list[p])
                container.setOnLongClickListener {
                    onLongClick.invoke(list[p],p)
                    true
                }
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder=ItemHolder(
        PaymentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(holder.adapterPosition)

    }

    override fun getItemCount(): Int= list.size
    fun changeState(pos: Int) {
        this.list[pos].isPaid = true
        notifyItemRemoved(pos)
        notifyItemInserted(pos)
    }

}