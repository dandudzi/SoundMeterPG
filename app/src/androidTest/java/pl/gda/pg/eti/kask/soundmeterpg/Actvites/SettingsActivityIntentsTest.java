package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.findPreferencesOnView;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.isPreferenceChecked;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.isPreferenceEnabled;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.isPreferenceNotChecked;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.selectPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.uncheckPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.TIME_OUT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOffGPS;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOffInternetData;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnGPS;

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
    public void setUp() throws UiObjectNotFoundException {
        context = mActivityRule.getActivity().getBaseContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity());
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        turnOffGPS(device,context);
        turnOffInternetData(device,context);
    }

    @Test
    public void isGPSWorksCorrectly() throws UiObjectNotFoundException, InterruptedException {
        SettingsActivityWorkCorrectlyTest.setUpGPSPreference(prefs,context);
        selectPreference(R.string.gps_key_preference,prefs,context);

        onView(withText("Yes")).perform(click());
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(0, resultData);

        intending(allOf(hasAction("android.settings.LOCATION_SOURCE_SETTINGS"))).respondWith(result);
        device.pressBack();
    }



    @Test
    public void isInternetWorksCorrectly() throws UiObjectNotFoundException {
        SettingsActivityWorkCorrectlyTest.setUpInternetPreference(prefs,context);
        selectPreference(R.string.internet_key_preference,prefs,context);

        onView(withText("Yes")).perform(click());
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(0, resultData);

        intending(allOf(hasAction("Settings.ACTION_WIRELESS_SETTINGS"))).respondWith(result);
        device.pressBack();
    }


}
