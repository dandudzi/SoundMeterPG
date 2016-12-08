package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.LoginActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static org.hamcrest.Matchers.allOf;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOffGPS;

/**
 * Created by Daniel on 21.08.2016 at 16:16 :).
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityIntentTest {
    private Context context;
    private UiDevice device;

    @Rule
    public final IntentsTestRule<LoginActivity> mActivityRule = new IntentsTestRule<>(
            LoginActivity.class);

    @Before
    public void setUp(){
        context = mActivityRule.getActivity().getBaseContext();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    }

    @Test
    public void isRegistrationIntentSendCorrectly() throws UiObjectNotFoundException {
//        UiObject button = device.findObject(new UiSelector().text(context.getString(R.string.registration_text_login_activity)));
//        button.click();
//        String data = context.getString(R.string.registration_link_login_activity);
//        intended(allOf(hasData(data)));
//        button.waitUntilGone(UIAutomotorTestHelper.TIME_OUT);
//        device.pressBack();
    }
}
