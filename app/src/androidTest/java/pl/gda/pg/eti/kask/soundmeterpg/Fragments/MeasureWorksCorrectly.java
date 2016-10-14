package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assume.assumeTrue;
import static pl.gda.pg.eti.kask.soundmeterpg.Fragments.MeasureTestHelper.turnOffMicrophonePermission;
import static pl.gda.pg.eti.kask.soundmeterpg.Fragments.MeasureTestHelper.turnAllSettingsPermission;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.TIME_OUT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOffGPS;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnAllPermission;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnGPS;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnInternetData;

/**
 * Created by Daniel on 06.10.2016 at 11:39 :).
 */
@RunWith(AndroidJUnit4.class)
public class MeasureWorksCorrectly {
    Context context;
    UiDevice device;
    SharedPreferences prefs;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public void setUp() throws UiObjectNotFoundException, InterruptedException {
        context = mActivityRule.getActivity().getBaseContext();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity());
        turnOnInternetData(device,context);
        turnOnGPS(device,context);
        turnOnAllPermission(device,context);
        turnAllSettingsPermission(device,prefs,context);
    }

    @Test
    public void workCorrectly() throws UiObjectNotFoundException {
        UiObject okButton = device.findObject(new UiSelector().text("OK"));

        turnOffGPS(device,context);
        onView(withId(R.id.measure_button_fragment)).perform(click());
        okButton.waitForExists(TIME_OUT);
        onView(withText(R.string.turn_off_location_title_measure)).check(matches(isCompletelyDisplayed()));
        onView(withText(R.string.turn_off_location_msg_measure)).check(matches(isCompletelyDisplayed()));
        onView(withText("OK")).perform(click());
        turnOnGPS(device,context);

        turnOffMicrophonePermission(device,prefs,context);
        onView(withId(R.id.measure_button_fragment)).perform(click());
        while(!okButton.waitForExists(TIME_OUT*2))
            onView(withId(R.id.measure_button_fragment)).perform(click());
        onView(withText(R.string.turn_off_microphone_msg_measure)).check(matches(isCompletelyDisplayed()));
        onView(withText(R.string.turn_off_microphone_title_measure)).check(matches(isCompletelyDisplayed()));
        onView(withText("OK")).perform(click());
        turnOffMicrophonePermission(device,prefs,context);
    }
}
