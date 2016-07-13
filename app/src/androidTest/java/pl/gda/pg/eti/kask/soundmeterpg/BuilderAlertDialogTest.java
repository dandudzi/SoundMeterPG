package pl.gda.pg.eti.kask.soundmeterpg;

/**
 * Created by Daniel on 10.07.2016.
 */
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class BuilderAlertDialogTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public  void initDialog(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_about_dialog)).perform(click());
    }

    @Test
    public void isIconDisplayedCorrectly() {
        onView(withId(R.id.icon_about_dialog)).check(matches(isDisplayed()));
    }

    @Test
    public void isTitleDisplayedCorrectly() {
        onView(withId(R.id.title_main_about_dialog)).check(matches(isDisplayed()));
        onView(withId(R.id.title_main_about_dialog)).check(matches(withText(R.string.title_about_dialog)));
    }

    @Test
    public void isAuthorDisplayedCorrectly() {
        onView(withId(R.id.author_abouta_dialog)).perform(scrollTo());
        onView(withId(R.id.author_abouta_dialog)).check(matches(isDisplayed()));
        onView(withId(R.id.author_abouta_dialog)).check(matches(withText(R.string.author_about_dialog)));
    }

    @Test
    public void isContactDisplayedCorrectly() {
        onView(withId(R.id.contact_abouta_dialog)).perform(scrollTo());
        onView(withId(R.id.title_contact_abouta_dialog)).check(matches(isDisplayed()));
        onView(withId(R.id.contact_abouta_dialog)).check(matches(isDisplayed()));
        onView(withId(R.id.contact_abouta_dialog)).check(matches(withText(R.string.email_contact)));
    }

    @Test
    public void isVersionDisplayedCorrectly() {
        onView(withId(R.id.version_abouta_dialog)).perform(scrollTo());
        onView(withId(R.id.title_version_abouta_dialog)).check(matches(isDisplayed()));
        onView(withId(R.id.version_abouta_dialog)).check(matches(isDisplayed()));
    }

    @Test
    public void isLastBuildDisplayedCorrectly() {
        //onView(withId(R.id.last_build_abouta_dialog)).check(matches())
        onView(withId(R.id.last_build_abouta_dialog)).perform(scrollTo());
        onView(withId(R.id.title_last_build_abouta_dialog)).check(matches(isDisplayed()));
        onView(withId(R.id.last_build_abouta_dialog)).check(matches(isDisplayed()));
    }

}