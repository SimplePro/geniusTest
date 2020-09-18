package com.wotin.geniustest.Adapter.Test

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.Activity.Test.TestConcentractionActivity
import com.wotin.geniustest.Activity.Test.TestQuicknessActivity
import com.wotin.geniustest.CustomClass.TestModeCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.networkState

class TestModeRecyclerViewAdapter(val modeList : ArrayList<TestModeCustomClass>) : RecyclerView.Adapter<TestModeRecyclerViewAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_mode_recyclerview_item, parent, false)
        return CustomViewHolder(
            view
        ).apply {
            modeLayout.setOnClickListener{
                val connectivityManager : ConnectivityManager = parent.context.getSystemService(
                    Context.CONNECTIVITY_SERVICE
                ) as ConnectivityManager
                if(networkState(connectivityManager)) {
                    if(modeList[adapterPosition].mode == "집중력 테스트") {
                        val intent = Intent(parent.context, TestConcentractionActivity::class.java)
                        parent.context.startActivity(intent)
                        (parent.context as Activity).finish()
                    } else if(modeList[adapterPosition].mode == "순발력 테스트") {
                        val intent = Intent(parent.context, TestQuicknessActivity::class.java)
                        parent.context.startActivity(intent)
                        (parent.context as Activity).finish()
                    }
                } else Toast.makeText(parent.context.applicationContext, "네트워크 연결을 확인해주세요", Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun getItemCount(): Int = modeList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.modeText.text = modeList[position].mode
        holder.modeScoreText.text = modeList[position].score.toString()
        holder.modeDifferenceText.text = modeList[position].difference
        if(modeList[position].start) {
            holder.modeStartImageView.setImageResource(R.drawable.start_circle)
        } else {
            holder.modeStartImageView.setImageResource(R.drawable.no_start_circle)
        }
        when(modeList[position].mode){
            "기억력 테스트" -> {
                holder.modeImage.setImageResource(R.drawable.memory)
            }
            "집중력 테스트" -> {
                holder.modeImage.setImageResource(R.drawable.concentration)
            }
            "순발력 테스트" -> {
                holder.modeImage.setImageResource(R.drawable.quickness)
            }
        }
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val modeText = itemView.findViewById<TextView>(R.id.test_mode_item_textview)
        val modeImage = itemView.findViewById<ImageView>(R.id.test_mode_item_imageview)
        val modeScoreText = itemView.findViewById<TextView>(R.id.test_mode_item_score_textview)
        val modeDifferenceText = itemView.findViewById<TextView>(R.id.test_mode_item_difference_textview)
        val modeLayout = itemView.findViewById<CardView>(R.id.test_mode_item_layout)
        val modeStartImageView = itemView.findViewById<ImageView>(R.id.test_mode_item_start_imageview)
    }
}