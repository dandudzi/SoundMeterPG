package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

/**
 * Created by Daniel on 09.07.2016 at 12:12 :).
 */
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SettingsTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.closeDrawer;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class MainActivityToolbarTest {
    private UiDevice device;
    private Context context;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp(){
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        context = mActivityRule.getActivity().getBaseContext();
    }

    @Test
    public void isNavigationDrawerShowsCorrectly(){
        String firstRow = context.getResources().getStringArray(R.array.rows_list_drawer)[1];
        openDrawer(R.id.drawer_layout);
        onView(withText(firstRow)).check(matches(isCompletelyDisplayed()));
        closeDrawer(R.id.drawer_layout);
        onView(withText(firstRow)).check(matches(not(isDisplayed())));

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
        onView(withId(android.R.id.button1)).perform(click());
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
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).check(matches(isCompletelyDisplayed()));
        onView(withText(R.string.title_faq_dialog)).perform(click());
        onView(withText(R.string.application_description_faq_dialog)).check(matches(isCompletelyDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.application_description_faq_dialog)).check(doesNotExist());

    }


}

