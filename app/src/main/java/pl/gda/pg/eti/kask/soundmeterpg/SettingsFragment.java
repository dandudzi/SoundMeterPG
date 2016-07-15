package pl.gda.pg.eti.kask.soundmeterpg;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by Daniel on 14.07.2016.
 */
public class SettingsFragment extends PreferenceFragment {
    private CheckBoxPreference accessToInternalStoragePreference;
    private CheckBoxPreference workingInBackgroundPreference;
    private CheckBoxPreference processingPrivateDataPreference;
    private String keyProcessingPrivateData;
    private String keyWorkingInBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        initialCheckBoxesAndKeysPreference();
        setDependencyForAccessToInternalStoragePreference(createNewAccessToInternalStorageOnPreferenceChangeListener());
        setAvailabilityForAccessToInternalStoragePreference();

    }

   

    private void initialCheckBoxesAndKeysPreference() {
        String keyAccessToInternalStorage = getResources().getString(R.string.key_access_to_internal_storage_preference);
        keyProcessingPrivateData = getResources().getString(R.string.key_processing_private_data_preferenece);
        keyWorkingInBackground = getResources().getString(R.string.key_working_in_background_preferenece);

        accessToInternalStoragePreference = (CheckBoxPreference) findPreference(keyAccessToInternalStorage);
        workingInBackgroundPreference= (CheckBoxPreference)findPreference(keyWorkingInBackground);
        processingPrivateDataPreference = (CheckBoxPreference)findPreference(keyProcessingPrivateData);

    }

    private Preference.OnPreferenceChangeListener createNewAccessToInternalStorageOnPreferenceChangeListener() {
        Preference.OnPreferenceChangeListener newOnPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {
                CheckBoxPreference changedPreference = (CheckBoxPreference) preference;

                String keyChangedPreference = changedPreference.getKey();
                if(newValue.toString().equals("true"))
                    accessToInternalStoragePreference.setEnabled(true);
                else if(keyChangedPreference.equals(keyProcessingPrivateData)) {
                    if (workingInBackgroundPreference.isChecked() == false)
                        accessToInternalStoragePreference.setEnabled(false);
                }else if(keyChangedPreference.equals(keyWorkingInBackground)) {
                    if (processingPrivateDataPreference.isChecked() == false)
                        accessToInternalStoragePreference.setEnabled(false);
                }
                return true;
            }

        };
        return newOnPreferenceChangeListener;
    }

    private void setDependencyForAccessToInternalStoragePreference(Preference.OnPreferenceChangeListener newOnPreferenceChangeListener) {



        workingInBackgroundPreference.setOnPreferenceChangeListener(newOnPreferenceChangeListener);
        processingPrivateDataPreference.setOnPreferenceChangeListener(newOnPreferenceChangeListener);

    }

    private void setAvailabilityForAccessToInternalStoragePreference() {
        if(workingInBackgroundPreference.isChecked() || processingPrivateDataPreference.isChecked())
            accessToInternalStoragePreference.setEnabled(true);
        else
            accessToInternalStoragePreference.setEnabled(false);
    }
}


