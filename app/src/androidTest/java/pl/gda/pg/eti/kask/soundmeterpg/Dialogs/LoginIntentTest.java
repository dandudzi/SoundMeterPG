package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.LoginActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Created by Daniel on 21.08.2016 at 16:23 :).
 */
@RunWith(AndroidJUnit4.class)
public class LoginIntentTest {
    private Context context;
    private UiDevice device;

    @Rule
    public final IntentsTestRule<LoginActivity> mActivityRule = new IntentsTestRule<>(
            LoginActivity.class);

    @Before
    public void initValues(){
        context = mActivityRule.getActivity().getBaseContext();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void isCookiesIntentSendCorrectly(){
        onView(withId(R.id.login_button_login_activity)).perform(click());
        String data = context.getString(R.string.cookies_link_login_dialog);
        onView(withText(containsString(context.getString(R.string.cookies_hyperlink_login_dialog)))).perform(click());
        intended(allOf(hasData(data)));
        try{
            Thread.sleep(2000);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        device.pressBack();
    }
}
