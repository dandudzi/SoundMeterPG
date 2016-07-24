package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Daniel on 14.07.2016.
 */
public class SettingsFragment extends PreferenceFragment {
    

    private CheckBoxPreference accessToInternalStorage;
    private CheckBoxPreference workingInBackground;
    private CheckBoxPreference processingPrivateData;
    private CheckBoxPreference accessToGPS;
    private CheckBoxPreference accessToInternet;
    private String keyProcessingPrivateData;
    private String keyWorkingInBackground;
    private Activity activity;
    private LocationManager locationManager;
    private ConnectionInternetDetector internetDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        initialCheckBoxesAndKeysPreference();

        Preference.OnPreferenceChangeListener listener = createNewPreferenceChangeListenerAccessToInternalStorageOn();
        setDependencyForAccessToInternalStoragePreference(listener);
        setAvailabilityForAccessToInternalStoragePreference();

        listener =  createPreferenceChangeListenerAccessToGPS();
        accessToGPS.setOnPreferenceChangeListener(listener);

        listener =  createPreferenceChangeListenerAccessToInternet();
        accessToInternet.setOnPreferenceChangeListener(listener);

        activity = SettingsFragment.this.getActivity();
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        internetDetector = new ConnectionInternetDetector(activity.getBaseContext());

        setCheckboxInternetAndGpsStartingValueDependentOnAccessToService();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            switch (requestCode) {
                case FactorAlertDialog.REQUEST_CODE_GPS:
                    setGPSCheckboxSelectBasedOnResultUserAction();
                    break;

                case  FactorAlertDialog.REQUEST_CODE_INTERNET:
                    setInternetCheckboxSelectBasedOnResultUserAction();
                    break;
            }
        }

    }

    private void setInternetCheckboxSelectBasedOnResultUserAction() {
        if(internetDetector.isConnectingToInternet())
            accessToInternet.setChecked(true);
        else
            accessToInternet.setChecked(false);
    }

    private void setGPSCheckboxSelectBasedOnResultUserAction() {
        if(locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            accessToGPS.setChecked(true);
        else
            accessToGPS.setChecked(false);
    }

    private void initialCheckBoxesAndKeysPreference() {
        Resources resources = getResources();
        String keyAccessToInternalStorage = resources.getString(R.string.key_internal_storage_preference);
        keyProcessingPrivateData = resources.getString(R.string.key_private_data_preference);
        keyWorkingInBackground = resources.getString(R.string.key_working_in_background_preference);
        String keyAccessToGPS = resources.getString(R.string.key_gps_preference);
        String keyAccessToInternet = resources.getString(R.string.key_internet_preference);

        accessToInternalStorage = (CheckBoxPreference) findPreference(keyAccessToInternalStorage);
        workingInBackground = (CheckBoxPreference)findPreference(keyWorkingInBackground);
        processingPrivateData = (CheckBoxPreference)findPreference(keyProcessingPrivateData);
        accessToGPS = (CheckBoxPreference) findPreference(keyAccessToGPS);
        accessToInternet = (CheckBoxPreference) findPreference(keyAccessToInternet);
    }

    private Preference.OnPreferenceChangeListener createNewPreferenceChangeListenerAccessToInternalStorageOn() {
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {
                CheckBoxPreference changedPreference = (CheckBoxPreference) preference;

                String keyChangedPreference = changedPreference.getKey();
                if(newValue.toString().equals("true"))
                    accessToInternalStorage.setEnabled(true);
                else if(keyChangedPreference.equals(keyProcessingPrivateData)) {
                    if (!workingInBackground.isChecked())
                        accessToInternalStorage.setEnabled(false);
                }else if(keyChangedPreference.equals(keyWorkingInBackground)) {
                    if (!processingPrivateData.isChecked())
                        accessToInternalStorage.setEnabled(false);
                }
                return true;
            }

        };
    }

    private void setDependencyForAccessToInternalStoragePreference(Preference.OnPreferenceChangeListener newOnPreferenceChangeListener) {
        workingInBackground.setOnPreferenceChangeListener(newOnPreferenceChangeListener);
        processingPrivateData.setOnPreferenceChangeListener(newOnPreferenceChangeListener);
    }

    private void setAvailabilityForAccessToInternalStoragePreference() {
        if(workingInBackground.isChecked() || processingPrivateData.isChecked())
            accessToInternalStorage.setEnabled(true);
        else
            accessToInternalStorage.setEnabled(false);
    }

    private Preference.OnPreferenceChangeListener createPreferenceChangeListenerAccessToGPS(){
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().equals("true")) {
                    if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        AlertDialog alert = FactorAlertDialog.createNoGPSDialog(activity, SettingsFragment.this);
                        alert.show();
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private Preference.OnPreferenceChangeListener createPreferenceChangeListenerAccessToInternet() {
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().equals("true")) {
                    if (!internetDetector.isConnectingToInternet()) {
                        AlertDialog alert = FactorAlertDialog.createNoInternetDialog(activity, SettingsFragment.this);
                        alert.show();
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private void setCheckboxInternetAndGpsStartingValueDependentOnAccessToService() {
        if(internetDetector.isConnectingToInternet() && accessToInternet.isChecked())
            accessToInternet.setChecked(true);
        else
            accessToInternet.setChecked(false);

        if(locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )&& accessToGPS.isChecked())
            accessToGPS.setChecked(true);
        else
            accessToGPS.setChecked(false);
    }


}


