package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

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

import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.*;

/**
 * Created by Daniel on 24.07.2016 :) at 12:11 :).
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityIntentsTest {

    private Context context;
    private SharedPreferences prefs;
    private UiDevice device;
    @Rule
    public final IntentsTestRule<SettingsActivity> mActivityRule = new IntentsTestRule<>(
            SettingsActivity.class);

    @Before
    public void initValues() {
        context = mActivityRule.getActivity().getBaseContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity());
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void isGPSWorksCorrectly(){
        isPreferenceWorkCorrectly(R.string.gps_key_preference);

        LocationManager locationManager = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isServiceEnable = locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );

        isIntentInPreferenceDialogWorksCorrectly(R.string.gps_key_preference,isServiceEnable,
                R.string.no_gps_text_dialog,"Settings.ACTION_LOCATION_SOURCE_SETTINGS");
    }

    @Test
    public void isInternetWorksCorrectly(){
        isPreferenceWorkCorrectly(R.string.internet_key_preference);

        ConnectionInternetDetector detector = new ConnectionInternetDetector(context);
        boolean isServiceEnable = detector.isConnectingToInternet();

        isIntentInPreferenceDialogWorksCorrectly(R.string.internet_key_preference,isServiceEnable,
                R.string.no_internet_text_dialog,"Settings.ACTION_WIRELESS_SETTINGS");
    }

    private void isPreferenceWorkCorrectly(int stringKey){
        selectPreference(R.string.recording_audio_key_preference,prefs,context);
        selectPreference(R.string.private_data_key_preference,prefs,context);
        selectPreference(R.string.working_in_background_key_preference,prefs,context);

        isPreferenceEnabled(stringKey,context);

        uncheckPreference(stringKey,prefs,context);
        isPreferenceNotChecked(stringKey,context);

        selectPreference(stringKey,prefs,context);
    }

    private void isIntentInPreferenceDialogWorksCorrectly(int stringKey, boolean isServiceEnable, int stringTextNoService, String action){
        if(isServiceEnable)
            isPreferenceChecked(stringKey,context);
        else{
            Intent resultData =  new Intent();
            Instrumentation.ActivityResult result =  new Instrumentation.ActivityResult(0,resultData);

            onView(withText(stringTextNoService)).check(matches(isCompletelyDisplayed()));
            onView(withId(android.R.id.button1)).perform(click());
            intending(allOf(hasAction(action))).respondWith(result);
            try {
                //Trzeba trochę poczekąć aż nowa aktywność się otworzy :(
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            device.pressBack();
            isPreferenceNotChecked(stringKey,context);
        }
    }
}
