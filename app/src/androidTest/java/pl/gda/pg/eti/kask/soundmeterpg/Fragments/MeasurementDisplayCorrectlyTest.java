package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientInternalStoragePermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.MEASUREMENTS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openActivityByRecentApps;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;

/**
 * Created by gierl on 09.12.2016.
 */
@RunWith(AndroidJUnit4.class)
public class MeasurementDisplayCorrectlyTest {
    private static Context context;
    private static UiDevice device;
    private static SharedPreferences prefs;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    @Before
    public   void openMeasurements() throws UiObjectNotFoundException {
        context = mActivityRule.getActivity().getBaseContext();
        prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openItemInDrawer(MEASUREMENTS_ITEM,device);
    }
    @Test
    public void isHeaderDisplayCorrectly(){
        isDisplayCorrectly("Stored on server");
        isDisplayCorrectly("Date");
       isDisplayCorrectly("Average sound");
    }


    @Test
    public void isRowDisplayedCorrectly() throws InsufficientInternalStoragePermissionsException, UiObjectNotFoundException, RemoteException, InterruptedException {

        device.pressBack();
        openActivityByRecentApps(device);
        MeasurementStatistics statistics = new MeasurementStatistics();
        statistics.avg = 22;
        statistics.min = 12;
        statistics.max = 40;

        createMeasureObject(statistics);
        openMeasurements();

        UiObject listview = new UiObject(new UiSelector().className("android.widget.ListView").clickable(true));
        //Istnieje tylko header i jedna wartosc listy
        if (listview.getChildCount() > 2) {
            Assert.fail();
        }
    }

    @Test
    public void isDeletinRowAndRefreshingListCorrectlyTest() throws RemoteException, UiObjectNotFoundException, InsufficientInternalStoragePermissionsException {

        device.pressBack();
        openActivityByRecentApps(device);
        MeasurementStatistics statistics = new MeasurementStatistics();
        statistics.avg = 22;
        statistics.min = 12;
        statistics.max = 40;

        createMeasureObject(statistics);
        openMeasurements();

        longClick(String.valueOf(statistics.avg));


        deleteMeasureAction();
        UiObject listview = new UiObject(new UiSelector().className("android.widget.ListView"));
        //Istnieje tylko header i jedna wartosc listy
        if (listview.getChildCount() != 1) {
            Assert.fail();
        }


    }

    private void deleteMeasureAction() throws UiObjectNotFoundException {
        UiObject deleterow = new UiObject(new UiSelector().textContains("Delete sample"));
        deleterow.click();
        deleterow  = new UiObject(new UiSelector().textContains("Yes"));
        deleterow.click();
        device.pressBack();
    }

    private void createMeasureObject(MeasurementStatistics statistics) throws InsufficientInternalStoragePermissionsException {
        Measurement measurement = new Measurement(statistics, new Location(12.22, 23.33), true, new Date(), 1000);
        context.deleteDatabase(context.getResources().getString(R.string.database_name));
        DataBaseHandler dataBaseHandler = new DataBaseHandler(context, context.getResources().getString(R.string.database_name));
        dataBaseHandler.insert(measurement);
    }

    public  void longClick(String text) throws UiObjectNotFoundException {
        UiObject object = new UiObject(new UiSelector().textContains(text));
        Rect coordinates = object.getBounds();
        device.swipe(coordinates .centerX(), coordinates .centerY(),
                coordinates .centerX(), coordinates .centerY(),
                100);
    }






    private  void isDisplayCorrectly(String text){
        onView(withText(text)).check(matches(isCompletelyDisplayed()));
    }

}
