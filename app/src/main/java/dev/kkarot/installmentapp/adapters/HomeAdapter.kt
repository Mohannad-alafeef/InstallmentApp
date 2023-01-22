package dev.kkarot.installmentapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.databinding.CustomerInfoItemBinding
import dev.kkarot.installmentapp.ui.fragments.HomeFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "---HomeAdapter"

class HomeAdapter(private var list: List<CustomerInfo>) :
    RecyclerView.Adapter<HomeAdapter.ItemHolder>() {
    inner class ItemHolder(private val binding: CustomerInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(p: Int) {
            binding.apply {
                name.text = list[p].customerName
                holder.setName(list[p].customerName)

                infoContainer.setOnClickListener { container ->
                    HomeFragmentDirections.actionHomeFragmentToCustomerDetailsFragment(list[p])
                        .let { direction ->
                            container.findNavController().navigate(direction)
                        }
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
}