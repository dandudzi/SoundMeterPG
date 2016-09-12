package pl.gda.pg.eti.kask.soundmeterpg.Drawer;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.checkIsDrawerOpen;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.checkViewIsDisplay;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByIconApp;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByRecentApps;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openDrawer;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;

/**
 * Created by Daniel on 09.09.2016 at 19:57 :).
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class NavigationDrawerLifecycle {
    private UiDevice device;

    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openActivityByIconApp(device);
        openItemInDrawer(MEASURE_ITEM, device);
        openNavigationDrawerAndCheckIsDisplay();
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
        checkIsDrawerOpen(device);
    }

    @Test
    public void pressHomeAndBackToAppByRecentApp() throws RemoteException, UiObjectNotFoundException {
        device.pressHome();
        openActivityByRecentApps(device);
       checkIsDrawerOpen(device);
    }


    private void openNavigationDrawerAndCheckIsDisplay() throws UiObjectNotFoundException {
        openDrawer(device);
        checkIsDrawerOpen(device);
    }


}
