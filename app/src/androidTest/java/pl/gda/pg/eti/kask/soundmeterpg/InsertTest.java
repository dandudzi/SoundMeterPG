package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.RegionIterator;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.AdapterViewProtocol;
import android.support.test.espresso.core.deps.guava.eventbus.AsyncEventBus;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Exception.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exception.OverrangeException;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;


import static junit.framework.Assert.fail;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.selectPreference;

/**
 * Created by gierl on 16.08.2016.
 */
@RunWith(AndroidJUnit4.class)
public class InsertTest {
    private static Probe _probe;
    private static Context _context;
    private static Insert _insert;
    private static Intent _intent;
    private static UiDevice _device;
    private static SharedPreferences prefs;
    private static ConnectionInternetDetector _connectionInternetDetector;
    private static PreferenceParser _preferenceParser;
    protected static ServiceConnection _mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            Insert.LocalBinder binder = (Insert.LocalBinder) service;
            _insert = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @ClassRule
    public static ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void setUp() {
        _context = mActivityRule.getActivity().getApplication().getBaseContext();
        _intent = new Intent(_context, Insert.class);
        _context.bindService(_intent, _mConnection, Context.BIND_AUTO_CREATE);
        _device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        _connectionInternetDetector = new ConnectionInternetDetector(_context);
        prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        _preferenceParser = new PreferenceParser(_context);
        Random rand = new Random();
        try {
            _probe = new Probe(Probe.MIN_NOISE_LEVEL + (Probe.MAX_NOISE_LEVEL - Probe.MIN_NOISE_LEVEL) * rand.nextDouble(),
                    Probe.MIN_LATITUDE + (Probe.MAX_LATITUDE - Probe.MIN_LATITUDE) * rand.nextDouble(),
                    Probe.MIN_LONGITUDE + (Probe.MAX_LONGITUDE - Probe.MIN_LONGITUDE) * rand.nextDouble());
        } catch (OverrangeException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void isServiceRunning() {
        //Serwis jest zbindowany i powinien być uruchomiony.
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Insert.class, _context));
        _context.unbindService(_mConnection);
        Assert.assertFalse(ServiceDetector.isMyServiceRunning(Insert.class, _context));
        //Zasymulowanie wcisnięcia przycisk home.
        _context.bindService(_intent, _mConnection, Context.BIND_AUTO_CREATE);
        _device.pressHome();
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Insert.class, _context));
        // Zasymulowanie zablokowanie telefonu i wyłączenie ekranu.
        try {
            _device.sleep();
            Assert.assertTrue(ServiceDetector.isMyServiceRunning(Insert.class, _context));
            _device.wakeUp();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        returnService();
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Insert.class, _context));
        _device.pressBack();
        _device.pressBack();
        mActivityRule.getActivity().finish();
        _context.unbindService(_mConnection);
        Assert.assertFalse("Should return false if app is closed", ServiceDetector.isMyServiceRunning(Insert.class, _context));
        returnService();
    }

    @Test
    public void isConnectionWithServer() {
        if (_connectionInternetDetector.isConnectingToInternet())
            Assert.assertTrue(_insert.isConnectionWithServer("soundmeterpg.cba.pl"));
        else
            Assert.assertFalse(_insert.isConnectionWithServer("soundmeterpg.cba.pl"));
        //TODO ogarnąć to na emulatorze, na fizycznym urządzeniu działa

    }

    @Test
    public void isSendingDataToServerCorrectly() {

        //Ustawienie preferencji internetu
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(_context.getResources().getString(R.string.internet_key_preference), false);
        try {
            if (_connectionInternetDetector.isConnectingToInternet() && _preferenceParser.hasPrivilegesToUseInternet())
                Assert.assertTrue(_insert.insert(_probe));
        } catch (NullRecordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void NullParametrTest() {
        Probe probe = null;
        Throwable e = null;
        try {
            _insert.insert(probe);
        } catch (Throwable ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof NullRecordException);
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
