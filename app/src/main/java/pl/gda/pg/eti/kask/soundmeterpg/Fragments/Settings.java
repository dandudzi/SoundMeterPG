package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.NoGPS;
import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.NoInternet;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SeekBarPreference;
import pl.gda.pg.eti.kask.soundmeterpg.Services.ServiceDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

/**
 * Created by Daniel on 14.07.2016 at 12:11 :).
 */
public class Settings extends PreferenceFragment {
    private CheckBoxPreference internalStoragePreference;
    private CheckBoxPreference workingInBackground;
    private CheckBoxPreference privateDataPreference;
    private CheckBoxPreference GPSPreference;
    private CheckBoxPreference internetPreference;
    private CheckBoxPreference recordingPreference;
    private String privateDataKey;
    private String workingInBackgroundKey;
    private Activity activity;
    private ConnectionInternetDetector internetDetector;
    private PreferenceParser preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        initialCheckBoxesAndKeysPreference();

        Preference.OnPreferenceChangeListener listener = createListenerAccessToInternalStorage();
        setDependencyForAccessToInternalStoragePreference(listener);
        setAvailabilityForAccessToInternalStoragePreference();

        listener =  createListenerAccessToGPS();
        GPSPreference.setOnPreferenceChangeListener(listener);

        listener =  createListenerAccessToInternet();
        internetPreference.setOnPreferenceChangeListener(listener);

        listener = createListenerAccessToMicrophone();
        recordingPreference.setOnPreferenceChangeListener(listener);

        activity = getActivity();
        internetDetector = new ConnectionInternetDetector(activity.getBaseContext());
        preference = new PreferenceParser(activity);
        setCheckboxInternetAndGpsStartingValueDependentOnAccessToService();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            switch (requestCode) {
                case NoGPS.REQUEST_CODE_GPS:
                    setGPSCheckboxSelectBasedOnResultUserAction();
                    break;

                case  NoInternet.REQUEST_CODE_INTERNET:
                    setInternetCheckboxSelectBasedOnResultUserAction();
                    break;
            }
        }

    }

    private void setInternetCheckboxSelectBasedOnResultUserAction() {
        if(internetDetector.isConnectingToInternet())
            internetPreference.setChecked(true);
        else
            internetPreference.setChecked(false);
    }

    private void setGPSCheckboxSelectBasedOnResultUserAction() {
        if(ServiceDetector.isGPSEnabled(activity.getBaseContext()))
            GPSPreference.setChecked(true);
        else
            GPSPreference.setChecked(false);
    }

    private void initialCheckBoxesAndKeysPreference() {
        Resources resources = getResources();
        String keyAccessToInternalStorage = resources.getString(R.string.internal_storage_key_preference);
        privateDataKey = resources.getString(R.string.private_data_key_preference);
        workingInBackgroundKey = resources.getString(R.string.working_in_background_key_preference);
        String keyAccessToGPS = resources.getString(R.string.gps_key_preference);
        String keyAccessToInternet = resources.getString(R.string.internet_key_preference);
        String keyRecording = resources.getString(R.string.recording_audio_key_preference);

        internalStoragePreference = (CheckBoxPreference) findPreference(keyAccessToInternalStorage);
        workingInBackground = (CheckBoxPreference)findPreference(workingInBackgroundKey);
        privateDataPreference = (CheckBoxPreference)findPreference(privateDataKey);
        GPSPreference = (CheckBoxPreference) findPreference(keyAccessToGPS);
        internetPreference = (CheckBoxPreference) findPreference(keyAccessToInternet);
        recordingPreference = (CheckBoxPreference) findPreference(keyRecording);
    }

    private Preference.OnPreferenceChangeListener createListenerAccessToInternalStorage() {
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {
                CheckBoxPreference changedPreference = (CheckBoxPreference) preference;

                String keyChangedPreference = changedPreference.getKey();
                if(newValue.toString().equals("true"))
                    internalStoragePreference.setEnabled(true);
                else if(keyChangedPreference.equals(privateDataKey)) {
                    if (!workingInBackground.isChecked())
                        internalStoragePreference.setEnabled(false);
                }else if(keyChangedPreference.equals(workingInBackgroundKey)) {
                    if (!privateDataPreference.isChecked())
                        internalStoragePreference.setEnabled(false);
                }
                return true;
            }

        };
    }

    private void setDependencyForAccessToInternalStoragePreference(Preference.OnPreferenceChangeListener newOnPreferenceChangeListener) {
        workingInBackground.setOnPreferenceChangeListener(newOnPreferenceChangeListener);
        privateDataPreference.setOnPreferenceChangeListener(newOnPreferenceChangeListener);
    }

    private void setAvailabilityForAccessToInternalStoragePreference() {
        if(workingInBackground.isChecked() || privateDataPreference.isChecked())
            internalStoragePreference.setEnabled(true);
        else
            internalStoragePreference.setEnabled(false);
    }

    private Preference.OnPreferenceChangeListener createListenerAccessToGPS(){
            return new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(newValue.toString().equals("true")) {
                        if(Build.VERSION.SDK_INT >= 23)
                            if(Settings.this.preference.isNotGPSAvailable()){
                                Settings.this.preference.requestGPSPermission(activity);
                                return  false;
                            }
                        if (!ServiceDetector.isGPSEnabled(activity.getBaseContext())) {
                            AlertDialog alert = NoGPS.create(activity, Settings.this);
                            Log.i("NoGPS","Opening Dialog");
                            alert.show();
                            return false;
                        }
                    }
                    return true;
            }
        };
    }

    private Preference.OnPreferenceChangeListener createListenerAccessToInternet() {

        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().equals("true")) {
                    if(Build.VERSION.SDK_INT >= 23)
                        if(Settings.this.preference.isNotInternetAvailable()){
                            Settings.this.preference.requestInternetPermission(activity);
                            return  false;
                        }
                    if (!internetDetector.isConnectingToInternet()) {
                        AlertDialog alert = NoInternet.create(activity, Settings.this);
                        Log.i("NoInternet","Opening Dialog");
                        alert.show();
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private Preference.OnPreferenceChangeListener createListenerAccessToMicrophone() {

        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().equals("true")) {
                    if(Build.VERSION.SDK_INT >= 23)
                        if(Settings.this.preference.isNotMicrophoneAvailable()) {
                            Settings.this.preference.requestMicrophonePermission(activity);
                            return  false;
                        }
                    recordingPreference.setChecked(true);
                }else
                    recordingPreference.setChecked(false);

                return true;
            }
        };
    }

    private void setCheckboxInternetAndGpsStartingValueDependentOnAccessToService() {
        if(internetDetector.isConnectingToInternet() && internetPreference.isChecked())
            internetPreference.setChecked(true);
        else
            internetPreference.setChecked(false);

        if(ServiceDetector.isGPSEnabled(activity.getBaseContext()) && GPSPreference.isChecked())
            GPSPreference.setChecked(true);
        else
            GPSPreference.setChecked(false);
    }


}


