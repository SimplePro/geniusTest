package com.wotin.geniustest.adapter.Test

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wotin.geniustest.activity.test.TestActivity
import com.wotin.geniustest.activity.test.TestQuicknessActivity
import com.wotin.geniustest.R
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import com.wotin.geniustest.service.QuicknessTestHeartManagementService

class TestQuicknessSlidingUpPanelRecyclerViewAdapter(val brainModeList : ArrayList<String>) : RecyclerView.Adapter<TestQuicknessSlidingUpPanelRecyclerViewAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TestQuicknessSlidingUpPanelRecyclerViewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sliding_up_panel_recyclerview_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                if(adapterPosition == 0) {
                    val intent = Intent(parent.context, TestQuicknessActivity::class.java)
                    intent.putExtra("brain_mode", "right_brain")
                    parent.context.startActivity(intent)
                    (parent.context as Activity).finish()
                } else if(adapterPosition == 1) {
                    val intent = Intent(parent.context, TestQuicknessActivity::class.java)
                    intent.putExtra("brain_mode", "left_brain")
                    parent.context.startActivity(intent)
                    (parent.context as Activity).finish()
                }
                val modeList = GetRoomMethod().getTestModeData(parent.context.applicationContext)
                modeList[2].start = false
                UpdateRoomMethod().updateTestModeData(parent.context.applicationContext, modeList[2])
                ContextCompat.startForegroundService(parent.context, Intent(parent.context.applicationContext, QuicknessTestHeartManagementService::class.java))
                val service = QuicknessTestHeartManagementService()
                service.setQuicknessInterface(TestActivity())
            }
        }
    }

    override fun getItemCount(): Int = brainModeList.size

    override fun onBindViewHolder(
        holder: TestQuicknessSlidingUpPanelRecyclerViewAdapter.CustomViewHolder,
        position: Int
    ) {
        holder.brainModeTextView.text = brainModeList[position]
    }

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val brainModeTextView = itemView.findViewById<TextView>(R.id.brain_mode_textview)
    }


}