package com.example.android.appusagestatistics;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
public class ScreenTimeBroadcastReceiver extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Toast.makeText(context, "Screen on "+UtilityClass.totalHrs(context), Toast.LENGTH_SHORT).show();
        }
    }
}