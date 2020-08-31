package com.wotin.geniustest.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.R

class ModeRecyclerViewAdapter(val modeList : ArrayList<String>) : RecyclerView.Adapter<ModeRecyclerViewAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModeRecyclerViewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mode_recyclerview_item, parent, false)
        return CustomViewHolder(view).apply {
            modeLayout.setOnClickListener{
                Toast.makeText(parent.context.applicationContext, modeList[adapterPosition], Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getItemCount(): Int = modeList.size

    override fun onBindViewHolder(holder: ModeRecyclerViewAdapter.CustomViewHolder, position: Int) {
        holder.modeText.text = modeList[position]
        when(modeList[position]){
            "기억력 테스트" -> {
                holder.modeImage.setImageResource(R.drawable.memory)
            }
            "집중력 테스트" -> {
                holder.modeImage.setImageResource(R.drawable.concentration)
            }
        }
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val modeText = itemView.findViewById<TextView>(R.id.mode_item_textview)
        val modeImage = itemView.findViewById<ImageView>(R.id.mode_item_imageview)
        val modeLayout = itemView.findViewById<CardView>(R.id.mode_item_layout)
    }
}