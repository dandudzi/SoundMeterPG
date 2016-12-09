package pl.gda.pg.eti.kask.soundmeterpg.Drawer;

import android.content.Context;
import android.support.test.uiautomator.UiDevice;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.AccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Daniel on 09.09.2016 at 19:52 :).
 */
@Ignore
public class NavigationDrawerDisplayCorrectly {
    UiDevice device;
    Context context;
    AccountManager manager;


    @Test
    public void isNavigationDrawerShowsCorrectly() {
        onView(withId(R.id.left_drawer)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isMeasureDisplayCorrectly(){
        onView(withText(R.string.measure_drawer)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isMeasurementsDisplayCorrectly(){
        onView(withText(R.string.measurements_drawer)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isSettingsDisplayCorrectly(){
        int text = R.string.settings_drawer;
        onView(withText(text)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isLoginOrLogoutDisplayCorrectly(){
        String text = getLogText();
        onView(withText(text)).check(matches(isCompletelyDisplayed()));
    }


    @Test
    public void isHeaderDisplayCorrectly(){
        int id = getHeader();
        onView(withId(id)).check(matches(isCompletelyDisplayed()));
    }


    @Test
    public void relativePositionTest(){
        Matcher header = withId(getHeader());
        Matcher measure = withText(R.string.measure_drawer);
        Matcher measurements = withText(R.string.measurements_drawer);
        Matcher settings = withText(R.string.settings_drawer);
        Matcher log = withText(getLogText());

        onView(header).check(isAbove(measure));
        onView(measure).check(isAbove(measurements));
        onView(measurements).check(isAbove(settings));
        onView(settings).check(isAbove(log));
    }

    public String getLogText() {
        String text;
        if(manager.isLogIn())
            text =context.getString(R.string.log_out_drawer);
        else
            text ="Sign in";
        return text;
    }

    public int getHeader() {
        int id;
        if(manager.isLogIn())
            id = R.id.log_out_header;
        else
            id = R.id.log_out_header;
        return id;
    }

}
