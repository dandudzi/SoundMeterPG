package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.LOGIN_ACTIVITY_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.LOGIN_ACTIVITY_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.LOGIN_BUTTON_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.LOGIN_DIALOG_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.LOGIN_DIALOG_TEXT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.TIME_OUT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.checkViewIsDisplay;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByIconApp;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByRecentApps;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;

/**
 * Created by Daniel on 08.09.2016 at 19:17 :).
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginLifecycleTest {
    private UiDevice device;

    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openActivityByIconApp(device);
        openItemInDrawer(MEASURE_ITEM, device);
        openLoginDialogAndCheckIsDisplay();
    }


    @Test
    public void backTest() throws UiObjectNotFoundException {
        device.pressBack();
        checkViewIsDisplay(LOGIN_ACTIVITY_ID,device);
    }

    @Test
    public void pressHomeAndBackToAppByIconApp() throws UiObjectNotFoundException {
        device.pressHome();
        openActivityByIconApp(device);
        checkViewIsDisplay(MEASURE_ID,device);
        openItemInDrawer(LOGIN_ACTIVITY_ITEM,device);
        checkViewIsDisplay(LOGIN_ACTIVITY_ID,device);
    }

    @Test
    public void pressHomeAndBackToAppByRecentApp() throws RemoteException, UiObjectNotFoundException {
        device.pressHome();
        openActivityByRecentApps(device);
        checkViewIsDisplay(MEASURE_ID,device);
        openItemInDrawer(LOGIN_ACTIVITY_ITEM,device);
        checkViewIsDisplay(LOGIN_ACTIVITY_ID,device);
    }


    private void openLoginDialogAndCheckIsDisplay() throws UiObjectNotFoundException {
        openItemInDrawer(LOGIN_ACTIVITY_ITEM,device);
        checkViewIsDisplay(LOGIN_ACTIVITY_ID,device);
        UiObject button = device.findObject(new UiSelector().text(LOGIN_DIALOG_TEXT).resourceId(LOGIN_BUTTON_ID));
        button.waitForExists(TIME_OUT);
        button.click();
        checkViewIsDisplay(LOGIN_DIALOG_ID,device);
    }

}
