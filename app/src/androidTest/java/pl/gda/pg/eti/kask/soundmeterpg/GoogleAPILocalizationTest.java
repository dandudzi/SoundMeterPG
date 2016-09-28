package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullLocalizationException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverrangeException;
import pl.gda.pg.eti.kask.soundmeterpg.Services.GoogleAPILocalization;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by gierl on 12.09.2016.
 */


@RunWith(AndroidJUnit4.class)
public class GoogleAPILocalizationTest {
    private static final double MIN = 0.0001;
    private static final double MAX = 120.23;
    private static final int MAX_PROBE = 5;
    private Intent localizationIntent = new Intent(InstrumentationRegistry.getTargetContext(), GoogleAPILocalization.class);
    private IBinder binder;
    private GoogleAPILocalization service;
    private static Context context = InstrumentationRegistry.getTargetContext();
    private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());
    static MockLocationProvider mockLocationProvider;
    private static List<Double> latitude;
    private static List<Double> longitude;

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule().withTimeout(60L, TimeUnit.SECONDS);

    @BeforeClass
    public static void setUp() {
        latitude = new ArrayList<>(MAX_PROBE);
        longitude = new ArrayList<>(MAX_PROBE);
        mockLocationProvider = new MockLocationProvider(LocationManager.GPS_PROVIDER, context);
        Random rand = new Random();
        for (int i = 0; i < MAX_PROBE; i++) {
            latitude.add(MIN + (MAX - MIN) * rand.nextDouble());
            longitude.add(MIN + (MAX - MIN) * rand.nextDouble());
        }
    }
    @AfterClass
    public static void terminate(){
        mockLocationProvider.shutdown();
    }

    @Before
    public void boundService() throws TimeoutException, InterruptedException {
       // context = InstrumentationRegistry.getTargetContext();
        localizationIntent = new Intent(context, GoogleAPILocalization.class);
        binder = mServiceRule.bindService(localizationIntent);
        //Czas na zbindowanie, metoda asynchroniczna
        Thread.sleep(2000);
        if (binder == null)
            binder = mServiceRule.bindService(localizationIntent);
        service = ((GoogleAPILocalization.LocalBinder) binder).getService();
    }

    @Test
    public void hasNotPrivilegesToUseLocalization() throws TimeoutException, InterruptedException {
        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, false);
        PreferenceTestHelper.setPrivilages(R.string.gps_key_preference, preferences, context, false);
        Assert.assertFalse(service.canUseGPS());
    }

    @Test
    public void isGettingLocalization() throws InterruptedException, TimeoutException {
        if (ServiceDetector.isGPSEnabled(context)) {
        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, true);
        PreferenceTestHelper.setPrivilages(R.string.gps_key_preference, preferences, context, true);
        //Czas na podłączenie sie googleApiClienta <-Metoda asynchroniczna
        Thread.sleep(4000);
        mockLocationProvider.pushLocation(latitude.get(0), longitude.get(0));
        Thread.sleep(context.getResources().getInteger(R.integer.time_of_sample));
        Location l = null;
            try {
                l = service.getLocation();
                Assert.assertNotNull(l);
            } catch (NullLocalizationException e) {
                e.printStackTrace();
                fail();
            }
        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, true);
        PreferenceTestHelper.setPrivilages(R.string.gps_key_preference, preferences, context, false);
            try {
                l = service.getLocation();
                Assert.assertNotNull(l);
            } catch (NullLocalizationException e) {
                e.printStackTrace();
                fail();
            }
            PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, false);
            PreferenceTestHelper.setPrivilages(R.string.gps_key_preference, preferences, context, true);
            try {
                l = service.getLocation();
                Assert.assertNotNull(l);
            } catch (NullLocalizationException e) {
                e.printStackTrace();
                fail();
            }
            PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, false);
            PreferenceTestHelper.setPrivilages(R.string.gps_key_preference, preferences, context, false);
            Throwable throwable = null;
            try {
              l = service.getLocation();
            } catch (Throwable e) {
                throwable = e;
            }
            Assert.assertTrue(throwable instanceof NullLocalizationException);
        }
    }

    @Test
    public void isCorrectLocalization() throws InterruptedException, TimeoutException {
        if (ServiceDetector.isGPSEnabled(context)) {
            PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, true);
            PreferenceTestHelper.setPrivilages(R.string.gps_key_preference, preferences, context, true);
            Thread.sleep(4000);
            Handler mainHandler = new Handler(context.getMainLooper());
            final CountDownLatch latch = new CountDownLatch(1);
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < MAX_PROBE; i++) {
                            mockLocationProvider.pushLocation(latitude.get(i), longitude.get(i));
                            Thread.sleep(context.getResources().getInteger(R.integer.time_of_sample));
                            Location l = service.getLocation();
                            Assert.assertEquals("loop nr = " + i, latitude.get(i), l.getLatitude(), 0.000001);
                            Assert.assertEquals("loop nr = " + i, longitude.get(i), l.getLongitude(), 0.000001);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (NullLocalizationException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }
            };
            mainHandler.post(myRunnable);
            latch.await();
            mainHandler.removeCallbacksAndMessages(null);


        } else {
            Throwable throwable = null;
            try {
                Location l = service.getLocation();
            } catch (Throwable e) {
                throwable = e;
            }
            Assert.assertTrue(throwable instanceof NullLocalizationException);
        }
    }
}
class MockLocationProvider {
    Context _context;
    String _provider;

    public MockLocationProvider(String name, Context ctx) {
        _context = ctx;
        _provider = name;
        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        lm.addTestProvider(_provider, false, false, false, false, false,
                true, true, 0, 5);
        lm.setTestProviderEnabled(_provider, true);
    }

    public void pushLocation(double lat, double lon) {
        try {
            LocationManager lm = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
            Location mockLocation = new Location(_provider);
            mockLocation.setLatitude(lat);
            mockLocation.setLongitude(lon);
            mockLocation.setAltitude(0);
            mockLocation.setTime(System.currentTimeMillis());
            mockLocation.setAccuracy((float) 0.0);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            }
            lm.setTestProviderLocation(_provider, mockLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        LocationManager lm = (LocationManager) _context.getSystemService(
                Context.LOCATION_SERVICE);
        lm.removeTestProvider(_provider);
    }


}
