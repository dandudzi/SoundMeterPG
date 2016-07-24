package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.isPreferenceChecked;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.isPreferenceEnabled;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.isPreferenceNotChecked;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.isPreferenceNotEnabled;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.selectPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.uncheckPreference;

/**
 * Created by Daniel on 24.07.2016 :).
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityWorkCorrectlyTest {
    private Context context;
    private SharedPreferences prefs;
    @Rule
    public ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(
            SettingsActivity.class);

    @Before
    public void initSettings() {
        context = mActivityRule.getActivity().getBaseContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity());
    }

    @Test
    public void isPrivateDataWorksCorrectly() {
        selectPreference(R.string.key_recording_audio_preference,prefs,context);
        uncheckPreference(R.string.key_private_data_preference,prefs,context);
        uncheckPreference(R.string.key_working_in_background_preference,prefs,context);

        isPreferenceNotEnabled(R.string.key_gps_preference,context);
        isPreferenceNotEnabled(R.string.key_internet_preference,context);
        isPreferenceNotEnabled(R.string.key_internal_storage_preference,context);

        selectPreference(R.string.key_private_data_preference,prefs,context);
        isPreferenceEnabled(R.string.key_gps_preference,context);
        isPreferenceEnabled(R.string.key_internet_preference,context);
        isPreferenceEnabled(R.string.key_internal_storage_preference,context);
    }

    @Test
    public void isWorkingInBackgroundWorksCorrectly() {
        selectPreference(R.string.key_recording_audio_preference,prefs,context);
        uncheckPreference(R.string.key_private_data_preference,prefs,context);
        uncheckPreference(R.string.key_working_in_background_preference,prefs,context);

        isPreferenceNotEnabled(R.string.key_gps_preference,context);
        isPreferenceNotEnabled(R.string.key_internet_preference,context);
        isPreferenceNotEnabled(R.string.key_internal_storage_preference,context);

        selectPreference(R.string.key_working_in_background_preference,prefs,context);
        isPreferenceNotEnabled(R.string.key_gps_preference,context);
        isPreferenceNotEnabled(R.string.key_internet_preference,context);
        isPreferenceEnabled(R.string.key_internal_storage_preference,context);
    }

    @Test
    public void isWorkingInBackgroundAndPrivateDataWorkCorrectly() {
        selectPreference(R.string.key_recording_audio_preference,prefs,context);
        selectPreference(R.string.key_private_data_preference,prefs,context);
        selectPreference(R.string.key_working_in_background_preference,prefs,context);

        isPreferenceEnabled(R.string.key_gps_preference,context);
        isPreferenceEnabled(R.string.key_internet_preference,context);
        isPreferenceEnabled(R.string.key_internal_storage_preference,context);
    }

    @Test
    public void isRecordingAudioWorksCorrectly() {
        uncheckPreference(R.string.key_recording_audio_preference,prefs,context);

        isPreferenceNotEnabled(R.string.key_private_data_preference,context);
        isPreferenceNotEnabled(R.string.key_working_in_background_preference,context);
        isPreferenceNotEnabled(R.string.key_gps_preference,context);
        isPreferenceNotEnabled(R.string.key_internet_preference,context);
        isPreferenceNotEnabled(R.string.key_internal_storage_preference,context);

        selectPreference(R.string.key_recording_audio_preference,prefs,context);

        isPreferenceEnabled(R.string.key_private_data_preference,context);
        isPreferenceEnabled(R.string.key_working_in_background_preference,context);
    }

    @Test
    public void isGPSWorksCorrectly(){
        selectPreference(R.string.key_recording_audio_preference,prefs,context);
        selectPreference(R.string.key_private_data_preference,prefs,context);
        selectPreference(R.string.key_working_in_background_preference,prefs,context);

        isPreferenceEnabled(R.string.key_gps_preference,context);

        uncheckPreference(R.string.key_gps_preference,prefs,context);
        isPreferenceNotChecked(R.string.key_gps_preference,context);

        selectPreference(R.string.key_gps_preference,prefs,context);
        LocationManager locationManager = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            isPreferenceChecked(R.string.key_gps_preference,context);
        else{
            onView(withText(R.string.text_no_gps_dialog)).check(matches(isCompletelyDisplayed()));
            onView(withId(android.R.id.button2)).perform(click());
            isPreferenceNotChecked(R.string.key_gps_preference,context);
        }
    }

    @Test
    public void isInternetWorksCorrectly(){
        selectPreference(R.string.key_recording_audio_preference,prefs,context);
        selectPreference(R.string.key_private_data_preference,prefs,context);
        selectPreference(R.string.key_working_in_background_preference,prefs,context);

        isPreferenceEnabled(R.string.key_internet_preference,context);

        uncheckPreference(R.string.key_internet_preference,prefs,context);
        isPreferenceNotChecked(R.string.key_internet_preference,context);

        selectPreference(R.string.key_internet_preference,prefs,context);
        ConnectionInternetDetector detector = new ConnectionInternetDetector(context);
        if(detector.isConnectingToInternet())
            isPreferenceChecked(R.string.key_internet_preference,context);
        else{
            onView(withText(R.string.text_no_internet_dialog)).check(matches(isCompletelyDisplayed()));
            onView(withId(android.R.id.button2)).perform(click());
            isPreferenceNotChecked(R.string.key_internet_preference,context);
        }
    }

    @Test
    public void isInternalStorageWorksCorrectly(){
        selectPreference(R.string.key_recording_audio_preference,prefs,context);
        selectPreference(R.string.key_private_data_preference,prefs,context);
        selectPreference(R.string.key_working_in_background_preference,prefs,context);

        isPreferenceEnabled(R.string.key_internal_storage_preference,context);

        uncheckPreference(R.string.key_internal_storage_preference,prefs,context);
        isPreferenceNotChecked(R.string.key_internal_storage_preference,context);

        selectPreference(R.string.key_internal_storage_preference,prefs,context);
        isPreferenceChecked(R.string.key_internal_storage_preference,context);
    }


}