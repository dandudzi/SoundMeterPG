package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.content.Context;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.uiautomator.UiDevice;

import org.junit.Ignore;
import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SettingsTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by Daniel on 06.09.2016 at 12:29 :).
 */
@Ignore
public class MainActivityToolbar {
    Context context;
    UiDevice device;



    @Test
    public void isNavigationDrawerOpenCorrectly(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.left_drawer)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
        onView(withId(R.id.left_drawer)).check(matches(not(isDisplayed())));
    }

    @Test
    public void isToolbarShowsCorrectly(){
        onView(withText(R.string.app_name)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isAboutDialogShowsCorrectly() {
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_about_dialog)).check(matches(isCompletelyDisplayed()));
        onView(withText(R.string.title_about_dialog)).perform(click());
        onView(withText(R.string.author_about_dialog)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.ok_btn)).perform(scrollTo());
        onView(withId(R.id.ok_btn)).perform(click());
        onView(withText(R.string.author_about_dialog)).check(doesNotExist());
    }

    @Test
    public void isSettingsShowsCorrectly(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_settings)).check(matches(isCompletelyDisplayed()));
        onView(withText(R.string.title_settings)).perform(click());
        SettingsTestHelper.isSettingDisplay();
        SettingsTestHelper.backFromSettings(device);

    }


    @Test
    public void isFAQShowsCorrectly(){
//        openContextualActionModeOverflowMenu();
//        onView(withText(R.string.title_faq_dialog)).check(matches(isCompletelyDisplayed()));
//        onView(withText(R.string.title_faq_dialog)).perform(click());
//        onView(withText(R.string.application_description_faq_dialog)).check(matches(isCompletelyDisplayed()));
//        onView(withId(android.R.id.button1)).perform(click());
//        onView(withText(R.string.application_description_faq_dialog)).check(doesNotExist());

    }
}
