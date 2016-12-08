package pl.gda.pg.eti.kask.soundmeterpg.Services;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Database.MeasurementDataBaseObject;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientInternalStoragePermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;
import pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.Services.GoogleAPILocalization;
import pl.gda.pg.eti.kask.soundmeterpg.Services.Sender;
import pl.gda.pg.eti.kask.soundmeterpg.Services.ServiceDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;
import pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper;

import static android.support.test.espresso.core.deps.guava.base.Ascii.MAX;
import static android.support.test.espresso.core.deps.guava.base.Ascii.MIN;
@RunWith(AndroidJUnit4.class)
public class SenderTest {

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
        MeasurementStatistics statistics = new MeasurementStatistics();
        statistics.avg = 22;
        statistics.min = 12;
        statistics.max = 40;

        measurement = new Measurement(statistics,new Location(12.22,23.33), true, new Date(), 1000);
        context = mActivityRule.getActivity().getBaseContext();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        connectionInternetDetector = new ConnectionInternetDetector(context);
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        intent = new Intent(context, Sender.class);
        context.deleteDatabase(context.getResources().getString(R.string.database_name));
        dataBaseHandler = new DataBaseHandler(context, context.getResources().getString(R.string.database_name));
        context.startService(intent);
    }
    @After
    public void closeService(){
        context.stopService(intent);
    }


    @Test
    public void bindingTest() throws TimeoutException, InterruptedException {

        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Sender.class, context));
    }


    @Test
    public void tryToSendDataWithoutInternetPermission() throws InsufficientInternalStoragePermissionsException, InterruptedException {
        PreferenceTestHelper.setPrivilages(context.getResources().getString(R.string.internet_key_preference), sharedPreferences, false);
        //sprawdzic logowanie

        dataBaseHandler.insert(measurement);
        Thread.sleep(15000);
        Assert.assertNotNull(dataBaseHandler.getTheOldestRowToSendToServer());
       //1 Assert.assertFalse(sender.isConnectionWithServer(SITE));
    }
    /*\
    @Test
    public void isConnectionWithServerWhenCellularDataIsOffTest() throws UiObjectNotFoundException {

        UIAutomotorTestHelper.turnOffInternetData(device, context);
        Assert.assertFalse(sender.isConnectionWithServer(SITE));
    }
    @Test
    public void isConnectionWithServerWhenCellularDataIsOnTest() throws UiObjectNotFoundException {
        UIAutomotorTestHelper.turnOnInternetData(device, context);
        checkWithPermission();
        checkWithoutPermission();
    }

    @Test
    public void sendToServerWithoutCellularDataTest() throws UiObjectNotFoundException {
        UIAutomotorTestHelper.turnOffInternetData(device, context);
        Assert.assertFalse(sender.insert((MeasurementDataBaseObject) measurement));
    }

    @Test
    public void sendToServerWithoutPermissionTest(){
        checkWithoutPermission();
        Assert.assertFalse(sender.insert((MeasurementDataBaseObject) measurement));
    }

    @Test
    public void sendToServerTest() throws UiObjectNotFoundException {
        checkWithPermission();
        UIAutomotorTestHelper.turnOnInternetData(device, context);
        Assert.assertTrue(sender.insert((MeasurementDataBaseObject) measurement));
    }

    private void checkWithoutPermission() {
        PreferenceTestHelper.setPrivilages(context.getResources().getString(R.string.internet_key_preference), sharedPreferences, false);
        Assert.assertFalse(sender.isConnectionWithServer(SITE));
    }

    private void checkWithPermission() throws UiObjectNotFoundException {
        PreferenceTestHelper.setPrivilages(context.getResources().getString(R.string.internet_key_preference), sharedPreferences, true);
        UIAutomotorTestHelper.turnOnInternetData(device, context);
        Assert.assertTrue(sender.isConnectionWithServer(SITE));
    }
    private  Measurement createMeasurement() {
        Measurement measure;

        Random random = new Random();
        measurementStatistics.min = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
        measurementStatistics.max = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
        measurementStatistics.avg = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
        measure= new Measurement(measurementStatistics, CORRECT_LOCATION, true, date,1000);
        return measure;
    }
*/
}
