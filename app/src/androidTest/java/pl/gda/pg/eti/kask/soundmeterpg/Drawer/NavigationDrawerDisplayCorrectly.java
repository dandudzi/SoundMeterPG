package pl.gda.pg.eti.kask.soundmeterpg.Drawer;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.uiautomator.UiDevice;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

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
import static pl.gda.pg.eti.kask.soundmeterpg.DrawerTestHelper.clickOnDisplayRow;
import static pl.gda.pg.eti.kask.soundmeterpg.DrawerTestHelper.findPositionInRows;
import static pl.gda.pg.eti.kask.soundmeterpg.DrawerTestHelper.isDrawerNotDisplay;

/**
 * Created by Daniel on 09.09.2016 at 19:52 :).
 */
@Ignore
public class NavigationDrawerDisplayCorrectly {

    UiDevice device;
    Context context;
    String[] rows;

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
    public void isSettingsRowWorksCorrectly() throws Exception{
        int position = findPositionInRows("Settings",rows);
        clickOnDisplayRow(position,rows);
        SettingsTestHelper.isSettingDisplay();
        SettingsTestHelper.backFromSettings(device);
        isDrawerNotDisplay(position,rows);
    }


    @Test
    public void isMeasurementsRowWorksCorrectly() throws Exception{
        int position = findPositionInRows("Measurements",rows);
        clickOnDisplayRow(position,rows);
        onView(withText("measurements")).check(matches(isCompletelyDisplayed()));
        isDrawerNotDisplay(position,rows);
    }

    @Test
    public void isLogInRowWorksCorrectly() throws Exception{
        int position = findPositionInRows("Log in",rows);
        clickOnDisplayRow(position,rows);
        onView(withId(R.id.skip_button_login_activity)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.skip_button_login_activity)).perform(click());
        isDrawerNotDisplay(position,rows);
    }

    @Test
    public void isMeasureRowWorksCorrectly() throws Exception{
        int position = findPositionInRows("Measure",rows);
        clickOnDisplayRow(position,rows);
        onView(withText("measure")).check(matches(isCompletelyDisplayed()));
        isDrawerNotDisplay(position,rows);
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
}
