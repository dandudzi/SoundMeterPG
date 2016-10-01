package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientPermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Services.GoogleAPILocalization;
import pl.gda.pg.eti.kask.soundmeterpg.Services.SampleCreator;
import pl.gda.pg.eti.kask.soundmeterpg.Services.Sender;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

import static junit.framework.Assert.fail;

/**
 * Created by Filip Gierłowski and dandudzi xd
 */
@RunWith(AndroidJUnit4.class)
public class SampleCreatorLifeCycleTest {
    private static Context context;
    private static Intent intent;
    private static SampleCreator sampleCreator;
    public  ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            SampleCreator.LocalBinder binder = (SampleCreator.LocalBinder) service;
            sampleCreator = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            sampleCreator = null;
        }
    };

    @ClassRule
    public static ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    public final ServiceTestRule mServiceRule = new ServiceTestRule().withTimeout(60L, TimeUnit.SECONDS);

    @BeforeClass
    public static void initialize() {
        context = mActivityRule.getActivity().getApplication().getBaseContext();
        intent = new Intent(context, SampleCreator.class);
    }

    @Test
    public void isServiceBindedByMainActivityContext() throws InterruptedException {
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Thread.sleep(4000);
        Assert.assertTrue(isServiceRuning(context));
        context.unbindService(mConnection);
    }
    @Test
    public void isServiceBindedByServiceTestRule() throws TimeoutException, InterruptedException {
        IBinder binder = mServiceRule.bindService(intent);
        //Czas na zbindowanie, metoda asynchroniczna
        Thread.sleep(2000);
        if (binder == null)
            binder = mServiceRule.bindService(intent);
        sampleCreator = ((SampleCreator.LocalBinder) binder).getService();
        Assert.assertTrue(isServiceRuning(InstrumentationRegistry.getTargetContext()));
    }
    @Test
    public void multipleBindingServiceTest() throws InterruptedException {
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Thread.sleep(4000);
        Assert.assertTrue(isServiceRuning(context));
        context.unbindService(mConnection);
        Assert.assertFalse(isServiceRuning(context));
    }


    @Test
    public void startAndStopTest() throws InterruptedException, InsufficientPermissionsException {
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Thread.sleep(8000);
        while(sampleCreator == null)
        Thread.sleep(2000);
        sampleCreator.start();
        sampleCreator.stop();
        sampleCreator.start();
        sampleCreator.stop();
        sampleCreator.start();
        context.unbindService(mConnection);
        sampleCreator.stop();
    }
    private boolean isServiceRuning(Context ctx){
        return ( ServiceDetector.isMyServiceRunning(SampleCreator.class, ctx) &&
                ServiceDetector.isMyServiceRunning(GoogleAPILocalization.class, ctx) &&
                ServiceDetector.isMyServiceRunning(Sender.class, ctx));
    }
}
