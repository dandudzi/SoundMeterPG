package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.FlakyTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.TimeoutException;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.MeasurementDataBaseManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pl.gda.pg.eti.kask.soundmeterpg.Fragments.MeasureTestHelper.turnAllSettingsPermission;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.selectPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.uncheckPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.SETTINGS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.TIME_OUT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOffGPS;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnAllPermission;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnGPS;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnInternetData;

/**
 * Created by gierl on 08.12.2016.
 */
@RunWith(AndroidJUnit4.class)
public class MeasureTest {

    private static UiDevice device = null;
    private static SharedPreferences sharedPreferences;
    private Context context;
    private DataBaseHandler dataBaseHandler;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initSettings() {


        context = mActivityRule.getActivity().getBaseContext();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        context.deleteDatabase(context.getResources().getString(R.string.database_name));
        dataBaseHandler = new DataBaseHandler(context, context.getResources().getString(R.string.database_name));

    }

@Test
public void isServiceWorking() throws UiObjectNotFoundException, InterruptedException {
    openItemInDrawer(SETTINGS_ITEM,device);
    turnAllSettingsPermission(device, sharedPreferences, context);
    device.pressBack();
    UiObject startBtn = device.findObject(new UiSelector().resourceId("pl.gda.pg.eti.kask.soundmeterpg:id/measure_button_fragment"));
    startBtn.click();
    Thread.sleep(2000);
    Assert.assertTrue(ServiceDetector.isMyServiceRunning(Measure.class, context));
    startBtn.click();
    Thread.sleep(2000);
    Assert.assertFalse(ServiceDetector.isMyServiceRunning(Measure.class, context));

}


    @Test
    public void isStoringInListWhenHavePermissionTest() throws UiObjectNotFoundException, InterruptedException {
        openItemInDrawer(SETTINGS_ITEM,device);
        selectPreference(R.string.internal_storage_key_preference,sharedPreferences,context);
        context.deleteDatabase(context.getResources().getString(R.string.database_name));
        UiObject startBtn = device.findObject(new UiSelector().resourceId("pl.gda.pg.eti.kask.soundmeterpg:id/measure_button_fragment"));
        device.pressBack();
        startBtn.click();
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Measure.class, context));
        Thread.sleep(3000);
        startBtn.click();
        Thread.sleep(2000);
        Assert.assertNotNull(dataBaseHandler.getLastAddedRow());

    }
    @Test
    public void isNotStoringInListWhenNotHavePermissionTest() throws UiObjectNotFoundException, InterruptedException {
        openItemInDrawer(SETTINGS_ITEM,device);
        uncheckPreference(R.string.internal_storage_key_preference,sharedPreferences,context);
        context.deleteDatabase(context.getResources().getString(R.string.database_name));
        UiObject startBtn = device.findObject(new UiSelector().resourceId("pl.gda.pg.eti.kask.soundmeterpg:id/measure_button_fragment"));
       device.pressBack();
        startBtn.click();
        Thread.sleep(4000);
        startBtn.click();
        Assert.assertNull(dataBaseHandler.getLastAddedRow());



    }

}
