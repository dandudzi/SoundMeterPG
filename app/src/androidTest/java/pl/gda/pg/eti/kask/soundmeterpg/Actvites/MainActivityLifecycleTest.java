package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASUREMENTS_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASUREMENTS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ID;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASURE_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByIconApp;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.testDrawerItem;
/**
 * Created by Daniel on 26.08.2016 at 09:50 :).
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityLifecycleTest {

    private static final boolean PRESS_BACK = true;
    private static final boolean PRESS_HOME = false;
    private static final boolean OPEN_APP_BY_RECENT_APP = false;
    private static final boolean OPEN_APP_BY_ICON_APP = true;

    private UiDevice device;

    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openActivityByIconApp(device);
        openItemInDrawer(MEASURE_ITEM,device);
    }

    //Measure tests

    @Test
    public void pressBackAndBackToMeasureByIconApps() throws Exception{
       // testDrawerItem(PRESS_BACK,OPEN_APP_BY_ICON_APP,MEASURE_ITEM,MEASURE_ID,MEASURE_ID, device);
    }

    @Test
    public void pressBackAndBackToMeasureByRecentApps() throws Exception{
      //  testDrawerItem(PRESS_BACK,OPEN_APP_BY_RECENT_APP,MEASURE_ITEM,MEASURE_ID,MEASURE_ID, device);
    }

    @Test
    public void pressHomeAndBackToMeasureByIconApps() throws Exception{
      //  testDrawerItem(PRESS_HOME,OPEN_APP_BY_ICON_APP,MEASURE_ITEM,MEASURE_ID,MEASURE_ID, device);
    }

    @Test
    public void pressHomeAndBackToMeasureByRecentApps() throws Exception{
       // testDrawerItem(PRESS_HOME,OPEN_APP_BY_RECENT_APP,MEASURE_ITEM,MEASURE_ID,MEASURE_ID, device);
    }

    //Measurements tests

    @Test
    public void pressBackAndBackToMeasurementsByIconApps() throws Exception{
       // testDrawerItem(PRESS_BACK,OPEN_APP_BY_ICON_APP, MEASUREMENTS_ITEM,MEASUREMENTS_ID,MEASURE_ID, device);
    }

    @Test
    public void pressBackAndBackToMeasurementsByRecentApps() throws Exception{
        //testDrawerItem(PRESS_BACK,OPEN_APP_BY_RECENT_APP, MEASUREMENTS_ITEM,MEASUREMENTS_ID,MEASURE_ID, device);
    }

    @Test
    public void pressHomeAndBackToMeasurementsByIconApps() throws Exception{
      //  testDrawerItem(PRESS_HOME,OPEN_APP_BY_ICON_APP, MEASUREMENTS_ITEM,MEASUREMENTS_ID,MEASUREMENTS_ID, device);
    }

    @Test
    public void pressHomeAndBackToMeasurementsByRecentApps() throws Exception{
       // testDrawerItem(PRESS_HOME,OPEN_APP_BY_RECENT_APP, MEASUREMENTS_ITEM,MEASUREMENTS_ID,MEASUREMENTS_ID, device);
    }


}
