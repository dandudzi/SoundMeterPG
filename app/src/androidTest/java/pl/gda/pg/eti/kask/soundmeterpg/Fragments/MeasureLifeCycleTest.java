package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASUREMENTS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.SETTINGS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.TIME_OUT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByIconApp;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;

/**
 * Created by Daniel on 15.10.2016.
 */
@RunWith(AndroidJUnit4.class)
public class MeasureLifeCycleTest {
    private UiDevice device;
    private UiObject measureButton;
    private UiObject notificattion;
    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        if(Build.VERSION.SDK_INT > 22){
            openPermission(device);
            turnOnGPSPermission();
            turnOnMicrophonePermission();
            device.pressHome();
        }
        openActivityByIconApp(device);
        setUpSettings();
        device.pressBack();
        measureButton = device.findObject(new UiSelector().resourceId("pl.gda.pg.eti.kask.soundmeterpg:id/measure_button_fragment"));
        notificattion = device.findObject(new UiSelector().text("Actual measure"));
        measureButton.click();
    }

    public void setUpSettings() throws UiObjectNotFoundException {
        openItemInDrawer(SETTINGS_ITEM,device);
        UiObject perrmision = null;
        int i =0;
        do{
            perrmision = device.findObject(new UiSelector().checked(false).resourceId("android:id/checkbox").instance(i));
            if(perrmision != null && perrmision.exists() )
                perrmision.click();
            i++;
        }while (perrmision != null && i < 10);

    }

    @Test
    public void simpleTest() throws UiObjectNotFoundException {
        openItemInDrawer(MEASURE_ITEM,device);
        device.openNotification();
        assertFalse(notificattion.exists());
        device.pressBack();

        openItemInDrawer(MEASUREMENTS_ITEM,device);
        device.openNotification();
        notificattion.waitForExists(TIME_OUT);
        assertTrue(notificattion.exists());
        device.pressBack();

        openItemInDrawer(MEASURE_ITEM,device);
        device.openNotification();
        notificattion.waitForExists(TIME_OUT);
        assertFalse(notificattion.exists());
        device.pressBack();

        openItemInDrawer(MEASUREMENTS_ITEM,device);
        device.pressHome();
        device.openNotification();
        notificattion.waitForExists(TIME_OUT);
        assertTrue(notificattion.exists());
        notificattion.click();

        device.openNotification();
        notificattion.waitForExists(TIME_OUT);
        assertTrue(notificattion.exists());
        notificattion.swipeLeft(20);
        assertTrue(notificattion.exists());
        device.pressBack();

        openItemInDrawer(MEASURE_ITEM,device);
        device.openNotification();
        notificattion.waitForExists(TIME_OUT);
        assertFalse(notificattion.exists());
    }


    public void turnOnGPSPermission() throws UiObjectNotFoundException {
        UiObject gps = device.findObject(new UiSelector().className("android.widget.Switch").instance(0));
        if (gps.getText().equals("OFF")) {
                gps.click();
            }
    }

    public void turnOnMicrophonePermission() throws UiObjectNotFoundException {
        UiObject mic = device.findObject(new UiSelector().className("android.widget.Switch").instance(1));
        if (mic.getText().equals("OFF")) {
                mic.click();
           }
    }

    private static void openPermission(UiDevice device) throws UiObjectNotFoundException {
        device.pressHome();
        device.openQuickSettings();
        UiObject settings = device.findObject( new UiSelector().resourceId("com.android.systemui:id/settings_button"));
        settings.clickAndWaitForNewWindow();
        UiObject content;
        if(Build.VERSION.SDK_INT == 24)
            content = device.findObject( new UiSelector().resourceId("com.android.settings:id/dashboard_container"));
        else
            content = device.findObject( new UiSelector().resourceId("android:id/list"));

        UiObject apps = device.findObject( new UiSelector().textStartsWith("Apps"));
        while(!apps.exists())
                        content.swipeUp(50);
        apps.clickAndWaitForNewWindow();

        if(Build.VERSION.SDK_INT == 24)
            content = device.findObject( new UiSelector().resourceId("com.android.settings:id/list_container"));

        UiObject app = device.findObject( new UiSelector().textStartsWith(UIAutomotorTestHelper.APP_NAME));
        while(!app.exists())
                        content.swipeUp(50);
        app.clickAndWaitForNewWindow();
        UiObject permission = device.findObject( new UiSelector().textStartsWith("Permission"));
        permission.clickAndWaitForNewWindow();
    }
}
