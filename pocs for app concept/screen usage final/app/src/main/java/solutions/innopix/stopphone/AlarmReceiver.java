package solutions.innopix.stopphone;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Screen on "+UtilityClass.totalHrs(context), Toast.LENGTH_SHORT).show();

        Intent i = new Intent(context, WarningActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
