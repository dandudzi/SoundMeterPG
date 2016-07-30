package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

/**
 * Created by Daniel on 09.07.2016.
 */
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityToolbarTest {
    private UiDevice device;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setDeviceReadyForTesting(){
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void isAboutDialogShow() {
        openContextualActionModeOverflowMenu();
        onView(ViewMatchers.withText(R.string.title_about_dialog)).perform(click());
        onView(withText(R.string.author_about_dialog)).check(matches(isCompletelyDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.author_about_dialog)).check(doesNotExist());
    }

    @Test
    public void isSettingsShow(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_settings)).perform(click());
        onView(withText(R.string.title_recording_audio_preference)).check(matches(isCompletelyDisplayed()));
        device.pressBack();
        onView(withText(R.string.title_recording_audio_preference)).check(doesNotExist());

    }

    @Test
    public void isFAQShow(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
        onView(withText(R.string.application_description_faq_dialog)).check(matches(isCompletelyDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.application_description_faq_dialog)).check(doesNotExist());

    }


}

