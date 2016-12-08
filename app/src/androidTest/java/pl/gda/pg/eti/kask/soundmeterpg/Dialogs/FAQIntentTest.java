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
import pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.openLinkWithUri;
import static android.support.test.espresso.action.ViewActions.scrollTo;
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
    public void setUp(){
        context = mActivityRule.getActivity().getBaseContext();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
    }

    @Test
    public void isIntentAnswer5(){
        textViewIntentTest(R.id.answer5,"https://github.com/dandudzi/SoundMeterPG/blob/master/licence");
    }


    @Test
    public void isIntentAnswer6(){
        textViewIntentTest(R.id.answer6,"https://soundmeterpg.pl/web/register_web");
    }


    @Test
    public void isIntentAnswer7(){
        textViewIntentTest(R.id.answer7,"https://github.com/dandudzi/SoundMeterPG/");
    }


    @Test
    public void isIntentAnswer8(){
        textViewIntentTest(R.id.answer8,"http://soundmeterpg.pl");
    }



    private void textViewIntentTest( int hyperlinkID, String urlID) {
        UiObject somethingInView = device.findObject(new UiSelector().resourceId("R.id.icon_faq_dialog"));

        onView(withId(hyperlinkID)).perform(scrollTo(), openLinkWithUri(urlID));
        intended(allOf(hasData(urlID)));
        somethingInView.waitUntilGone(UIAutomotorTestHelper.TIME_OUT);
        device.pressBack();
    }
}
