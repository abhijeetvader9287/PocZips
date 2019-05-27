package com.bapspatil.moodtracker.adapter

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.bapspatil.moodtracker.R
import com.bapspatil.moodtracker.util.Constants

import java.util.ArrayList

class MoodsAdapter(private val mContext: Context, private val mMoods: ArrayList<Int>, private val mComments: ArrayList<String>) : RecyclerView.Adapter<MoodsAdapter.MoodViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MoodViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_mood, viewGroup, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(moodViewHolder: MoodViewHolder, i: Int) {
        when (i) {
            0 -> moodViewHolder.daysTextView.setText(R.string.today)
            1 -> moodViewHolder.daysTextView.setText(R.string.yesterday)
            else -> {
                val daysAgoText = i.toString() + " " + mContext.getString(R.string.days_ago)
                moodViewHolder.daysTextView.text = daysAgoText
            }
        }

        val mood = mMoods[i]
        val leftLayoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        val rightLayoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        val moodWeight: Float
        when (mood) {
            0 -> moodWeight = 0.2f
            1 -> moodWeight = 0.4f
            2 -> moodWeight = 0.6f
            3 -> moodWeight = 0.8f
            4 -> moodWeight = 1.0f
            else -> moodWeight = 0.8f
        }
        leftLayoutParams.weight = moodWeight
        rightLayoutParams.weight = 1.0f - moodWeight
        moodViewHolder.leftFrameLayout.layoutParams = leftLayoutParams
        moodViewHolder.rightFrameLayout.layoutParams = rightLayoutParams

        moodViewHolder.leftFrameLayout.setBackgroundResource(Constants.moodColorsArray[mood])

        val comment = mComments[i]
        if (comment != null && !comment.isEmpty()) {
            moodViewHolder.commentButton.visibility = View.VISIBLE
            moodViewHolder.commentButton.setOnClickListener { Toast.makeText(mContext, comment, Toast.LENGTH_LONG).show() }
        } else {
            moodViewHolder.commentButton.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mMoods.size
    }

    inner class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val leftFrameLayout: FrameLayout
          val rightFrameLayout: FrameLayout
          val commentButton: ImageButton
          val daysTextView: TextView

        init {

            leftFrameLayout = itemView.findViewById(R.id.left_frame_layout)
            rightFrameLayout = itemView.findViewById(R.id.right_frame_layout)
            commentButton = itemView.findViewById(R.id.btn_show_comment)
            daysTextView = itemView.findViewById(R.id.tv_days)
        }
    }
}
