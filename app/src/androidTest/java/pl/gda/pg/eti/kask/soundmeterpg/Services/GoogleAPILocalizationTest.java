package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.app.ActivityCompat;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.TurnOffGPSException;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper;

/**
 * Created by gierl on 08.10.2016.
 */
@RunWith(AndroidJUnit4.class)
public class GoogleAPILocalizationTest {
    private static final double MIN = Location.MIN_LATITUDE;
    private static final double MAX = 120.23;
    private static final int MAX_PROBE = 5;
    private Intent localizationIntent = new Intent(InstrumentationRegistry.getTargetContext(), GoogleAPILocalization.class);
    private IBinder binder;
    private GoogleAPILocalization service;
    private static Context context = InstrumentationRegistry.getTargetContext();
    private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());
    private MockLocationProvider mockLocationProvider;
    private static List<Double> latitude;
    private static List<Double> longitude;
    private static UiDevice device;
    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule().withTimeout(60L, TimeUnit.SECONDS);

    @BeforeClass
    public static void setUp() {
        latitude = new ArrayList<>(MAX_PROBE);
        longitude = new ArrayList<>(MAX_PROBE);
        // mockLocationProvider = new MockLocationProvider(LocationManager.GPS_PROVIDER, context);
        Random rand = new Random();
        for (int i = 0; i < MAX_PROBE; i++) {
            latitude.add(Location.MIN_LATITUDE + (Location.MAX_LATITUDE - Location.MIN_LATITUDE) * rand.nextDouble());
            longitude.add(Location.MIN_LONGITUDE + (Location.MAX_LONGITUDE - Location.MIN_LONGITUDE) * rand.nextDouble());
        }
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    }


    @Before
    public void boundService() throws TimeoutException, InterruptedException {
        localizationIntent = new Intent(context, GoogleAPILocalization.class);
        binder = mServiceRule.bindService(localizationIntent);
        while (binder == null) {
            Thread.sleep(200);
            binder = mServiceRule.bindService(localizationIntent);
        }
        service = ((GoogleAPILocalization.LocalBinder) binder).getService();
    }

    @Test
    public void bindingTest() throws TimeoutException, InterruptedException {
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(GoogleAPILocalization.class, context));
    }


    @Test(expected = TurnOffGPSException.class)
    public void getLocationWhenGPSisOffTest() throws UiObjectNotFoundException, TurnOffGPSException, TimeoutException, InterruptedException {
        if(ServiceDetector.isGPSEnabled(context))
            UIAutomotorTestHelper.turnOnGps(device, context);
        Location location = service.getLocation();

    }

    @Test
    public void getLocationTest() throws TurnOffGPSException, UiObjectNotFoundException, InterruptedException, TimeoutException {
        mockLocationProvider = new MockLocationProvider(LocationManager.GPS_PROVIDER, context);
        if (!ServiceDetector.isGPSEnabled(context))
            UIAutomotorTestHelper.turnOnGps(device, context);
        for (int i = 0; i < MAX_PROBE; i++) {
            mockLocationProvider.pushLocation(latitude.get(i), longitude.get(i));
            Thread.sleep(3000);
            Location l = service.getLocation();
            Assert.assertEquals(latitude.get(i), l.getLatitude(), 0.000001);
            Assert.assertEquals(longitude.get(i), l.getLongitude(), 0.000001);
        }
        mockLocationProvider.shutdown();
    }

    class MockLocationProvider {
        Context context;
        String provider;

        public MockLocationProvider(String name, Context ctx) {
            context = ctx;
            provider = name;
            LocationManager lm = (LocationManager) ctx.getSystemService(
                    Context.LOCATION_SERVICE);
            lm.addTestProvider(provider, false, false, false, false, false,
                    true, true, 0, 5);
            lm.setTestProviderEnabled(provider, true);
        }

        public void pushLocation(double lat, double lon) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            android.location.Location mockLocation = setMockLocation(lat, lon);
            lm.setTestProviderEnabled(provider,true);
            lm.setTestProviderLocation(provider, mockLocation);

        }

        @NonNull
        private android.location.Location setMockLocation(double lat, double lon) {
            android.location.Location mockLocation = new android.location.Location(provider);
            mockLocation.setLatitude(lat);
            mockLocation.setLongitude(lon);
            mockLocation.setAltitude(0);
            mockLocation.setTime(System.currentTimeMillis());
            mockLocation.setAccuracy((float) 0.0);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            }
            return mockLocation;
        }

        public void shutdown() {
            LocationManager lm = (LocationManager) context.getSystemService(
                    Context.LOCATION_SERVICE);
            lm.removeTestProvider(provider);
        }
    }
}
