package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.ButtonTestHelper;
import pl.gda.pg.eti.kask.soundmeterpg.EditTextTestHelper;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Daniel on 06.09.2016 at 12:19 :).
 */
@Ignore
public class LoginActivityDisplayCorrectly {
    Context context;

    @Test
    public void isBannerDisplayCorrectly() throws Exception {
       // onView(withId(R.id.banner_login_activity)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isLoginEditTextDisplayCorrectly(){
      //  ViewInteraction view = onView(withId(R.id.login_edit_text_login_activity));
       // int hintId= R.string.login_hint;
       // EditTextTestHelper.testEditTextWithHintIsDisplayCorrectly(view,hintId );
    }

    @Test
    public void isPasswordEditTextDisplayCorrectly(){
      //  ViewInteraction view = onView(withId(R.id.password_edit_text_login_activity));
      //  int hintId = R.string.password_hint;
       // EditTextTestHelper.testEditTextWithHintIsDisplayCorrectly(view,hintId);
    }

    @Test
    public void isRegistrationTextViewDisplayCorrectly(){
       // ViewInteraction view = onView(withId(R.id.registration_text_view_login_activity));
       // String text = context.getString(R.string.registration_text_login_activity);
       // TextViewTestHelper.testSinglelineTextView(view,text);
    }


    @Test
    public void skipButtonDisplayCorrectly(){
//        ViewInteraction view = onView(withId(R.id.skip_button_login_activity));
//        int textId = R.string.skip_button_text_login_activity;
//        ButtonTestHelper.testButtonWithTextDisplayCorrectly(view,textId);
    }

    @Test
    public void loginButtonDisplayCorrectly(){
//        ViewInteraction view = onView(withId(R.id.login_button_login_activity));
//        int textId = R.string.login_button_text;
//        ButtonTestHelper.testButtonWithTextDisplayCorrectly(view,textId);
    }

    @Test
    public void relativeTest(){
//        Matcher banner = withId(R.id.banner_login_activity);
//        Matcher loginEditText = withId(R.id.login_edit_text_login_activity);
//        Matcher passwordEditText = withId(R.id.password_edit_text_login_activity);
//        Matcher skipButton = withId(R.id.skip_button_login_activity);
//        Matcher loginButton = withId(R.id.login_button_login_activity);
//        Matcher registrationTextView = withId(R.id.registration_text_view_login_activity);
//        Matcher errorTextView = withId(R.id.error_message_login_activity);
//
//        onView(banner).check(isAbove(loginEditText));
//        onView(loginEditText).check(isAbove(passwordEditText));
//        onView(passwordEditText).check(isAbove(skipButton));
//        onView(skipButton).check(isLeftOf(loginButton));
//        onView(loginButton).check(isAbove(registrationTextView));
//        onView(registrationTextView).check(isAbove(errorTextView));
    }
}
