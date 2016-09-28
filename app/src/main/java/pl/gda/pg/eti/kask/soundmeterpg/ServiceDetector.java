package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;
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
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGPSEnabled(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);;
       return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
