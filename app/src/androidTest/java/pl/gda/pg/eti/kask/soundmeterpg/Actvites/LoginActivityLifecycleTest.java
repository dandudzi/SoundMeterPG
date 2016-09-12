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

import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.ANDROID_POSITIVE_BUTTON_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.LOGIN_ACTIVITY_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.LOGIN_ACTIVITY_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.LOGIN_PROGRESS_ACTIVITY_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.checkViewIsDisplay;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByIconApp;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByRecentApps;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.pressButtonAndWaitForNewWindow;

/**
 * Created by Daniel on 30.08.2016 at 10:50 :).
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityLifecycleTest {
    private UiDevice device;
    private static final String LOGIN_BUTTON_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/login_button_login_activity";
    private static final boolean START_LOGIN = true;
    private static final boolean NOT_START_LOGIN = false;
    private static final boolean OPEN_APP_BY_RECENT_APP = true;
    private static final boolean OPEN_APP_BY_ICON_APP = false;

    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openActivityByIconApp(device);
        openItemInDrawer(MEASURE_ITEM,device);
    }

    @Test
    public void openLoginPressBackTest() throws UiObjectNotFoundException {
        openLoginActivityAndCheckIsDisplay();
        device.pressBack();
        checkViewIsDisplay(MEASURE_ID,device);
    }

    @Test
    public void openLoginStartLogInPressBackOpenLogInAgainNotLogInInProgressTest() throws UiObjectNotFoundException {
        openLoginActivityAndCheckIsDisplay();
        startLogIn();
        checkViewIsDisplay(LOGIN_PROGRESS_ACTIVITY_ID,device);
        device.pressBack();
        checkViewIsDisplay(MEASURE_ID,device);
        openLoginActivityAndCheckIsDisplay();
    }

    @Test
    public void openLoginActivityPressHomeOpenAppByRecentAppTest() throws UiObjectNotFoundException, RemoteException {
        openLoginActivityAndPressHomeThenBack(OPEN_APP_BY_RECENT_APP,NOT_START_LOGIN);
    }

    @Test
    public void openLoginActivityPressHomeOpenAppByIconAppTest() throws UiObjectNotFoundException, RemoteException {
        openLoginActivityAndPressHomeThenBack(OPEN_APP_BY_ICON_APP,NOT_START_LOGIN);
    }

    @Test
    public void openLoginActivityStartLoginPressHomeOpenAppByIconAppTest() throws RemoteException, UiObjectNotFoundException {
        openLoginActivityAndPressHomeThenBack(OPEN_APP_BY_ICON_APP,START_LOGIN);
        openLoginActivityAndCheckIsDisplay();
    }

    @Test
    public void openLoginActivityStartLoginPressHomeOpenAppByRecentAppTest() throws RemoteException, UiObjectNotFoundException {
        openLoginActivityAndPressHomeThenBack(OPEN_APP_BY_RECENT_APP,START_LOGIN);
        openLoginActivityAndCheckIsDisplay();
    }

    private void openLoginActivityAndPressHomeThenBack(boolean byRecentAppIfNotByIconApp, boolean startLogIn) throws UiObjectNotFoundException, RemoteException {
        openLoginActivityAndCheckIsDisplay();
        if(startLogIn)
            startLogIn();
        device.pressHome();
        if(byRecentAppIfNotByIconApp)
            openActivityByRecentApps(device);
        else
            openActivityByIconApp(device);
        checkViewIsDisplay(MEASURE_ID,device);
    }


    private void openLoginActivityAndCheckIsDisplay() throws UiObjectNotFoundException {
        openItemInDrawer(LOGIN_ACTIVITY_ITEM,device);
        checkViewIsDisplay(LOGIN_ACTIVITY_ID,device);
    }

    private void startLogIn() throws UiObjectNotFoundException {
        pressButtonAndWaitForNewWindow(LOGIN_BUTTON_ID,device);
        pressButtonAndWaitForNewWindow(ANDROID_POSITIVE_BUTTON_ID,device);
    }

}
