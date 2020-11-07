package com.wotin.geniustest.adapter.Test

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.R

class TestQuicknessRecyclerViewAdapter(val quicknessList : ArrayList<String>, val clickListener: ItemClickListener)  : RecyclerView.Adapter<TestQuicknessRecyclerViewAdapter.CustomViewHolder>() {
    interface ItemClickListener {
        fun itemClick(clickedColor : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quickness_recyclerview_item, parent, false)
        return TestQuicknessRecyclerViewAdapter.CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                clickListener.itemClick(quicknessList[adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = quicknessList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        when(quicknessList[position]) {
            "빨강" -> {
                holder.quicknessImageView.setImageResource(R.drawable.red_circle)
                Log.d(
                    "TAG",
                    "onBindViewHolder: quicknessList[$position] is ${quicknessList[position]}"
                )
            }
            "주황" -> {
                holder.quicknessImageView.setImageResource(R.drawable.orange_circle)
                Log.d(
                    "TAG",
                    "onBindViewHolder: quicknessList[$position] is ${quicknessList[position]}"
                )
            }
            "노랑" -> {
                holder.quicknessImageView.setImageResource(R.drawable.yellow_circle)
                Log.d(
                    "TAG",
                    "onBindViewHolder: quicknessList[$position] is ${quicknessList[position]}"
                )
            }
            "연두" -> {
                holder.quicknessImageView.setImageResource(R.drawable.light_green_circle)
                Log.d(
                    "TAG",
                    "onBindViewHolder: quicknessList[$position] is ${quicknessList[position]}"
                )
            }
            "초록" -> {
                holder.quicknessImageView.setImageResource(R.drawable.green_circle)
                Log.d(
                    "TAG",
                    "onBindViewHolder: quicknessList[$position] is ${quicknessList[position]}"
                )
            }
            "하늘" -> {
                holder.quicknessImageView.setImageResource(R.drawable.sky_blue_circle)
                Log.d(
                    "TAG",
                    "onBindViewHolder: quicknessList[$position] is ${quicknessList[position]}"
                )
            }
            "파랑" -> {
                holder.quicknessImageView.setImageResource(R.drawable.blue_circle)
                Log.d(
                    "TAG",
                    "onBindViewHolder: quicknessList[$position] is ${quicknessList[position]}"
                )
            }
            "보라" -> {
                holder.quicknessImageView .setImageResource(R.drawable.purple_circle)
                Log.d(
                    "TAG",
                    "onBindViewHolder: quicknessList[$position] is ${quicknessList[position]}"
                )
            }
        }
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val quicknessImageView = itemView.findViewById<ImageView>(R.id.quickness_item_imageview)
    }
}
