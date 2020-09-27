package com.wotin.geniustest.Adapter.Practice

import android.app.Activity
import android.app.AlertDialog
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.Activity.Practice.PracticeConcentractionActivity
import com.wotin.geniustest.Activity.Practice.PracticeQuicknessActivity
import com.wotin.geniustest.CustomClass.PracticeModeCustomClass
import com.wotin.geniustest.R
import com.wotin.geniustest.networkState


class PracticeModeRecyclerViewAdapter(val modeList : ArrayList<PracticeModeCustomClass>, val quicknessModeClicked: QuicknessModeClickedInterface) : RecyclerView.Adapter<PracticeModeRecyclerViewAdapter.CustomViewHolder>() {

    interface QuicknessModeClickedInterface {
        fun quicknessModeClicked()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.practice_mode_recyclerview_item, parent, false)
        return CustomViewHolder(
            view
        ).apply {
            modeLayout.setOnClickListener {
                val connectivityManager: ConnectivityManager =
                    parent.context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                if (networkState(connectivityManager)) {
                    if (modeList[adapterPosition].mode == "집중력 테스트") {
                        val intent =
                            Intent(parent.context, PracticeConcentractionActivity::class.java)
                        parent.context.startActivity(intent)
                        (parent.context as Activity).finish()
                    } else if (modeList[adapterPosition].mode == "순발력 테스트") {
                        quicknessModeClicked.quicknessModeClicked()
                    }
                } else Toast.makeText(
                    parent.context.applicationContext,
                    "네트워크 연결을 확인해주세요",
                    Toast.LENGTH_LONG
                ).show()
            }
            modeQuestionMark.setOnClickListener {
                when(modeList[adapterPosition].mode) {
                    "집중력 테스트" -> {
                        val dialog = AlertDialog.Builder(parent.context)
                        val EDialog = LayoutInflater.from(parent.context)
                        val MView = EDialog.inflate(R.layout.explain_concentraction_dialog_layout, null)
                        val builder = dialog.create()

                        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        builder.window?.requestFeature(Window.FEATURE_NO_TITLE)

                        builder.setView(MView)
                        builder.show()
                    }
                    "순발력 테스트" -> {
                        val dialog = AlertDialog.Builder(parent.context)
                        val EDialog = LayoutInflater.from(parent.context)
                        val MView = EDialog.inflate(R.layout.explain_quickness_dialog_layout, null)
                        val builder = dialog.create()
                        builder.setView(MView)
                        builder.show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = modeList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
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
            "순발력 테스트" -> {
                holder.modeImage.setImageResource(R.drawable.quickness)
            }
        }
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val modeText = itemView.findViewById<TextView>(R.id.practice_mode_item_textview)
        val modeImage = itemView.findViewById<ImageView>(R.id.practice_mode_item_imageview)
        val modeScoreText = itemView.findViewById<TextView>(R.id.practice_mode_item_score_textview)
        val modeDifferenceText = itemView.findViewById<TextView>(R.id.practice_mode_item_difference_textview)
        val modeLayout = itemView.findViewById<CardView>(R.id.practice_mode_item_layout)
        val modeQuestionMark = itemView.findViewById<ImageView>(R.id.practice_mode_question_mark_imageview)
    }
}