package dev.kkarot.installmentapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.databinding.CustomerInfoItemBinding

private const val TAG = "---HomeAdapter"

class HomeAdapter(
    private var list: List<CustomerInfo>,
    val onItemClick: (CustomerInfo) -> Unit,
    val onItemLongClick: (CustomerInfo,Int) -> Unit
) :
    RecyclerView.Adapter<HomeAdapter.ItemHolder>() {
    inner class ItemHolder(private val binding: CustomerInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(p: Int) {
            binding.apply {
                name.text = list[p].customerName
                holder.setName(list[p].customerName)
                infoContainer.setOnClickListener {
                    onItemClick.invoke(list[p])
                }
                infoContainer.setOnLongClickListener {
                    onItemLongClick.invoke(list[p],p)
                    return@setOnLongClickListener true
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder = ItemHolder(
        CustomerInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(holder.adapterPosition)
    }

    override fun getItemCount(): Int = list.size

    fun filter(list: List<CustomerInfo>) {
        notifyItemRangeRemoved(0, this.list.size)
        this.list = list
        notifyItemRangeInserted(0, list.size)

    }

    fun remove(list: List<CustomerInfo>, pos: Int) {
        this.list = list
        notifyItemRemoved(pos)
    }
}