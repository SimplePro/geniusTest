package com.wotin.geniustest.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.wotin.geniustest.Activity.Test.TestMemoryActivity
import com.wotin.geniustest.Activity.UserManagement.UserInformationActivity
import com.wotin.geniustest.CustomClass.RankCustomClass
import com.wotin.geniustest.EncryptionAndDetoxification
import com.wotin.geniustest.R
import kotlinx.android.synthetic.main.rank_item_view.view.*
import org.json.JSONObject

class RankRecyclerViewAdapter(val rankList : ArrayList<RankCustomClass>) : RecyclerView.Adapter<RankRecyclerViewAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RankRecyclerViewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rank_item_view, parent, false)
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
        holder.idTextView.text = EncryptionAndDetoxification().encryptionAndDetoxification(rankList[position].id)
        holder.rankTextView.text = rankList[position].ranking
        holder.levelTextView.text = rankList[position].level
        when(rankList[position].level) {
            "천재" -> holder.levelImageView.setImageResource(R.drawable.genius)
            "고수" -> holder.levelImageView.setImageResource(R.drawable.good_brain)
            "중수" -> holder.levelImageView.setImageResource(R.drawable.normal_brain)
            else -> holder.levelImageView.setImageResource(R.drawable.bad_brain)
        }
        holder.heartTextView.text = rankList[position].heart
        holder.rankTestSumDifferenceTextVIew.text = JSONObject(rankList[position].bestScore).get("test_sum_difference").toString()
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val levelImageView : ImageView = itemView.findViewById(R.id.rank_level_imageview)
        val rankTextView : TextView = itemView.findViewById(R.id.rank_textview)
        val idTextView: TextView = itemView.findViewById(R.id.rank_id_textview)
        val levelTextView : TextView = itemView.findViewById(R.id.rank_level_textview)
        val heartTextView : TextView = itemView.findViewById(R.id.rank_heart_textview)
        val rankTestSumDifferenceTextVIew : TextView = itemView.findViewById(R.id.rank_test_sum_difference_textview)
    }

}