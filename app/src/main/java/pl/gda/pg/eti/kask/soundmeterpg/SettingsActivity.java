package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

/**
 * Created by Daniel on 10.07.2016.
 */
public class SettingsActivity extends PreferenceActivity  {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.header_preferences, target);
    }


    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return SettingsActivity.class.getName().equals(fragmentName) ||
                SettingsFragment.class.getName().equals(fragmentName);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.fragment_preferences);
        }

    }
}
