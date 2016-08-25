package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.RegionIterator;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.AdapterViewProtocol;
import android.support.test.espresso.core.deps.guava.eventbus.AsyncEventBus;
import android.support.test.espresso.intent.rule.IntentsTestRule;
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
import pl.gda.pg.eti.kask.soundmeterpg.Exception.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exception.OverrangeException;


import static junit.framework.Assert.fail;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by gierl on 16.08.2016.
 */
@RunWith(AndroidJUnit4.class)
public class InsertTest {
    private static Probe _probe;
    private static Context _context;
    private static Insert _insert;
    private static Intent _intent;
    private static boolean _mBound;
    private static UiDevice _device;
    protected static ServiceConnection _mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            Insert.LocalBinder binder = (Insert.LocalBinder) service;
            _insert = binder.getService();
            _mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            _mBound = false;
        }
    };

    @ClassRule
    public static IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(MainActivity.class);

    @BeforeClass
    public static void setUp() {
        _context = mActivityRule.getActivity().getApplication().getBaseContext();
        _intent = new Intent(_context, Insert.class);
        _context.bindService(_intent, _mConnection, Context.BIND_AUTO_CREATE);
        _device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
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
    public void isNetworkEnabled() {
        _insert.enableNetwork();
        Assert.assertTrue(_insert.getNetworkConnection());
        _insert.disableNetwork();
        Assert.assertFalse(_insert.getNetworkConnection());
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
        if (_insert.getNetworkConnection())
            Assert.assertTrue(_insert.isConnectionWithServer("soundmeterpg.cba.pl"));
        else
            Assert.assertFalse(_insert.isConnectionWithServer("soundmeterpg.cba.pl"));
        //TODO ogarnąć to na emulatorze, na fizycznym urządzeniu działa

    }

    @Test
    public void isSendingDataToServer() {
        try {
            if (_insert.getNetworkConnection())
                Assert.assertTrue(_insert.insert(_probe));
            else
                Assert.assertFalse(_insert.insert(_probe));
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

    @Test
    public void isConnectionStringCorrect() {
        //  fail();
        //  String connectionString = Insert.URL + _probe.getAvgNoiseLevel() + Insert.LATITUDE + _probe.getLatitude() + Insert.LONGITUDE + _probe.getLongitude();
    }

    private void returnService() {
        //Uruchomienie aplikacji na nowo.
        final String launcherPackage = _device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        _device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 1000);

        Context ctx = InstrumentationRegistry.getContext();
        final Intent intent = ctx.getPackageManager().getLaunchIntentForPackage("pl.gda.pg.eti.kask.soundmeterpg");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ctx.startActivity(intent);
        _device.wait(Until.hasObject(By.pkg("pl.gda.pg.eti.kask.soundmeterpg").depth(0)), 2000);
        _context.bindService(_intent, _mConnection, Context.BIND_AUTO_CREATE);
    }

}
