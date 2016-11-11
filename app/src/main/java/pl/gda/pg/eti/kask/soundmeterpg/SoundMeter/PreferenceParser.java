package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.PreferenceManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by gierl on 25.08.2016.
 */
public class PreferenceParser implements PreferenceManager {
    private Context context;
    SharedPreferences preferences;


    public PreferenceParser(Context context) {
        this.context = context;
        preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    @Override
    public boolean hasPermissionToUseInternet() {
        if(Build.VERSION.SDK_INT >= 23)
            setPreferenceIfDifferentThanPermission(context.getResources().getString(R.string.internet_key_preference), false,isNotInternetAvailable());
        return preferences.getBoolean(context.getResources().getString(R.string.internet_key_preference), false);

    }

    @Override
    public boolean hasPermissionToUseGPS() {
        if(Build.VERSION.SDK_INT >= 23)
            setPreferenceIfDifferentThanPermission(context.getResources().getString(R.string.gps_key_preference), false,isNotGPSAvailable());
        return preferences.getBoolean(context.getResources().getString(R.string.gps_key_preference), false);
    }

    @Override
    public boolean hasPermissionToUseInternalStorage(){
        return preferences.getBoolean(context.getResources().getString(R.string.internal_storage_key_preference), true);
    }

    @Override
    public boolean hasPermissionToUseMicrophone(){
        if(Build.VERSION.SDK_INT >= 23)
            setPreferenceIfDifferentThanPermission(context.getResources().getString(R.string.recording_audio_key_preference), true,isNotMicrophoneAvailable());
        return preferences.getBoolean(context.getResources().getString(R.string.recording_audio_key_preference), true);
    }

    @Override
    public boolean hasPermissionToWorkInBackground() {
        return preferences.getBoolean(context.getResources().getString(R.string.working_in_background_key_preference), true);
    }
    @Override
    public boolean hasPermissionToSendToServer(){
        return preferences.getBoolean(context.getResources().getString(R.string.sending_measurement_key_preferenece),true);
    }

    @Override
    public int howManyMeasurementsInStorage() {
        return preferences.getInt(context.getResources().getString(R.string.measurements_in_storage_key_preference), 50);
    }

    public void setPreferenceIfDifferentThanPermission(String key, boolean defaultValue, boolean isServiceNotAvailable) {
        Boolean isAvailable = preferences.getBoolean(key,defaultValue);
        SharedPreferences.Editor editor = preferences.edit();
        if(!isAvailable && !isServiceNotAvailable)
            editor.putBoolean(key,false);
        else
            editor.putBoolean(key,!isServiceNotAvailable);

        editor.commit();
    }

    public void askUserForPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = new String[] {
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION};
            ArrayList<String> tmp = new ArrayList<>();
            for (String permission: permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    if ( ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                   } else
                        tmp.add(permission);
                }
            }
            if(tmp.size() > 0)
                ActivityCompat.requestPermissions(activity, tmp.toArray(new String[tmp.size()]), 1);
        }
    }

    public void setAllPreferenceLikePermission(){
        SharedPreferences.Editor editor = preferences.edit();
        String internet = context.getResources().getString(R.string.internet_key_preference);
        setPreferenceIfDifferentThanPermission(internet,false,isNotInternetAvailable());

        String microphone = context.getResources().getString(R.string.recording_audio_key_preference);
        setPreferenceIfDifferentThanPermission(microphone,true,isNotMicrophoneAvailable());

        String gps = context.getResources().getString(R.string.gps_key_preference);
        setPreferenceIfDifferentThanPermission(gps,false,isNotGPSAvailable());
    }

    public boolean isNotGPSAvailable() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    public boolean isNotMicrophoneAvailable() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;
    }

    public boolean isNotInternetAvailable() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED;
    }

    public void requestGPSPermission(Activity activity ){
        ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    public void requestInternetPermission(Activity activity ){
        ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.INTERNET}, 1);
    }

    public void requestMicrophonePermission(Activity activity ){
        ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.RECORD_AUDIO}, 1);
    }
}
