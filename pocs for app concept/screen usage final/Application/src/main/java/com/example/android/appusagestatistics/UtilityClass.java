package com.example.android.appusagestatistics;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class UtilityClass {
    private static class LastTimeLaunchedComparatorDesc implements Comparator<UsageStats> {
        @Override
        public int compare(UsageStats left, UsageStats right) {
            return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
        }
    }
    public  static   long totalHrs(Context context) {
        List<UsageStats> usageStatsList =
                getUsageStatistics(context, UsageStatsManager.INTERVAL_DAILY);
        Collections.sort(usageStatsList, new UtilityClass.LastTimeLaunchedComparatorDesc());
        List<CustomUsageStats> customUsageStatsList = new ArrayList<>();
        long totaltime = 0;
        for (int i = 0; i < usageStatsList.size(); i++) {
            CustomUsageStats customUsageStats = new CustomUsageStats();
            customUsageStats.appName = usageStatsList.get(i).getPackageName();
            try {
                Drawable appIcon =context.getPackageManager()
                        .getApplicationIcon(usageStatsList.get(i).getPackageName());
                customUsageStats.totalForgrond = usageStatsList.get(i).getTotalTimeInForeground();
                customUsageStats.appIcon = appIcon;
            } catch (PackageManager.NameNotFoundException e) {

                customUsageStats.totalForgrond = usageStatsList.get(i).getTotalTimeInForeground();
                customUsageStats.appIcon = context
                        .getDrawable(R.drawable.ic_default_app_launcher);
            }
            totaltime = totaltime + usageStatsList.get(i).getTotalTimeInForeground();
            if (TimeUnit.MILLISECONDS.toMinutes(usageStatsList.get(i).getTotalTimeInForeground()) > 0) {
                customUsageStatsList.add(customUsageStats);
            }
        }
        CustomUsageStats customUsageStats = new CustomUsageStats();
        customUsageStats.totalForgrond = totaltime;
        customUsageStats.appName = "Total time " + TimeUnit.MILLISECONDS.toHours(totaltime) + "";
        customUsageStatsList.add(0, customUsageStats);

        return  TimeUnit.MILLISECONDS.toHours(totaltime);
    }
    public  static List<UsageStats> getUsageStatistics(Context context,int intervalType) {
        UsageStatsManager  mUsageStatsManager = (UsageStatsManager) context
                .getSystemService(Context.USAGE_STATS_SERVICE);
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        return queryUsageStats;
    }
}
