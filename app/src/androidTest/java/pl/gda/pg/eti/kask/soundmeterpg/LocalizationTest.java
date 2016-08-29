package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;
import android.telephony.TelephonyManager;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullLocalizationException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Filip Gierłowski and Daniel Dudziak
 */
@RunWith(AndroidJUnit4.class)
public class LocalizationTest {
    private static Localization _localization;
    private static final double MIN = 0.0001;
    private static final double MAX = 120.23;
    private static Context _context;
    private static Intent _intent;
    private static List<Double> _latitude;
    private static List<Double> _longitude;
    private static SharedPreferences _preferences;
    private static UiDevice _device;
    private static SharedPreferences.Editor _editor;
    protected static ServiceConnection _mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            Localization.LocalBinder binder = (Localization.LocalBinder) service;
            _localization = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };
    @ClassRule
    public static ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    @BeforeClass
    public static void randomizeArrayList() {
        _context = mActivityRule.getActivity().getApplication().getBaseContext();
        _intent = new Intent(_context, Localization.class);
        _context.bindService(_intent, _mConnection, Context.BIND_AUTO_CREATE);
        _device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        _latitude = new ArrayList<Double>(10);
        _longitude = new ArrayList<Double>(10);
        _preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        _editor = _preferences.edit();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            _latitude.add(MIN + (MAX - MIN) * rand.nextDouble());
            _longitude.add(MIN + (MAX - MIN) * rand.nextDouble());
        }
    }

    @Test
    public void isServiceRunning() {
        //Serwis jest zbindowany i powinien być uruchomiony.
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Localization.class, _context));
        _context.unbindService(_mConnection);
        Assert.assertFalse(ServiceDetector.isMyServiceRunning(Localization.class, _context));
        //Zasymulowanie wcisnięcia przycisk home.
        _context.bindService(_intent, _mConnection, Context.BIND_AUTO_CREATE);
        _device.pressHome();
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Localization.class, _context));
        // Zasymulowanie zablokowanie telefonu i wyłączenie ekranu.
        try {
            _device.sleep();
            Assert.assertTrue(ServiceDetector.isMyServiceRunning(Localization.class, _context));
            _device.wakeUp();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        returnService();
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Localization.class, _context));
        _device.pressBack();
        _device.pressBack();
        mActivityRule.getActivity().finish();
        _context.unbindService(_mConnection);
        Assert.assertFalse("Should return false if app is closed", ServiceDetector.isMyServiceRunning(Localization.class, _context));
        returnService();
    }


    @Test
    public void canGetLocalization() {
        //Posiada preferencje do użycia internetu.
        privilegesToUseGps(false);
        privilegesToUseInternet(true);
        LocationManager locationManager = locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            Assert.assertTrue("Should return true, Internet enabled and has privileges to use Internet", _localization.canUseLocation());
        else
            Assert.assertFalse("Should return false, Internet disabled", _localization.canUseLocation());

        //Posiada prerenecje do użycia GPS
        privilegesToUseGps(true);
        privilegesToUseInternet(false);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            Assert.assertTrue("Should return true, GPS enabled and has privileges to use GPS", _localization.canUseLocation());
        else
            Assert.assertFalse("Should return false, GPS disabled", _localization.canUseLocation());

        //Posiada preferencje do użycia GPS oraz Internetu
        privilegesToUseGps(true);
        privilegesToUseInternet(true);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            Assert.assertTrue("Should return true, GPS or Internet enabled and has privileges to use GPS and Internet", _localization.canUseLocation());
        else
            Assert.assertFalse("Should return true, GPS or Internet enabled and has privileges to use GPS and Internet", _localization.canUseLocation());

    }


    @Test
    public void isCorrectLocalizationFromNetwork() throws InterruptedException {
        privilegesToUseInternet(true);
        privilegesToUseGps(false);
        Handler mainHandler = new Handler(_context.getMainLooper());
        final CountDownLatch latch = new CountDownLatch(1);
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MockLocationProvider _mockLocationProvider = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, _context);
                    for (int i = 0; i < 10; i++) {
                        _mockLocationProvider.pushLocation(_latitude.get(i), _longitude.get(i));
                        Thread.sleep(1000);
                        Location l = _localization.getLocalization();
                        Assert.assertEquals(_latitude.get(i), l.getLatitude(), 0.0);
                        Assert.assertEquals(_longitude.get(i), l.getLongitude(), 0.0);
                    }
                    _mockLocationProvider.shutdown();
                    _localization.stopUsingGPS();
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


    }

    @Test
    public void isCorrectLocalizationFromGPS() throws InterruptedException {
        privilegesToUseGps(true);
        privilegesToUseInternet(false);
        Handler mainHandler = new Handler(_context.getMainLooper());
        final CountDownLatch latch = new CountDownLatch(1);
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MockLocationProvider _mockLocationProvider = new MockLocationProvider(LocationManager.GPS_PROVIDER, _context);
                    for (int i = 0; i < 10; i++) {
                        _mockLocationProvider.pushLocation(_latitude.get(i), _longitude.get(i));
                        Thread.sleep(1000);
                        Location l = _localization.getLocalization();
                        Assert.assertEquals(_latitude.get(i), l.getLatitude(), 0.0);
                        Assert.assertEquals(_longitude.get(i), l.getLongitude(), 0.0);
                    }
                    _mockLocationProvider.shutdown();
                    _localization.stopUsingGPS();
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
    }

    private void privilegesToUseInternet(boolean enabled) {
        _editor.putBoolean(_context.getResources().getString(R.string.internet_key_preference), enabled);
        _editor.commit();
    }

    private void privilegesToUseGps(boolean enabled) {
        _editor.putBoolean(_context.getResources().getString(R.string.gps_key_preference), enabled);
        _editor.commit();
    }


    private void returnService() {
        //Uruchomienie aplikacji na nowo.
        final String launcherPackage = _device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        _device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 2000);
        Context ctx = InstrumentationRegistry.getContext();
        final Intent intent = ctx.getPackageManager().getLaunchIntentForPackage("pl.gda.pg.eti.kask.soundmeterpg");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ctx.startActivity(intent);
        _device.wait(Until.hasObject(By.pkg("pl.gda.pg.eti.kask.soundmeterpg").depth(0)), 2000);
        _context.bindService(_intent, _mConnection, Context.BIND_AUTO_CREATE);
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