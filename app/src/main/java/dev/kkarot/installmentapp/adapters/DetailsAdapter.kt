package dev.kkarot.installmentapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kkarot.installmentapp.databinding.DetailsRowBinding

class DetailsAdapter:RecyclerView.Adapter<DetailsAdapter.ItemHolder>() {
    inner class ItemHolder(v:DetailsRowBinding):RecyclerView.ViewHolder(v.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder=ItemHolder(
        DetailsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

    }

    override fun getItemCount(): Int= 5
}