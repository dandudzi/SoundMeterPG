package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Daniel on 03.08.2016 :) at 12:13 :).
 */
@RunWith(AndroidJUnit4.class)
public class FAQIntentTest {

    private Context context;
    private UiDevice device;

    @Rule
    public final IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void initValues(){
        context = mActivityRule.getActivity().getBaseContext();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    @Test
    public void isIntentGitHubSendCorrectly(){
        textViewIntentTest(R.id.github_hyperlink_text_view_faq_dialog,R.string.github_url_faq_dialog);
    }



    @Test
    public void isIntentLicenceSendCorrectly(){
        textViewIntentTest(R.id.licence_hyperlink_text_view_faq_dialog,R.string.licence_url_faq_dialog);
    }


    @Test
    public void isIntentSoundMeterPGSendCorrectly(){
        textViewIntentTest(R.id.soundmeterpg_hyperlink_text_view_faq_dialog,R.string.website_url_faq_dialog);
    }


    @Test
    public void isIntentHelpSendCorrectly(){
        textViewIntentTest(R.id.help_hyperlink_text_view_faq_dialog,R.string.help_url_faq_dialog);
    }

    private void textViewIntentTest( int hyperlinkID, int urlID) {
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
        UiObject somethingInView = device.findObject(new UiSelector().resourceId("R.id.icon_faq_dialog"));
        String data = context.getString(urlID);

        onView(withId(hyperlinkID)).perform(click());
        intended(allOf(hasData(data)));
        somethingInView.waitUntilGone(2000);
        device.pressBack();
    }
}
