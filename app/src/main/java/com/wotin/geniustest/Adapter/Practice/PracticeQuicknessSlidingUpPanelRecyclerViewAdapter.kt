package com.wotin.geniustest.Adapter.Practice

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.Activity.Practice.PracticeQuicknessActivity
import com.wotin.geniustest.R

class PracticeQuicknessSlidingUpPanelRecyclerViewAdapter(val brainModeList : ArrayList<String>) : RecyclerView.Adapter<PracticeQuicknessSlidingUpPanelRecyclerViewAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PracticeQuicknessSlidingUpPanelRecyclerViewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sliding_up_panel_recyclerview_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                if(adapterPosition == 0) {
                    val intent = Intent(parent.context, PracticeQuicknessActivity::class.java)
                    intent.putExtra("brain_mode", "right_brain")
                    parent.context.startActivity(intent)
                    (parent.context as Activity).finish()
                } else if(adapterPosition == 1) {
                    val intent = Intent(parent.context, PracticeQuicknessActivity::class.java)
                    intent.putExtra("brain_mode", "left_brain")
                    parent.context.startActivity(intent)
                    (parent.context as Activity).finish()
                }
            }
        }
    }

    override fun getItemCount(): Int = brainModeList.size

    override fun onBindViewHolder(
        holder: PracticeQuicknessSlidingUpPanelRecyclerViewAdapter.CustomViewHolder,
        position: Int
    ) {
        holder.brainModeTextView.text = brainModeList[position]
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val brainModeTextView = itemView.findViewById<TextView>(R.id.brain_mode_textview)
    }


}