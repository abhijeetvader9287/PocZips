package solutions.innopix.geoprofiler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 13456;

    @Override
    public void onReceive(Context context, Intent intent) {
        BackgroundService.getLocation();
    }

}