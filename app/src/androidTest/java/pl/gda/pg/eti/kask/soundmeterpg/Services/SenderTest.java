package pl.gda.pg.eti.kask.soundmeterpg.Services;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

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

import pl.gda.pg.eti.kask.soundmeterpg.Database.MeasurementDataBaseObject;
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
    private static Context context = InstrumentationRegistry.getTargetContext();
    private static Sender sender;
    private static ConnectionInternetDetector connectionInternetDetector;
    private static Measurement measurement;
    private static MeasurementStatistics measurementStatistics = new MeasurementStatistics();
    private static Date date = new Date();
    private static final Location CORRECT_LOCATION = new Location(Location.MAX_LATITUDE,Location.MAX_LONGITUDE);
    private static String SITE = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.ping_site);
    private static UiDevice device = null;
    private static SharedPreferences sharedPreferences;
    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule().withTimeout(60L, TimeUnit.SECONDS);

    @BeforeClass
    public static void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        connectionInternetDetector = new ConnectionInternetDetector(context);
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

   /* @Before
    public void boundService() throws TimeoutException, InterruptedException {
        Intent senderIntent = new Intent(context, Sender.class);
        IBinder binder = mServiceRule.bindService(senderIntent);
        while (binder == null) {
            Thread.sleep(200);
            binder = mServiceRule.bindService(senderIntent);
        }
        sender = ((Sender.LocalBinder) binder).getService();
        measurement = createMeasurement();
    }
*/
    @Test
    public void bindingTest() throws TimeoutException, InterruptedException {
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Sender.class, context));
    }

    @Test
    public void isConnectionWithServerWithoutPermissionTest(){
        PreferenceTestHelper.setPrivilages(context.getResources().getString(R.string.internet_key_preference), sharedPreferences, false);
        Assert.assertFalse(sender.isConnectionWithServer(SITE));
    }
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

}
