package com.bapspatil.moodtracker.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout

import com.bapspatil.moodtracker.util.Constants
import com.bapspatil.moodtracker.R
import com.bapspatil.moodtracker.data.SharedPreferencesHelper
import com.bapspatil.moodtracker.receiver.UpdateDayReceiver

import java.util.Calendar

class MoodActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private var parentRelativeLayout: RelativeLayout? = null
    private var moodImageView: ImageView? = null
    private var addCommentButton: ImageButton? = null
    private var moodHistoryButton: ImageButton? = null
    private var mDetector: GestureDetectorCompat? = null

    private var mPreferences: SharedPreferences? = null
    private var currentDay: Int = 0
    private var currentMoodIndex: Int = 0
    private var currentComment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood)

        parentRelativeLayout = findViewById(R.id.parent_relative_layout)
        moodImageView = findViewById(R.id.iv_mood)
        addCommentButton = findViewById(R.id.btn_add_comment)
        moodHistoryButton = findViewById(R.id.btn_mood_history)

        mDetector = GestureDetectorCompat(this, this)
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        currentDay = mPreferences!!.getInt(SharedPreferencesHelper.KEY_CURRENT_DAY, 1)
        currentMoodIndex = mPreferences!!.getInt(SharedPreferencesHelper.KEY_CURRENT_MOOD, 3)
        currentComment = mPreferences!!.getString(SharedPreferencesHelper.KEY_CURRENT_COMMENT, "")

        changeUiForMood(currentMoodIndex)

        scheduleAlarm()

        addCommentButton!!.setOnClickListener {
            val editText = EditText(this@MoodActivity)
            editText.hint = "Comment"
            val alertDialog = AlertDialog.Builder(this@MoodActivity)
                    .setView(editText)
                    .setPositiveButton("CONFIRM") { dialog, which ->
                        if (!editText.text.toString().isEmpty()) {
                            SharedPreferencesHelper.saveComment(editText.text.toString(), currentDay, mPreferences!!)
                        }
                    }
                    .setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
                    .create()
            alertDialog.show()
        }

        moodHistoryButton!!.setOnClickListener {
            val intent = Intent(this@MoodActivity, MoodHistoryActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDown(e: MotionEvent): Boolean {
        // Not needed for this project.
        return false
    }

    override fun onShowPress(e: MotionEvent) {
        // Not needed for this project.
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        // Not needed for this project.
        return false
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        // Not needed for this project.
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        // Not needed for this project.
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        // Swiping from bottom to top or, swiping up!
        if (e1.y - e2.y > 50) {
            if (currentMoodIndex < 4) {
                currentMoodIndex++
                changeUiForMood(currentMoodIndex)
                SharedPreferencesHelper.saveMood(currentMoodIndex, currentDay, mPreferences!!)
            }
        } else if (e1.y - e2.y < 50) {
            if (currentMoodIndex > 0) {
                currentMoodIndex--
                changeUiForMood(currentMoodIndex)
                SharedPreferencesHelper.saveMood(currentMoodIndex, currentDay, mPreferences!!)
            }
        }// Swiping from top to bottom, or swiping down!
        return true
    }

    private fun changeUiForMood(moodIndex: Int) {
        moodImageView!!.setImageResource(Constants.moodImagesArray[moodIndex])
        parentRelativeLayout!!.setBackgroundResource(Constants.moodColorsArray[moodIndex])
        val mediaPlayer = MediaPlayer.create(this, Constants.moodSoundsArray[moodIndex])
        mediaPlayer.start()
    }

    private fun scheduleAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, UpdateDayReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mDetector!!.onTouchEvent(event)
    }
}
