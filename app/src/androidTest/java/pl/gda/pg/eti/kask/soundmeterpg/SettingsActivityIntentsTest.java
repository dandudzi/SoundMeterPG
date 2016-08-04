package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.isPreferenceChecked;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.isPreferenceEnabled;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.isPreferenceNotChecked;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.selectPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.TesterHelper.uncheckPreference;

/**
 * Created by Daniel on 24.07.2016 :).
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityIntentsTest {

    private Context context;
    private SharedPreferences prefs;
    private UiDevice device;
    @Rule
    public IntentsTestRule<SettingsActivity> mActivityRule = new IntentsTestRule<>(
            SettingsActivity.class);

    @Before
    public void initSettings() {
        context = mActivityRule.getActivity().getBaseContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity());
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void isGPSWorksCorrectly() {
        selectPreference(R.string.key_recording_audio_preference, prefs, context);
        selectPreference(R.string.key_private_data_preference, prefs, context);
        selectPreference(R.string.key_working_in_background_preference, prefs, context);

        isPreferenceEnabled(R.string.key_gps_preference, context);

        uncheckPreference(R.string.key_gps_preference, prefs, context);
        isPreferenceNotChecked(R.string.key_gps_preference, context);

        selectPreference(R.string.key_gps_preference, prefs, context);
        LocationManager locationManager = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            isPreferenceChecked(R.string.key_gps_preference, context);
        else {
            Intent resultData = new Intent();
            Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(0, resultData);

            onView(withText(R.string.text_no_gps_dialog)).check(matches(isCompletelyDisplayed()));
            onView(withId(android.R.id.button1)).perform(click());
            intending(allOf(hasAction("Settings.ACTION_LOCATION_SOURCE_SETTINGS"))).respondWith(result);
            device.pressBack();
            isPreferenceNotChecked(R.string.key_gps_preference, context);
        }
    }

    @Test
    public void isInternetWorksCorrectly() {
        selectPreference(R.string.key_recording_audio_preference, prefs, context);
        selectPreference(R.string.key_private_data_preference, prefs, context);
        selectPreference(R.string.key_working_in_background_preference, prefs, context);

        isPreferenceEnabled(R.string.key_internet_preference, context);

        uncheckPreference(R.string.key_internet_preference, prefs, context);
        isPreferenceNotChecked(R.string.key_internet_preference, context);

        selectPreference(R.string.key_internet_preference, prefs, context);
        ConnectionInternetDetector detector = new ConnectionInternetDetector(context);
        if (detector.isConnectingToInternet())
            isPreferenceChecked(R.string.key_internet_preference, context);
        else {
            Intent resultData = new Intent();
            Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(0, resultData);

            onView(withText(R.string.text_no_internet_dialog)).check(matches(isCompletelyDisplayed()));
            onView(withId(android.R.id.button1)).perform(click());
            intending(allOf(hasAction("Settings.ACTION_WIRELESS_SETTINGS"))).respondWith(result);
            device.pressBack();
            isPreferenceNotChecked(R.string.key_internet_preference, context);
        }
    }
}
