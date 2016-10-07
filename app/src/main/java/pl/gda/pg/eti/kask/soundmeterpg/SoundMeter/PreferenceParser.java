package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.content.Context;
import android.content.SharedPreferences;

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
        return preferences.getBoolean(context.getResources().getString(R.string.internet_key_preference), false);

    }
    @Override
    public boolean hasPermissionToUseGPS() {
        return preferences.getBoolean(context.getResources().getString(R.string.gps_key_preference), false);
    }

    @Override
    public boolean hasPermissionToUseInternalStorage(){
        return preferences.getBoolean(context.getResources().getString(R.string.internal_storage_key_preference), true);
    }

    @Override
    public boolean hasPermissionToUseMicrophone(){
        return preferences.getBoolean(context.getResources().getString(R.string.recording_audio_key_preference), true);
    }

    @Override
    public boolean hasPermissionToWorkInBackground() {
        return preferences.getBoolean(context.getResources().getString(R.string.working_in_background_key_preference), true);
    }

    @Override
    public int howManyMeasurementsInStorage() {
        return preferences.getInt(context.getResources().getString(R.string.measurements_in_storage_key_preference), 50);
    }
}
