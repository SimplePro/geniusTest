package com.wotin.geniustest.Adapter.Practice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.R

class PracticeQuicknessRecyclerViewAdapter(val quicknessList : ArrayList<String>, val clickListener: ItemClickListener) : RecyclerView.Adapter<PracticeQuicknessRecyclerViewAdapter.CustomViewHolder>() {
    interface ItemClickListener {
        fun itemClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PracticeQuicknessRecyclerViewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quickness_recyclerview_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                clickListener.itemClick(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = quicknessList.size

    override fun onBindViewHolder(
        holder: PracticeQuicknessRecyclerViewAdapter.CustomViewHolder,
        position: Int
    ) {
        when(quicknessList[position]) {
            "빨강" -> {
//                holder.quicknessImageView.tint
            }
        }
    }
    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val quicknessImageView = itemView.findViewById<ImageView>(R.id.quickness_item_imageview)

    }
}