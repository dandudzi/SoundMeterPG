package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientPermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;
import pl.gda.pg.eti.kask.soundmeterpg.Services.SampleCreator;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;

import static junit.framework.Assert.fail;

/**
 * Created by Filip Gierłowski and dandudzi xd
 */
@Ignore
@RunWith(AndroidJUnit4.class)
public class MeasurementCreatorTest {
    private static Context context;
    private static Intent sampleCreatorIntent;
    private static SampleCreator sampleCreator;
    private static ConnectionInternetDetector connectionInternetDetector;
    private static List<Double> latitude;
    private static List<Double> longitude;
    private IBinder binder;
    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());
    private static pl.gda.pg.eti.kask.soundmeterpg.DataBaseHandler dataBaseHandler;
    private static MockLocationProvider mockLocationProvider;
    private static final int MAX_PROBE = 10;

    public final ServiceTestRule mServiceRule = new ServiceTestRule().withTimeout(60L, TimeUnit.SECONDS);

    @BeforeClass
    public static void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        connectionInternetDetector = new ConnectionInternetDetector(context);
        latitude = new ArrayList<>(MAX_PROBE);
        longitude = new ArrayList<>(MAX_PROBE);
       // dataBaseHandler = new DataBaseHandler(context, context.getResources().getString(R.string.database_name));
        Random rand = new Random();
        for (int i = 0; i < MAX_PROBE; i++) {
           // latitude.add(Measurement.MIN_LATITUDE + (Measurement.MAX_LATITUDE - Measurement.MIN_LATITUDE) * rand.nextDouble());
           // longitude.add(Measurement.MIN_LONGITUDE + (Measurement.MAX_LONGITUDE - Measurement.MIN_LONGITUDE) * rand.nextDouble());

        }
        PreferenceTestHelper.setPrivilages(R.string.internal_storage_key_preference, preferences, context, true);
        PreferenceTestHelper.setPrivilages(R.string.recording_audio_key_preference, preferences, context, true);
        mockLocationProvider = new MockLocationProvider(LocationManager.GPS_PROVIDER, context);
    }
    @Before
    public void clearDataBaseAndBindService() throws TimeoutException, InterruptedException {
       context.deleteDatabase(context.getResources().getString(R.string.database_name));
        context = InstrumentationRegistry.getTargetContext();
        sampleCreatorIntent = new Intent(context, SampleCreator.class);
        binder = mServiceRule.bindService(sampleCreatorIntent);
        //Czas na zbindowanie, metoda asynchroniczna
        Thread.sleep(2000);
        if (binder == null)
            binder = mServiceRule.bindService(sampleCreatorIntent);
        sampleCreator = ((SampleCreator.LocalBinder) binder).getService();
    }
@Test
    /* Ustawiam brak uprawnien do wykorzystania neta i gps, wiec nie doda zadnej proboki */
public void isNotStoringSamples() throws InterruptedException, InsufficientPermissionsException {
    PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, false);
    PreferenceTestHelper.setPrivilages(R.string.gps_key_preference, preferences, context, false);
    mockLocationProvider.pushLocation(latitude.get(0), longitude.get(0));
    sampleCreator.start();
    Thread.sleep(context.getResources().getInteger(R.integer.time_of_sample)*context.getResources().getInteger(R.integer.samples_per_minute));
    sampleCreator.stop();
    Measurement addedToDB = null;
  /*  try {
        addedToDB = dataBaseHandler.getProbeFromDB();
    } catch (NullRecordException e) {
        e.printStackTrace();
        Assert.assertNull(addedToDB);
    }*/
}

    @Test
    public void isStoringSamplesIntoDataBaseButNotOnServer() throws InterruptedException, InsufficientPermissionsException {
        // TODO Dorobić czyszczenie bazy na serwerze
        // TODO Dorobić pobieranie rekordów  z serwera
        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, false);
        PreferenceTestHelper.setPrivilages(R.string.gps_key_preference, preferences, context, true);
        mockLocationProvider.pushLocation(latitude.get(1), longitude.get(1));
        Thread.sleep(3000);
        Measurement expected = null;
        try {
            //expected = new Measurement(15,latitude.get(1), longitude.get(1), 0);
        } catch (OverRangeException e) {
            e.printStackTrace();
        }
        sampleCreator.start();
        Thread.sleep(context.getResources().getInteger(R.integer.time_of_sample)*context.getResources().getInteger(R.integer.samples_per_minute));
        Measurement addedToDB = null;
        sampleCreator.stop();
    /*    try {
            addedToDB = dataBaseHandler.getProbeFromDB();
        } catch (NullRecordException e) {
            e.printStackTrace();
            fail();
        }*/

       /* Assert.assertEquals(expected.getLongitude(), addedToDB.getLongitude() , 0.0000001);
        Assert.assertEquals(expected.getLatitude(), addedToDB.getLatitude(), 0.0000001);
        Assert.assertFalse(addedToDB.getState());*/
    }
    @Test
   public void isSendToServerCorrect() throws InterruptedException, OverRangeException, InsufficientPermissionsException {


        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, true);
        PreferenceTestHelper.setPrivilages(R.string.gps_key_preference, preferences, context, true);
        mockLocationProvider.pushLocation(latitude.get(0), longitude.get(0));
        Measurement expected = null;
        //expected = new Measurement(15, latitude.get(0), longitude.get(0), 0);
        sampleCreator.start();
        Thread.sleep(context.getResources().getInteger(R.integer.time_of_sample)*context.getResources().getInteger(R.integer.samples_per_minute));
        Measurement addedToDB = null;
        sampleCreator.stop();
       /* try {
            addedToDB = dataBaseHandler.getProbeFromDB();
        } catch (NullRecordException e) {
            e.printStackTrace();
            fail();
        }*/
        /*Assert.assertEquals(expected.getLongitude(), addedToDB.getLongitude(), 0.0000001);
        Assert.assertEquals(expected.getLatitude(), addedToDB.getLatitude(), 0.0000001);
        Assert.assertTrue(addedToDB.getState());*/
    }
    @Test
    public void isThrowingExceptionCorrectly(){
        PreferenceTestHelper.setPrivilages(R.string.internal_storage_key_preference, preferences, context, false);
        Throwable e = null;
        try {
            sampleCreator.start();
        } catch (Throwable ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof InsufficientPermissionsException);
    }
    @Test
    public void stopCreatingSamples(){
        sampleCreator.stop();
        sampleCreator.stop();
    }

}
