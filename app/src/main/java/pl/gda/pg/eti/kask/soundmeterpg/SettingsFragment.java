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

    public static final int REQUEST_CODE_INTERNET =4991 ;
    public static final int REQUEST_CODE_GPS =1994 ;

    private CheckBoxPreference accessToInternalStoragePreference;
    private CheckBoxPreference workingInBackgroundPreference;
    private CheckBoxPreference processingPrivateDataPreference;
    private CheckBoxPreference accessToGPSPreference;
    private CheckBoxPreference accessToInternetPreference;
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
        accessToGPSPreference.setOnPreferenceChangeListener(listener);

        activity = SettingsFragment.this.getActivity();
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        changeCheckboxAccessToGPSValue();

        listener =  createPreferenceChangeListenerAccessToInternet();
        accessToInternetPreference.setOnPreferenceChangeListener(listener);

        internetDetector = new ConnectionInternetDetector(activity.getBaseContext());
        changeCheckboxAccessToInternetValue();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            switch (requestCode) {
                case REQUEST_CODE_GPS:
                    if(locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
                        accessToGPSPreference.setChecked(true);
                    else
                        accessToGPSPreference.setChecked(false);
                    break;

                case  REQUEST_CODE_INTERNET:
                    if(internetDetector.isConnectingToInternet())
                        accessToInternetPreference.setChecked(true);
                    else
                        accessToInternetPreference.setChecked(false);
                    break;
            }
        }

    }



    private void initialCheckBoxesAndKeysPreference() {
        Resources resources = getResources();
        String keyAccessToInternalStorage = resources.getString(R.string.key_access_to_internal_storage_preference);
        keyProcessingPrivateData = resources.getString(R.string.key_processing_private_data_preference);
        keyWorkingInBackground = resources.getString(R.string.key_working_in_background_preference);
        String keyAccessToGPS = resources.getString(R.string.key_access_to_gps_preference);
        String keyAccessToInternet = resources.getString(R.string.key_access_to_internet_preference);

        accessToInternalStoragePreference = (CheckBoxPreference) findPreference(keyAccessToInternalStorage);
        workingInBackgroundPreference= (CheckBoxPreference)findPreference(keyWorkingInBackground);
        processingPrivateDataPreference = (CheckBoxPreference)findPreference(keyProcessingPrivateData);
        accessToGPSPreference = (CheckBoxPreference) findPreference(keyAccessToGPS);
        accessToInternetPreference = (CheckBoxPreference) findPreference(keyAccessToInternet);
    }

    private Preference.OnPreferenceChangeListener createNewPreferenceChangeListenerAccessToInternalStorageOn() {
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {
                CheckBoxPreference changedPreference = (CheckBoxPreference) preference;

                String keyChangedPreference = changedPreference.getKey();
                if(newValue.toString().equals("true"))
                    accessToInternalStoragePreference.setEnabled(true);
                else if(keyChangedPreference.equals(keyProcessingPrivateData)) {
                    if (!workingInBackgroundPreference.isChecked())
                        accessToInternalStoragePreference.setEnabled(false);
                }else if(keyChangedPreference.equals(keyWorkingInBackground)) {
                    if (!processingPrivateDataPreference.isChecked())
                        accessToInternalStoragePreference.setEnabled(false);
                }
                return true;
            }

        };
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

    private  Preference.OnPreferenceChangeListener createPreferenceChangeListenerAccessToGPS(){
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().equals("true")) {
                    if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        AlertDialog alert = BuilderAlertDialog.createNoGPSDialog(activity, SettingsFragment.this);
                        alert.show();
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private void changeCheckboxAccessToGPSValue() {
        if(locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )&& accessToGPSPreference.isChecked())
            accessToGPSPreference.setChecked(true);
        else
            accessToGPSPreference.setChecked(false);
    }

    private Preference.OnPreferenceChangeListener createPreferenceChangeListenerAccessToInternet() {
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().equals("true")) {
                    if (!internetDetector.isConnectingToInternet()) {
                        AlertDialog alert = BuilderAlertDialog.createNoInternetDialog(activity, SettingsFragment.this);
                        alert.show();
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private void changeCheckboxAccessToInternetValue() {
        if(internetDetector.isConnectingToInternet() && accessToInternetPreference.isChecked())
            accessToInternetPreference.setChecked(true);
        else
            accessToInternetPreference.setChecked(false);
    }


}


