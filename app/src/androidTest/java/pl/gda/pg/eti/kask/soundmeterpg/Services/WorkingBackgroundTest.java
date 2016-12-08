package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;

import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.selectPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.uncheckPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.SETTINGS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByRecentApps;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;

/**
 * Created by gierl on 08.12.2016.
 */

public class WorkingBackgroundTest {
    private static Sender sender;
    private static ConnectionInternetDetector connectionInternetDetector;
    private static MeasurementStatistics measurementStatistics = new MeasurementStatistics();
    private static Date date = new Date();
    private static final Location CORRECT_LOCATION = new Location(Location.MAX_LATITUDE,Location.MAX_LONGITUDE);
    private static String SITE = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.ping_site);
    private static UiDevice device = null;
    private static SharedPreferences sharedPreferences;
    private static Intent intent;
    private Context context;
    private DataBaseHandler dataBaseHandler;
    private Measurement measurement;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initSettings() {


        context = mActivityRule.getActivity().getBaseContext();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        intent = new Intent(context, Sender.class);
        context.deleteDatabase(context.getResources().getString(R.string.database_name));
        dataBaseHandler = new DataBaseHandler(context, context.getResources().getString(R.string.database_name));
        //  context.startService(intent);
    }

    @Test
    public void isServiceWorking() throws UiObjectNotFoundException, InterruptedException, RemoteException {

       //Uruchomienie glownego serwisu i wyrazenie zgody na prace w tle. Wyjscie z apki

        UiObject startBtn = device.findObject(new UiSelector().resourceId("pl.gda.pg.eti.kask.soundmeterpg:id/measure_button_fragment"));

        startMeasureAndCloseApp(startBtn);
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(BackgroundWork.class, context));

        openActivityByRecentApps(device);

        startBtn.click();
        Thread.sleep(2000);
        Assert.assertFalse(ServiceDetector.isMyServiceRunning(Measure.class, context));

    }



    @Test
    public void isStoringInListWhenHavePermissionTest() throws UiObjectNotFoundException, InterruptedException, RemoteException {
        UiObject startBtn = device.findObject(new UiSelector().resourceId("pl.gda.pg.eti.kask.soundmeterpg:id/measure_button_fragment"));
        startMeasureAndCloseApp(startBtn);
        context.deleteDatabase(context.getResources().getString(R.string.database_name));
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Measure.class, context));
        openActivityByRecentApps(device);
        startBtn.click();
        Thread.sleep(2000);
        Assert.assertNotNull(dataBaseHandler.getLastAddedRow());

    }

    private void startMeasureAndCloseApp(UiObject startBtn) throws UiObjectNotFoundException, InterruptedException, RemoteException {
        openItemInDrawer(SETTINGS_ITEM,device);
        selectPreference(R.string.working_in_background_key_preference,sharedPreferences,context);
        device.pressBack();
        openActivityByRecentApps(device);
        startBtn.click();
        Thread.sleep(2000);
        device.pressBack();
    }


}
