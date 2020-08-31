package com.wotin.geniustest.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.CustomClass.ModeCustomClass
import com.wotin.geniustest.R

class ModeRecyclerViewAdapter(val modeList : ArrayList<ModeCustomClass>) : RecyclerView.Adapter<ModeRecyclerViewAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModeRecyclerViewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mode_recyclerview_item, parent, false)
        return CustomViewHolder(view).apply {
            modeLayout.setOnClickListener{
//                데이터에 따라서 다르게 행동해야 함. -> 기억력 테스트라면, 기억력 테스트를 하도록 하고. 집중력 테스트라면 집중력 테스트를 해야 한다.
            }
        }
    }

    override fun getItemCount(): Int = modeList.size

    override fun onBindViewHolder(holder: ModeRecyclerViewAdapter.CustomViewHolder, position: Int) {
        holder.modeText.text = modeList[position].mode
        holder.modeScoreText.text = modeList[position].score.toString()
        holder.modeDifferenceText.text = modeList[position].difference
        when(modeList[position].mode){
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
        val modeScoreText = itemView.findViewById<TextView>(R.id.mode_item_score_textview)
        val modeDifferenceText = itemView.findViewById<TextView>(R.id.mode_item_difference_textview)
        val modeLayout = itemView.findViewById<CardView>(R.id.mode_item_layout)
    }
}