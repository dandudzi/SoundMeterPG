package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Services.ServiceDetector;
import pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static junit.framework.Assert.assertTrue;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.SETTINGS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.TIME_OUT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByIconApp;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnGPS;

/**
 * Created by Daniel on 11.10.2016.
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityWorkCorrectlyAPI23AndHigher {

    UiDevice device;

    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    }

    @Ignore
    @Test
    public void workCorrectly() throws Exception {
        if (Build.VERSION.SDK_INT > 22) {
            openPermission(device);
            turnOffGPSPermission();
            turnOffMicrophonePermssion();
            this.turnOnGPS(device, InstrumentationRegistry.getContext());
            device.pressHome();
            openActivityByIconApp(device);
        }
    }

    public void turnOnGPS(UiDevice device, Context context) throws InterruptedException, UiObjectNotFoundException {
        if(!ServiceDetector.isGPSEnabled(context)){
            Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            settingIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(settingIntent);

            UiObject object = device.findObject(new UiSelector().className("android.widget.Switch"));
            object.waitForExists(2000);
            UiObject off = device.findObject(new UiSelector().textStartsWith("Off"));
            while(off.exists())
                off.click();
            Thread.sleep(1000);
        }
    }

    public UiObject openSettings() throws UiObjectNotFoundException {
        openItemInDrawer(SETTINGS_ITEM,device);
        UiObject gps =  device.findObject(new UiSelector().resourceId("android:id/checkbox").instance(3));
        gps.waitForExists(TIME_OUT);
        return gps;
    }

    public void turnOffGPSPermission() throws UiObjectNotFoundException {
        UiObject gps = device.findObject(new UiSelector().className("android.widget.Switch").instance(0));
        if (gps.getText().equals("ON")) {
            gps.click();
        }
    }
    public void turnOffMicrophonePermssion() throws UiObjectNotFoundException {
        UiObject mic = device.findObject(new UiSelector().className("android.widget.Switch").instance(1));
        if (mic.getText().equals("ON")) {
            mic.click();
        }
    }

    private static void openPermission(UiDevice device) throws UiObjectNotFoundException {
        device.pressHome();
        device.openQuickSettings();
        UiObject settings = device.findObject( new UiSelector().resourceId("com.android.systemui:id/settings_button"));
        settings.clickAndWaitForNewWindow();
        UiObject apps = device.findObject( new UiSelector().textStartsWith("Apps"));
        UiObject content = device.findObject( new UiSelector().resourceId("android:id/list"));
        while(!apps.exists())
            content.swipeDown(20);
        apps.clickAndWaitForNewWindow();

        UiObject app = device.findObject( new UiSelector().textStartsWith(UIAutomotorTestHelper.APP_NAME));
        while(!app.exists())
            content.swipeUp(20);
        app.clickAndWaitForNewWindow();
        UiObject permission = device.findObject( new UiSelector().textStartsWith("Permission"));
        permission.clickAndWaitForNewWindow();
    }
}
