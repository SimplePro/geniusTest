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
import com.wotin.geniustest.customClass.UserInformationCustomClass
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import com.wotin.geniustest.databinding.UserInformationItemViewBinding

class UserInformationRecyclerViewAdapter(val userData : ArrayList<UserInformationCustomClass>) : RecyclerView.Adapter<UserInformationRecyclerViewAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserInformationRecyclerViewAdapter.CustomViewHolder {
        val view = UserInformationItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val intent = Intent(parent.context, UserInformationActivity::class.java)
                intent.putExtra("userId", userData[adapterPosition].id)
                parent.context.startActivity(intent)
                (parent.context as Activity).finish()
            }
        }
    }

    override fun getItemCount(): Int = userData.size

    override fun onBindViewHolder(
        holder: UserInformationRecyclerViewAdapter.CustomViewHolder,
        position: Int
    ) {
        holder.onBind(userData[position])
    }

    class CustomViewHolder(val binding : UserInformationItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data : UserInformationCustomClass) {
            binding.userData = data
        }
    }
}