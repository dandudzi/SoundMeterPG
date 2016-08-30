package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.os.Build;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Daniel on 26.08.2016 at 09:50 :).
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityLifecycleTest {
    private final String APP_NAME = "SoundMeterPG";
    private final String MEASURE_ITEM = "Measure";
    private final String MEASURE_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/measure";
    private final String MEASUREMENTS_ITEM = "Measurements";
    private final String MEASUREMENTS_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/measurements";
    private final int TIME_OUT = 1000;

    private UiDevice device;

    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openActivityByIconApp();
    }

    //Measure tests

    @Test
    public void pressBackAndBackToMeasureByIconApps() throws Exception{
        testDrawerItem(true,true,MEASURE_ITEM,MEASURE_ID,MEASURE_ID);
    }

    @Test
    public void pressBackAndBackToMeasureByRecentApps() throws Exception{
        testDrawerItem(true,false,MEASURE_ITEM,MEASURE_ID,MEASURE_ID);
    }

    @Test
    public void pressHomeAndBackToMeasureByIconApps() throws Exception{
        testDrawerItem(false,true,MEASURE_ITEM,MEASURE_ID,MEASURE_ID);
    }

    @Test
    public void pressHomeAndBackToMeasureByRecentApps() throws Exception{
        testDrawerItem(false,false,MEASURE_ITEM,MEASURE_ID,MEASURE_ID);
    }

    //Measurements tests

    @Test
    public void pressBackAndBackToMeasurementsByIconApps() throws Exception{
        testDrawerItem(true,true, MEASUREMENTS_ITEM,MEASUREMENTS_ID,MEASURE_ID);
    }

    @Test
    public void pressBackAndBackToMeasurementsByRecentApps() throws Exception{
        testDrawerItem(true,false, MEASUREMENTS_ITEM,MEASUREMENTS_ID,MEASURE_ID);
    }

    @Test
    public void pressHomeAndBackToMeasurementsByIconApps() throws Exception{
        testDrawerItem(false,true, MEASUREMENTS_ITEM,MEASUREMENTS_ID,MEASUREMENTS_ID);
    }

    @Test
    public void pressHomeAndBackToMeasurementsByRecentApps() throws Exception{
        testDrawerItem(false,false, MEASUREMENTS_ITEM,MEASUREMENTS_ID,MEASUREMENTS_ID);
    }


    private void testDrawerItem(boolean pressBackIfNotPressHome, boolean openByIconAppsIfNotByRecentApps, String itemInDrawerName,
                                String idStartView, String idEndView  ) throws Exception {
        openItemInDrawer(itemInDrawerName);
        checkViewIsDisplay(idStartView);

        if(pressBackIfNotPressHome)
            device.pressBack();
        else
            device.pressHome();

        if(openByIconAppsIfNotByRecentApps)
            openActivityByIconApp();
        else
            openActivityByRecentApps();

        checkViewIsDisplay(idEndView);
    }

    private void openItemInDrawer(String itemText) throws UiObjectNotFoundException {
        UiObject drawer = device.findObject(new UiSelector().descriptionContains("drawer_open"));
        drawer.click();
        UiObject item = device.findObject(new UiSelector().text(itemText));
        if(!item.waitForExists(TIME_OUT))
            throw new UiObjectNotFoundException("Not found view");
        item.click();
    }

    private void checkViewIsDisplay(String idView) throws UiObjectNotFoundException {
        UiObject view = device.findObject(new UiSelector().resourceId(idView));
        if(!view.waitForExists(TIME_OUT))
            throw new UiObjectNotFoundException("Not found view");
    }



    private void openActivityByRecentApps() throws RemoteException, UiObjectNotFoundException {
        UiObject panel = getHomeScreen();
        if (!panel.exists())
            device.pressHome();
        device.pressRecentApps();

        UiObject myApp = device.findObject(new UiSelector().description(APP_NAME));

        if(Build.VERSION.SDK_INT > 19)
            while(true){
                 if(myApp.clickAndWaitForNewWindow(TIME_OUT))
                    break;
            }
        else
            myApp.clickAndWaitForNewWindow(TIME_OUT);
    }

    private void openActivityByIconApp() throws UiObjectNotFoundException {
        UiObject appsButton = device.findObject(new UiSelector().description("Apps"));
        UiObject panel  = getHomeScreen();
        if(panel.exists())
            appsButton.clickAndWaitForNewWindow();
        else{
            device.pressHome();
            appsButton.click();
        }

        UiObject myApp = device.findObject(new UiSelector().text(APP_NAME));
        UiObject tabContentView = getAppTab();
        while(!myApp.exists()) {
            tabContentView.swipeLeft(10);
            tabContentView.swipeDown(10);
        }

        myApp.clickAndWaitForNewWindow();
    }

    private UiObject getAppTab() {
        if(Build.VERSION.SDK_INT > 19)
            return device.findObject(new UiSelector().resourceId("android:id/content"));
        else
            return device.findObject(new UiSelector().resourceId("android:id/tabcontent"));
    }

    private UiObject getHomeScreen() {
        return device.findObject(new UiSelector().descriptionContains("Home screen"));
    }
}
