package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.LoginActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Daniel on 24.07.2016 :) at 12:13 :).
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {
    private Context context;

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public  void initDialog(){
        context = mActivityRule.getActivity().getBaseContext();
        onView(withId(R.id.login_button_login_activity)).perform(click());
    }

    @Test
    public void isIconDisplayedCorrectly() {
        onView(withId(R.id.icon_login_dialog)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isTitleDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.title_login_dialog));
        String text = context.getString(R.string.title_login_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isCookiesDescriptionDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.cookies_warning_login_dialog));
        String text = context.getString(R.string.cookies_information_login_dialog);
        TextViewTestHelper.testMultilineTextView(interaction,text);
    }

    @Test
    public void isHyperlinkDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.cookies_hyperlink_login_dialog));
        String text = context.getString(R.string.cookies_hyperlink_login_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void relativePositionTest() {
        Matcher icon = withId(R.id.icon_login_dialog);
        Matcher title = withId(R.id.title_login_dialog);
        Matcher cookiesDescription = withId(R.id.cookies_warning_login_dialog);
        Matcher hyperlink = withId(R.id.cookies_hyperlink_login_dialog);

        onView(icon).check(isLeftOf(title));
        onView(icon).check(isAbove(cookiesDescription));
        onView(cookiesDescription).check(isLeftOf(hyperlink));
    }
}
