package com.android.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.main.R
import com.android.main.ReserveDataBean
import com.android.main.ReserveInterf
import com.android.main.databinding.ItemReserveBinding

class ReserveRV(val list: MutableList<ReserveDataBean>,val call:ReserveFragment) : RecyclerView.Adapter<ReserveRV.Holder>() {

    class Holder(val binding: ItemReserveBinding,val call:ReserveFragment) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(item:ReserveDataBean){
            val t = "设备\n${item.id}"
            binding.tvDevId.text = t
            val time = "预计可用${item.time}小时"
            binding.tvItemTimes.text = time
            val level = "${item.level}楼"
            binding.tvItemLevels.text = level
            binding.root.setOnClickListener {
                call.selectId(item.id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<ItemReserveBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_reserve,
            parent,
            false
        )
        return Holder(binding,call)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount() = list.size

}