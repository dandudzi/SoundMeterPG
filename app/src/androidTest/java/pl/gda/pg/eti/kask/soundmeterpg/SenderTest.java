package pl.gda.pg.eti.kask.soundmeterpg;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
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

import java.util.Random;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverrangeException;
import pl.gda.pg.eti.kask.soundmeterpg.Services.Sender;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * Created by gierl on 16.08.2016.
 */
@RunWith(AndroidJUnit4.class)
public class SenderTest {
    private static Probe _probe;
    private static Context _context;
    private static Sender _sender;
    private static Intent _intent;
    private static UiDevice _device;
    private static ConnectionInternetDetector _connectionInternetDetector;
    private static PreferenceParser _preferenceParser;
    protected static ServiceConnection _mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Sender.LocalBinder binder = (Sender.LocalBinder) service;
            _sender = binder.getService();
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
        _intent = new Intent(_context, Sender.class);
        _context.bindService(_intent, _mConnection, Context.BIND_AUTO_CREATE);
        _device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        _connectionInternetDetector = new ConnectionInternetDetector(_context);
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
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Sender.class, _context));
        _context.unbindService(_mConnection);
        Assert.assertFalse(ServiceDetector.isMyServiceRunning(Sender.class, _context));
        //Zasymulowanie wcisnięcia przycisk home.
        _context.bindService(_intent, _mConnection, Context.BIND_AUTO_CREATE);
        _device.pressHome();
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Sender.class, _context));
        // Zasymulowanie zablokowanie telefonu i wyłączenie ekranu.
        try {
            _device.sleep();
            Assert.assertTrue(ServiceDetector.isMyServiceRunning(Sender.class, _context));
            _device.wakeUp();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        returnService();
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Sender.class, _context));
        _device.pressBack();
        _device.pressBack();
        mActivityRule.getActivity().finish();
        _context.unbindService(_mConnection);
        Assert.assertFalse("Should return false if app is closed", ServiceDetector.isMyServiceRunning(Sender.class, _context));
        returnService();
    }

    @Test
    public void isConnectionWithServer() {
        putPrivilages(true);
        if (_connectionInternetDetector.isConnectingToInternet())
            Assert.assertTrue(_sender.isConnectionWithServer(_context.getResources().getString(R.string.ping_site)));
        else
            Assert.assertFalse(_sender.isConnectionWithServer(_context.getResources().getString(R.string.ping_site)));
        //TODO ogarnąć to na emulatorze, na fizycznym urządzeniu działa
        putPrivilages(false);
        Assert.assertFalse(_sender.isConnectionWithServer(_context.getResources().getString(R.string.ping_site)));
    }

    @Test
    public void isSendingDataToServerCorrectly() {

        putPrivilages(true);
        try {
            if (_connectionInternetDetector.isConnectingToInternet())
                Assert.assertTrue(_sender.insert(_probe));
        } catch (NullRecordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void NullParametrTest() {
        putPrivilages(true);
        Probe probe = null;
        Throwable e = null;
        try {
            _sender.insert(probe);
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

    private void putPrivilages(boolean enabled) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(_context.getResources().getString(R.string.internet_key_preference), enabled);
        editor.commit();
    }

}
