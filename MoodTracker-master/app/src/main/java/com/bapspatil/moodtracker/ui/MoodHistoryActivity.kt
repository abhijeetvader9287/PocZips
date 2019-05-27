package com.bapspatil.moodtracker.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.bapspatil.moodtracker.adapter.MoodsAdapter
import com.bapspatil.moodtracker.R
import com.bapspatil.moodtracker.data.SharedPreferencesHelper

import java.util.ArrayList

class MoodHistoryActivity : AppCompatActivity() {

    private var moodsRecyclerView: RecyclerView? = null

    private var moodsAdapter: MoodsAdapter? = null
    private var mPreferences: SharedPreferences? = null
    private val moods = ArrayList<Int>()
    private val comments = ArrayList<String>()
    private var currentDay: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_history)

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        currentDay = mPreferences!!.getInt(SharedPreferencesHelper.KEY_CURRENT_DAY, 1)

        moodsRecyclerView = findViewById(R.id.rv_moods)
        moodsRecyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        for (i in 0 until currentDay) {
            moods.add(mPreferences!!.getInt("KEY_MOOD$i", 3))
            comments.add(mPreferences!!.getString("KEY_COMMENT$i", ""))
        }

        moodsAdapter = MoodsAdapter(this, moods, comments)
        moodsRecyclerView!!.adapter = moodsAdapter
    }
}
