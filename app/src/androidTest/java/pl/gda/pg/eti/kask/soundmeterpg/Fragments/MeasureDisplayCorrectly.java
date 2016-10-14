package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.LayoutMatchers.hasEllipsizedText;
import static android.support.test.espresso.matcher.LayoutMatchers.hasMultilineText;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by Daniel on 14.10.2016.
 */
@Ignore
@RunWith(AndroidJUnit4.class)
public class MeasureDisplayCorrectly {
    Context context;
    UiDevice device;
    SharedPreferences prefs;



    @Test
    public void isDisplayCorrectlyTest(){
        String text = context.getResources().getString(R.string.longitude_measure);
        isCompletelyDisplay(text);

        text = context.getResources().getString(R.string.latitude_measure);
        isCompletelyDisplay(text);

        text = context.getResources().getString(R.string.min_measure);
        isCompletelyDisplay(text);

        text = context.getResources().getString(R.string.max_measure);
        isCompletelyDisplay(text);

        text = context.getResources().getString(R.string.avg_measure);
        isCompletelyDisplay(text);

        isCompletelyDisplay(onView(withId(R.id.longitude_measure)));
        isCompletelyDisplay(onView(withId(R.id.latitude_measure)));
        isCompletelyDisplay(onView(withId(R.id.min_measure)));
        isCompletelyDisplay(onView(withId(R.id.max_measure)));
        isCompletelyDisplay(onView(withId(R.id.avg_measure)));
        isCompletelyDisplay(onView(withId(R.id.current_db_measure_fragment)));
    }

    public void isCompletelyDisplay(String text) {
        isCompletelyDisplay(onView(withText(text)));
    }

    public void isCompletelyDisplay(ViewInteraction view){
        view.check(matches(isCompletelyDisplayed()));
        view.check(matches(not(hasMultilineText())));
        view.check(matches(not(hasEllipsizedText())));
    }

    @Test
    public void relativeTest(){
        Matcher longitudeText = withId(R.id.longitude_measure);
        Matcher latitudeText = withId(R.id.latitude_measure);
        Matcher minText = withId(R.id.min_measure);
        Matcher maxText = withId(R.id.max_measure);
        Matcher avgText = withId(R.id.avg_measure);
        Matcher currentDb = withId(R.id.current_db_measure_fragment);
        Matcher longitudeTitle = withText(R.string.longitude_measure);
        Matcher latitudeTitle = withText(R.string.latitude_measure);
        Matcher minTitle = withText(R.string.min_measure);
        Matcher maxTitle = withText(R.string.max_measure);
        Matcher avgTitle = withText(R.string.avg_measure);

        onView(latitudeTitle).check(isLeftOf(currentDb));
        onView(currentDb).check(isLeftOf(minTitle));
        onView(minTitle).check(isLeftOf(minText));

        onView(latitudeTitle).check(isAbove(latitudeText));
        onView(latitudeText).check(isAbove(longitudeTitle));
        onView(longitudeTitle).check(isAbove(longitudeText));

        onView(minTitle).check(isAbove(maxTitle));
        onView(maxTitle).check(isAbove(avgTitle));

        onView(minText).check(isAbove(maxText));
        onView(maxText).check(isAbove(avgText));

    }


}
