package com.wotin.geniustest.adapter.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.R

class TestConcentractionRecycelrViewAdapter(val concentractionList : ArrayList<String>, val clickListener: ItemClickListener) : RecyclerView.Adapter<TestConcentractionRecycelrViewAdapter.CustomViewHolder>() {

    interface ItemClickListener {
        fun itemClicked(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TestConcentractionRecycelrViewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.concentraction_recyclerview_item, parent, false)
        return TestConcentractionRecycelrViewAdapter.CustomViewHolder(
            view
        ).apply {
            itemView.setOnClickListener {
                clickListener.itemClicked(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = concentractionList.size

    override fun onBindViewHolder(
        holder: TestConcentractionRecycelrViewAdapter.CustomViewHolder,
        position: Int
    ) {
        holder.concentractionTextView.text = concentractionList[position]
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val concentractionTextView = itemView.findViewById<TextView>(R.id.concentraction_textview)
    }
}
