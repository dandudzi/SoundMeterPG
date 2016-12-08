package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

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
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.SETTINGS_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.SETTINGS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.checkViewIsDisplay;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByIconApp;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByRecentApps;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;

/**
 * Created by Daniel on 06.09.2016 at 12:44 :).
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsActivityLifecycleTest {
    private UiDevice device;

    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openActivityByIconApp(device);
        openItemInDrawer(MEASURE_ITEM,device);
    }

    @Test
    public void openLoginPressBackTest() throws UiObjectNotFoundException {
//        openSettingsAndCheckIsDisplay();
//        device.pressBack();
//        checkViewIsDisplay(MEASURE_ID,device);
    }

    @Test
    public void openSettingsPressHomeAndOpenAppByIconAppTest() throws UiObjectNotFoundException {
//        openSettingsAndCheckIsDisplay();
//        device.pressHome();
//        openActivityByIconApp(device);
//        checkViewIsDisplay(SETTINGS_ID,device);
    }

    @Test
    public void openSettingsPressHomeAndOpenAppByRecentAppTest() throws UiObjectNotFoundException, RemoteException {
//        openSettingsAndCheckIsDisplay();
//        device.pressHome();
//        openActivityByRecentApps(device);
//        checkViewIsDisplay(SETTINGS_ID,device);
    }


    private void openSettingsAndCheckIsDisplay() throws UiObjectNotFoundException {
        openItemInDrawer(SETTINGS_ITEM,device);
        checkViewIsDisplay(SETTINGS_ID,device);
    }

}
