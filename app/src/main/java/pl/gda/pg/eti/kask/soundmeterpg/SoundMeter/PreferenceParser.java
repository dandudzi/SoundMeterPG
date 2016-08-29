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
    public boolean hasPrivilegesToUseInternet() {
        return _preferences.getBoolean(_context.getResources().getString(R.string.internet_key_preference), false);

    }

    public boolean hasPrivilegesToUseGPS() {
        return _preferences.getBoolean(_context.getResources().getString(R.string.gps_key_preference), false);
    }
}
