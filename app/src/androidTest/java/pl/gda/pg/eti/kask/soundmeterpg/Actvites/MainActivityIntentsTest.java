package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.content.Context;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Daniel on 24.07.2016 :).
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityIntentsTest {
    Context context;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void init(){
        context = mActivityRule.getActivity().getBaseContext();
    }

    @Test
    public void isSettingsIntentSendCorrectly(){
        openContextualActionModeOverflowMenu();
        onView(ViewMatchers.withText(R.string.title_settings)).perform(click());
        intended(allOf(hasComponent(hasClassName(SettingsActivity.class.getName())),toPackage("pl.gda.pg.eti.kask.soundmeterpg")));
    }

    @Test
    public void isFAQIntentGitHubSendCorrectly(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
        onView(withId(R.id.github_hyperlink_text_view_faq_dialog)).perform(click());
        String data = context.getString(R.string.github_url);
        intended(allOf(hasData(data)));
    }

    @Test
    public void isFAQIntentLicenceSendCorrectly(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
        onView(withId(R.id.licence_hyperlink_text_view_faq_dialog)).perform(click());
        String data = context.getString(R.string.licence_url);
        intended(allOf(hasData(data)));
    }


    @Test
    public void isFAQIntentSoundMeterPGSendCorrectly(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
        onView(withId(R.id.soundmeterpg_hyperlink_text_view_faq_dialog)).perform(click());
        String data = context.getString(R.string.soundmeterpg_url);
        intended(allOf(hasData(data)));
    }


    @Test
    public void isFAQIntentHelpSendCorrectly(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
        onView(withId(R.id.help_hyperlink_text_view_faq_dialog)).perform(click());
        String data = context.getString(R.string.help_url);
        intended(allOf(hasData(data)));
    }
}
