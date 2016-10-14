package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pl.gda.pg.eti.kask.soundmeterpg.Fragments.MeasureTestHelper.turnAllSettingsPermission;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnAllPermission;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnGPS;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnInternetData;

/**
 * Created by Daniel on 14.10.2016.
 */
@RunWith(AndroidJUnit4.class)
public class MeasureDisplayCorrectlyTest extends MeasureDisplayCorrectly {

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
        onView(withText("Start")).perform(click());
    }
}
