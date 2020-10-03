package com.wotin.geniustest.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.Activity.UserManagement.UserInformationActivity
import com.wotin.geniustest.CustomClass.UserInformationCustomClass
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import kotlinx.android.synthetic.main.user_information_item_view.view.*

class UserInformationRecyclerViewAdapter(val userData : ArrayList<UserInformationCustomClass>) : RecyclerView.Adapter<UserInformationRecyclerViewAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserInformationRecyclerViewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_information_item_view, parent, false)
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
        when(userData[position].level) {
            "천재" -> holder.levelImageView.setImageResource(R.drawable.genius)
            "고수" -> holder.levelImageView.setImageResource(R.drawable.good_brain)
            "중수" -> holder.levelImageView.setImageResource(R.drawable.normal_brain)
            "초보" -> holder.levelImageView.setImageResource(R.drawable.bad_brain)
        }
        holder.idTextView.text = EncryptionAndDetoxification().encryptionAndDetoxification(userData[position].id)
        holder.heartNumTextView.text = userData[position].heartNum
        holder.testSumDifferenceTextView.text = userData[position].testSumDifference
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val levelImageView = itemView.findViewById<ImageView>(R.id.user_information_level_imageview)
        val idTextView = itemView.findViewById<TextView>(R.id.user_information_id_textview)
        val heartNumTextView = itemView.findViewById<TextView>(R.id.user_information_heart_num_textview)
        val testSumDifferenceTextView = itemView.findViewById<TextView>(R.id.user_information_test_sum_difference_textview)
    }
}