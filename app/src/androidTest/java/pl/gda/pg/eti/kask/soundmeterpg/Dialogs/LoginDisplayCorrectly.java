package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Daniel on 06.09.2016 at 18:42 :).
 */
@Ignore
public class LoginDisplayCorrectly {

    Context context;

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
    public void isHyperlinkDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.cookies_hyperlink_login_dialog));
        String text = context.getString(R.string.cookies_information_login_dialog);
        TextViewTestHelper.testMultilineTextView(interaction,text);
    }

    @Test
    public void relativePositionTest() {
        Matcher icon = withId(R.id.icon_login_dialog);
        Matcher title = withId(R.id.title_login_dialog);
        Matcher hyperlink = withId(R.id.cookies_hyperlink_login_dialog);

        onView(icon).check(isLeftOf(title));
        onView(icon).check(isAbove(hyperlink));
    }
}
