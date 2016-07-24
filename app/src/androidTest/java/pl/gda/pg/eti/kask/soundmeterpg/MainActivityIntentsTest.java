package pl.gda.pg.eti.kask.soundmeterpg;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Daniel on 24.07.2016 :).
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityIntentsTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Test
    public void isSettingsIntentSendCorrectly(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_settings)).perform(click());
        intended(allOf(hasComponent(hasClassName(SettingsActivity.class.getName())),toPackage("pl.gda.pg.eti.kask.soundmeterpg")));
    }
}
