package pl.gda.pg.eti.kask.soundmeterpg;

/**
 * Created by Daniel on 09.07.2016.
 */
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.widget.ImageButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
public class MainActivityToolbarTest {
    private UiDevice mDevice;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setDeviceReadyForTesting(){
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());


    }

    @Test
    public void isAboutDialogShow() {
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_about_dialog)).perform(click());
        onView(withText(R.string.author_about_dialog)).check(matches(isCompletelyDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.author_about_dialog)).check(doesNotExist());
    }

    @Test
    public void isSettingsShow(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_settings)).perform(click());
        onView(withText(R.string.title_recording_audio_preference)).check(matches(isCompletelyDisplayed()));
        mDevice.pressBack();
        onView(withText(R.string.title_recording_audio_preference)).check(doesNotExist());

    }


}

