package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dalvik.annotation.TestTarget;
import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;

import static android.R.attr.id;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pl.gda.pg.eti.kask.soundmeterpg.R.id.design_menu_item_text;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASUREMENTS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.SETTINGS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;

/**
 * Created by gierl on 09.12.2016.
 */
@RunWith(AndroidJUnit4.class)
public class MeasurementDisplayCorrectlyTest {
    private static Context context;
    private static UiDevice device;
    private static SharedPreferences prefs;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    @Before
    public   void openMeasurements() throws UiObjectNotFoundException {
        context = mActivityRule.getActivity().getBaseContext();
        prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openItemInDrawer(MEASUREMENTS_ITEM,device);
    }
    @Test
    public void isNullList(){
        //isDisplayCorrectly("Stored on server");
        isDisplayCorrectly("Date");
       isDisplayCorrectly("Average sound");
    }

    private  void isDisplayCorrectly(String text){
        onView(withText(text)).check(matches(isCompletelyDisplayed()));
    }

}
