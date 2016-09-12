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

import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.FAQ_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.FAQ_TEXT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MENU_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.TIME_OUT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.checkViewIsDisplay;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByIconApp;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByRecentApps;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;

/**
 * Created by Daniel on 08.09.2016 at 18:40 :).
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class FAQLifecycleTest {
    private UiDevice device;

    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openActivityByIconApp(device);
        openItemInDrawer(MEASURE_ITEM,device);
        openFAQDialogAndCheckIsDisplay();
    }


    @Test
    public void backTest() throws UiObjectNotFoundException {
        device.pressBack();
        checkViewIsDisplay(MEASURE_ID,device);
    }

    @Test
    public void pressHomeAndBackToAppByIconApp() throws UiObjectNotFoundException {
        device.pressHome();
        openActivityByIconApp(device);
        checkViewIsDisplay(FAQ_ID,device);
    }

    @Test
    public void pressHomeAndBackToAppByRecentApp() throws RemoteException, UiObjectNotFoundException {
        device.pressHome();
        openActivityByRecentApps(device);
        checkViewIsDisplay(FAQ_ID,device);
    }

    private void openFAQDialogAndCheckIsDisplay() throws UiObjectNotFoundException {
        UiObject menu = device.findObject(new UiSelector().description(MENU_ID));
        menu.waitForExists(TIME_OUT);
        menu.click();
        UiObject button = device.findObject(new UiSelector().text(FAQ_TEXT));
        button.click();
        checkViewIsDisplay(FAQ_ID,device);
    }
}
