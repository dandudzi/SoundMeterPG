package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

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

import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.ConnectionInternetDetector;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.*;

/**
 * Created by Daniel on 24.07.2016 :) at 12:11 :).
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityWorkCorrectlyTest {
    private Context context;
    private SharedPreferences prefs;
    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(
            SettingsActivity.class);

    @Before
    public void initValues() {
        context = mActivityRule.getActivity().getBaseContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity());
    }

    @Test
    public void isPrivateDataWorksCorrectly() {
        selectPreference(R.string.recording_audio_key_preference,prefs,context);
        uncheckPreference(R.string.private_data_key_preference,prefs,context);
        uncheckPreference(R.string.working_in_background_key_preference,prefs,context);

        isPreferenceNotEnabled(R.string.gps_key_preference,context);
        isPreferenceNotEnabled(R.string.internet_key_preference,context);
        isPreferenceNotEnabled(R.string.internal_storage_key_preference,context);

        selectPreference(R.string.private_data_key_preference,prefs,context);
        isPreferenceEnabled(R.string.gps_key_preference,context);
        isPreferenceEnabled(R.string.internet_key_preference,context);
        isPreferenceEnabled(R.string.internal_storage_key_preference,context);
    }

    @Test
    public void isWorkingInBackgroundWorksCorrectly() {
        selectPreference(R.string.recording_audio_key_preference,prefs,context);
        uncheckPreference(R.string.private_data_key_preference,prefs,context);
        uncheckPreference(R.string.working_in_background_key_preference,prefs,context);

        isPreferenceNotEnabled(R.string.gps_key_preference,context);
        isPreferenceNotEnabled(R.string.internet_key_preference,context);
        isPreferenceNotEnabled(R.string.internal_storage_key_preference,context);

        selectPreference(R.string.working_in_background_key_preference,prefs,context);
        isPreferenceNotEnabled(R.string.gps_key_preference,context);
        isPreferenceNotEnabled(R.string.internet_key_preference,context);
        isPreferenceEnabled(R.string.internal_storage_key_preference,context);
    }

    @Test
    public void isWorkingInBackgroundAndPrivateDataWorkCorrectly() {
        selectPreference(R.string.recording_audio_key_preference,prefs,context);
        selectPreference(R.string.private_data_key_preference,prefs,context);
        selectPreference(R.string.working_in_background_key_preference,prefs,context);

        isPreferenceEnabled(R.string.gps_key_preference,context);
        isPreferenceEnabled(R.string.internet_key_preference,context);
        isPreferenceEnabled(R.string.internal_storage_key_preference,context);
    }

    @Test
    public void isRecordingAudioWorksCorrectly() {
        uncheckPreference(R.string.recording_audio_key_preference,prefs,context);

        isPreferenceNotEnabled(R.string.private_data_key_preference,context);
        isPreferenceNotEnabled(R.string.working_in_background_key_preference,context);
        isPreferenceNotEnabled(R.string.gps_key_preference,context);
        isPreferenceNotEnabled(R.string.internet_key_preference,context);
        isPreferenceNotEnabled(R.string.internal_storage_key_preference,context);

        selectPreference(R.string.recording_audio_key_preference,prefs,context);

        isPreferenceEnabled(R.string.private_data_key_preference,context);
        isPreferenceEnabled(R.string.working_in_background_key_preference,context);
    }

    @Test
    public void isGPSWorksCorrectly(){
        selectPreference(R.string.recording_audio_key_preference,prefs,context);
        selectPreference(R.string.private_data_key_preference,prefs,context);
        selectPreference(R.string.working_in_background_key_preference,prefs,context);

        isPreferenceEnabled(R.string.gps_key_preference,context);

        uncheckPreference(R.string.gps_key_preference,prefs,context);
        isPreferenceNotChecked(R.string.gps_key_preference,context);

        selectPreference(R.string.gps_key_preference,prefs,context);
        LocationManager locationManager = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            isPreferenceChecked(R.string.gps_key_preference,context);
        else{
            onView(withText(R.string.no_gps_text_dialog)).check(matches(isCompletelyDisplayed()));
            onView(withId(android.R.id.button2)).perform(click());
            isPreferenceNotChecked(R.string.gps_key_preference,context);
        }
    }

    @Test
    public void isInternetWorksCorrectly(){
        selectPreference(R.string.recording_audio_key_preference,prefs,context);
        selectPreference(R.string.private_data_key_preference,prefs,context);
        selectPreference(R.string.working_in_background_key_preference,prefs,context);

        isPreferenceEnabled(R.string.internet_key_preference,context);

        uncheckPreference(R.string.internet_key_preference,prefs,context);
        isPreferenceNotChecked(R.string.internet_key_preference,context);

        selectPreference(R.string.internet_key_preference,prefs,context);
        ConnectionInternetDetector detector = new ConnectionInternetDetector(context);
        if(detector.isConnectingToInternet())
            isPreferenceChecked(R.string.internet_key_preference,context);
        else{
            onView(withText(R.string.no_internet_text_dialog)).check(matches(isCompletelyDisplayed()));
            onView(withId(android.R.id.button2)).perform(click());
            isPreferenceNotChecked(R.string.internet_key_preference,context);
        }
    }

    @Test
    public void isInternalStorageWorksCorrectly(){
        selectPreference(R.string.recording_audio_key_preference,prefs,context);
        selectPreference(R.string.private_data_key_preference,prefs,context);
        selectPreference(R.string.working_in_background_key_preference,prefs,context);

        isPreferenceEnabled(R.string.internal_storage_key_preference,context);

        uncheckPreference(R.string.internal_storage_key_preference,prefs,context);
        isPreferenceNotChecked(R.string.internal_storage_key_preference,context);

        selectPreference(R.string.internal_storage_key_preference,prefs,context);
        isPreferenceChecked(R.string.internal_storage_key_preference,context);
    }


}
