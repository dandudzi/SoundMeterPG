package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;

/**
 * Created by gierl on 17.08.2016.
 */
public class ServiceDetector {

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.e("Service name = ", serviceClass.getName());
            String text = serviceClass.getName();
            String text2 = service.service.getClassName();
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isActivityRunning(Class<?> serviceClass, Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals("pl.gda.pg.eti.kask.soundmeterpg")) {
                return true;
            }
        }
        return false;
    }
}
