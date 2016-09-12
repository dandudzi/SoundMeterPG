package pl.gda.pg.eti.kask.soundmeterpg.Drawer;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.LoginActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Daniel on 11.08.2016 at 17:32 :).
 */
@RunWith(AndroidJUnit4.class)
public class NavigationDrawerIntentTest {

    @Rule
    public final IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Test
    public void isSettingsIntentSendCorrectly(){
        openDrawer(R.id.drawer_layout);
        onView(withText("Settings")).perform(click());
        intended(allOf(hasComponent(hasClassName(SettingsActivity.class.getName())),toPackage("pl.gda.pg.eti.kask.soundmeterpg")));
    }

    @Test
    public void isLogInIntentSendCorrectly(){
        openDrawer(R.id.drawer_layout);
        onView(withText("Log in")).perform(click());
        intended(allOf(hasComponent(hasClassName(LoginActivity.class.getName())),toPackage("pl.gda.pg.eti.kask.soundmeterpg")));
    }

}