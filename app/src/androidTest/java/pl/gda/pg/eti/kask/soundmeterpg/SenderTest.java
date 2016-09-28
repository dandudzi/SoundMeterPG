package pl.gda.pg.eti.kask.soundmeterpg;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverrangeException;
import pl.gda.pg.eti.kask.soundmeterpg.Services.Sender;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;


/**
 * Created by filgierl
 */

@RunWith(AndroidJUnit4.class)
public class SenderTest {
    private static Sample sample;
    private static Context context = InstrumentationRegistry.getTargetContext();
    private static Sender sender;
    private static ConnectionInternetDetector connectionInternetDetector;
    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule().withTimeout(60L, TimeUnit.SECONDS);
    private final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());

    @BeforeClass
    public static void setUp() {
        connectionInternetDetector = new ConnectionInternetDetector(context);
        Random rand = new Random();
        try {
            sample = new Sample(Sample.MIN_NOISE_LEVEL + (Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) * rand.nextDouble(),
                    Sample.MIN_LATITUDE + (Sample.MAX_LATITUDE - Sample.MIN_LATITUDE) * rand.nextDouble(),
                    Sample.MIN_LONGITUDE + (Sample.MAX_LONGITUDE - Sample.MIN_LONGITUDE) * rand.nextDouble(), 0);
        } catch (OverrangeException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void boundService() throws TimeoutException, InterruptedException {
        context = InstrumentationRegistry.getTargetContext();
        Intent senderIntent = new Intent(context, Sender.class);
        IBinder binder = mServiceRule.bindService(senderIntent);
        //Czas na zbindowanie, metoda asynchroniczna
        Thread.sleep(2000);
        if (binder == null)
            binder = mServiceRule.bindService(senderIntent);
        sender = ((Sender.LocalBinder) binder).getService();
    }

    /*
    @Test
    public void isServiceRunning() {
        //Serwis jest zbindowany i powinien być uruchomiony.
        Assert.assertTrue(ServiceDetector.isMyServiceRunning(Sender.class, _context));
        _context.unbindService(mConnection);
        Assert.assertFalse(ServiceDetector.isMyServiceRunning(Sender.class, _context));
        //Zasymulowanie wcisnięcia przycisk home.
        _context.bindService(_intent, mConnection, Context.BIND_AUTO_CREATE);
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
        _context.unbindService(mConnection);
        Assert.assertFalse("Should return false if app is closed", ServiceDetector.isMyServiceRunning(Sender.class, _context));
        returnService();
    }
*/
    @Test
    public void isConnectionWithServer() {
        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, true);
        if (connectionInternetDetector.isConnectingToInternet())
            Assert.assertTrue(sender.isConnectionWithServer(context.getResources().getString(R.string.ping_site)));
        else
            Assert.assertFalse(sender.isConnectionWithServer(context.getResources().getString(R.string.ping_site)));
        //TODO ogarnąć to na emulatorze, na fizycznym urządzeniu działa
        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, false);
        Assert.assertFalse(sender.isConnectionWithServer(context.getResources().getString(R.string.ping_site)));
    }

    @Test
    public void isSendingDataToServerCorrectly() {
        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, true);
        try {
            if (connectionInternetDetector.isConnectingToInternet())
                Assert.assertTrue(sender.insert(sample));
        } catch (NullRecordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tryingToSendWithoOutInternet() {
        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, false);
        try {
            if (connectionInternetDetector.isConnectingToInternet())
                Assert.assertFalse(sender.insert(sample));
        } catch (NullRecordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void NullParametrTest() {
        PreferenceTestHelper.setPrivilages(R.string.internet_key_preference, preferences, context, false);
        Sample sample = null;
        Throwable e = null;
        try {
            sender.insert(sample);
        } catch (Throwable ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof NullRecordException);
    }

   /* private void returnService() {
        //Uruchomienie aplikacji na nowo.
        final String launcherPackage = _device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        _device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 2000);
        Context ctx = InstrumentationRegistry.getContext();
        final Intent intent = ctx.getPackageManager().getLaunchIntentForPackage("pl.gda.pg.eti.kask.soundmeterpg");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ctx.startActivity(intent);
        _device.wait(Until.hasObject(By.pkg("pl.gda.pg.eti.kask.soundmeterpg").depth(0)), 2000);
        _context.bindService(_intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    */

}
