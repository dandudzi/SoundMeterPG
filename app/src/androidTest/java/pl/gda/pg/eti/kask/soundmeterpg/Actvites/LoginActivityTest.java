package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.EditTextTestHelper;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.MyAccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Daniel on 22.08.2016 at 16:50 :).
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private Context context;
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initSettings() {
        context = mActivityRule.getActivity().getBaseContext();
        openDrawer(R.id.drawer_layout);
        onView(withText("Log in")).perform(click());
    }

    @Test
    public void loginEditTextWorksCorrectly(){
        String loginText = "NadalPodkreslaGierlowski";
        ViewInteraction login = onView(withId(R.id.login_edit_text_login_activity));

        EditTextTestHelper.typingTextOnEditTextTest(loginText, login);
    }

    @Test
    public void passwordEditTextWorksCorrectly(){
        String password = "DajMiCos1234!@#$";
        ViewInteraction login = onView(withId(R.id.password_edit_text_login_activity));

        EditTextTestHelper.typingTextOnEditTextTest(password, login);
    }

    @Test
    public void skipTest(){
        pressButton(withId(R.id.skip_button_login_activity));
        onView(withText(R.string.app_name)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void cancelLoginTest(){
        pressButton(withId(R.id.login_button_login_activity));

        pressButton(withId(android.R.id.button2));

        onView(withId(R.id.login_button_login_activity)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void inCorrectLoginTest(){
        onView(withId(R.id.login_edit_text_login_activity)).perform(typeText("daj"));
        onView(withId(R.id.password_edit_text_login_activity)).perform(typeText("daj"));
        pressButton(withId(R.id.login_button_login_activity));

        pressButton(withId(android.R.id.button1));

        ViewInteraction errorTextView = onView(withId(R.id.error_message_login_activity));
        String errorText = context.getString(R.string.incorrect_credentials_login_activity);
        TextViewTestHelper.testSinglelineTextView(errorTextView,errorText);
    }

    @Test
    public void timeOutLoginTest(){
        pressButton(withId(R.id.login_button_login_activity));

        pressButton(withId(android.R.id.button1));

        ViewInteraction errorTextView = onView(withId(R.id.error_message_login_activity));
        String errorText = context.getString(R.string.timeout_error_message_login_activity);
        TextViewTestHelper.testSinglelineTextView(errorTextView,errorText);
    }


    @Test
    public void correctLoginTest(){
      //  onView(withId(R.id.login_edit_text_login_activity)).perform(typeText(MyAccountManager.login));
       // onView(withId(R.id.password_edit_text_login_activity)).perform(typeText(MyAccountManager.password));

      //  pressButton(withId(R.id.login_button_login_activity));

        //pressButton(withId(android.R.id.button1));

       // onView(withText(R.string.app_name)).check(matches(isCompletelyDisplayed()));
    }


    public void pressButton(Matcher<View> viewMatcher) {
        ViewInteraction loginButton = onView(viewMatcher);
        loginButton.check(matches(isCompletelyDisplayed()));
        loginButton.perform(click());
    }


}
