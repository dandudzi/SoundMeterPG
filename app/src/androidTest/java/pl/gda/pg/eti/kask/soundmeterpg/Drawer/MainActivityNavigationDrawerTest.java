package pl.gda.pg.eti.kask.soundmeterpg.Drawer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.RowsDrawerException;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SettingsTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.closeDrawer;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


/**
 * Created by Daniel on 09.08.2016 at 17:02 :).
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityNavigationDrawerTest {
    private UiDevice device;
    private Context context;
    private String[] rows;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp(){
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        context =  mActivityRule.getActivity().getBaseContext();
        rows = context.getResources().getStringArray(R.array.rows_list_drawer);
    }

    @Test
    public void isNavigationDrawerShowsCorrectly(){
        openDrawer(R.id.drawer_layout);

        String[] rows = context.getResources().getStringArray(R.array.rows_list_drawer);
        for(String row: rows){
            onView(withText(row)).check(matches(isCompletelyDisplayed()));
            getIcon(row).check(matches(isCompletelyDisplayed()));
        }

        closeDrawer(R.id.drawer_layout);
        for(String row: rows){
            onView(withText(row)).check(matches(not(isDisplayed())));
            getIcon(row).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void backButtonTest() {
        String firstRow = rows[0];
        openDrawer(R.id.drawer_layout);
        onView(withText(firstRow)).check(matches(isCompletelyDisplayed()));
        device.pressBack();
        onView(withText(firstRow)).check(matches(not(isDisplayed())));
        onView(withText(R.string.app_name)).check(matches(isCompletelyDisplayed()));

    }

    @Test
    public void isSettingsRowWorksCorrectly() throws Exception{
        int position = findPositionInRows("Settings");
        clickOnDisplayRow(position);
        SettingsTestHelper.isSettingDisplay();
        SettingsTestHelper.backFromSettings(device);
        isRowNotDisplay(position);
    }


    @Test
    public void isMeasurementsRowWorksCorrectly() throws Exception{
        int position = findPositionInRows("Measurements");
        clickOnDisplayRow(position);
        onView(withText("measurements")).check(matches(isCompletelyDisplayed()));
        isRowNotDisplay(position);
    }

    @Test
    public void isLogInRowWorksCorrectly() throws Exception{
        int position = findPositionInRows("Log in");
        clickOnDisplayRow(position);
        onView(withId(R.id.skip_button_login_activity)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.skip_button_login_activity)).perform(click());
        isRowNotDisplay(position);
    }

    @Test
    public void isMeasureRowWorksCorrectly() throws Exception{
        int position = findPositionInRows("Measure");
        clickOnDisplayRow(position);
        onView(withText("measure")).check(matches(isCompletelyDisplayed()));
        isRowNotDisplay(position);
    }

    @Test
    public void relativePositionTest(){
        openDrawer(R.id.drawer_layout);
        String[] rows = context.getResources().getStringArray(R.array.rows_list_drawer);
        Matcher upRow = withText(rows[0]);
        Matcher downRow;
        for(int i =1;i<rows.length;i++){
            downRow = withText(rows[i]);
            onView(upRow).check(isAbove(downRow));
            getIcon(rows[i-1]).check(isRightOf(upRow));
            upRow = downRow;
        }
        getIcon(rows[rows.length-1]).check(isRightOf(upRow));
        closeDrawer(R.id.drawer_layout);
    }


    private ViewInteraction getIcon(String siblingTextView) {
        return onView(allOf(withId(R.id.icon_drawer_row),hasSibling(withText(siblingTextView))));
    }

    private int findPositionInRows(String rowName) {
        for(int i = 0;i<rows.length;i++){
            if(rowName.equals(rows[i]))
                return i;
        }
        throw new RowsDrawerException("Not found row "+rowName);
    }

    private void clickOnDisplayRow(int position) {
        String row = rows[position];
        openDrawer(R.id.drawer_layout);
        onView(withText(row)).check(matches(isCompletelyDisplayed()));
        onView(withText(row)).perform(click());
    }

    private void isRowNotDisplay(int position) {
        String row = rows[position];
        onView(withText(row)).check(matches(not(isDisplayed())));
    }

}
