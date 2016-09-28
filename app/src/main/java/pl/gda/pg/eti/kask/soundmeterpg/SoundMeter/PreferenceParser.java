package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.PreferenceManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by gierl on 25.08.2016.
 */
public class PreferenceParser implements PreferenceManager {
    private Context _context;
    SharedPreferences _preferences;

    public PreferenceParser(Context context) {
        _context = context;
        _preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(_context);
    }

    @Override
    public boolean hasPermissionToUseInternet() {
        return _preferences.getBoolean(_context.getResources().getString(R.string.internet_key_preference), false);

    }
    @Override
    public boolean hasPermissionToUseGPS() {
        return _preferences.getBoolean(_context.getResources().getString(R.string.gps_key_preference), false);
    }

    @Override
    public boolean hasPermissionToUseInternalStorage(){
        return _preferences.getBoolean(_context.getResources().getString(R.string.internal_storage_key_preference), false);
    }

    @Override
    public boolean hasPermissionToUseMicrophone(){
        return _preferences.getBoolean(_context.getResources().getString(R.string.recording_audio_key_preference), false);
    }
}
