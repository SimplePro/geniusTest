package com.wotin.geniustest.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.activity.userManagement.UserInformationActivity
import com.wotin.geniustest.customClass.RankCustomClass
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import com.wotin.geniustest.databinding.RankItemViewBinding
import org.json.JSONObject

class RankRecyclerViewAdapter(val rankList : ArrayList<RankCustomClass>) : RecyclerView.Adapter<RankRecyclerViewAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RankRecyclerViewAdapter.CustomViewHolder {
        val view = RankItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val intent = Intent(parent.context, UserInformationActivity::class.java)
                intent.putExtra("userId", rankList[adapterPosition].id)
                parent.context.startActivity(intent)
                (parent.context as Activity).finish()
            }
        }
    }

    override fun getItemCount(): Int = rankList.size

    override fun onBindViewHolder(holder: RankRecyclerViewAdapter.CustomViewHolder, position: Int) {
        holder.onBind(rankList[position])
    }

    class CustomViewHolder(val binding : RankItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data : RankCustomClass) {
            binding.rankData = data
        }
    }

}