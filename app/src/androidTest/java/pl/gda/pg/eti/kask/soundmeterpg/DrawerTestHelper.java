package pl.gda.pg.eti.kask.soundmeterpg;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.RowsDrawerException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by Daniel on 26.08.2016 at 10:12 :).
 */
public class DrawerTestHelper {

    public static int findPositionInRows(String rowName, String[] rows) {
        for(int i = 0;i<rows.length;i++){
            if(rowName.equals(rows[i]))
                return i;
        }
        throw new RowsDrawerException("Not found row "+rowName);
    }

    public static void clickOnDisplayRow(int position, String[] rows) {
        String row = rows[position];
        openDrawer(R.id.drawer_layout);
        onView(withText(row)).check(matches(isCompletelyDisplayed()));
        onView(withText(row)).perform(click());
    }

    public static void isDrawerNotDisplay(int position, String[] rows) {
        String row = rows[position];
        onView(withText(row)).check(matches(not(isDisplayed())));
    }
}
