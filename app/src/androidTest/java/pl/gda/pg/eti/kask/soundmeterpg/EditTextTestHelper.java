package pl.gda.pg.eti.kask.soundmeterpg;

import android.support.test.espresso.ViewInteraction;

import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by Daniel on 21.08.2016 at 15:48 :).
 */
public class EditTextTestHelper {

    public static void testEditTextWithHintIsDisplayCorrectly(ViewInteraction editTextView, int hintID) {
        editTextView.check(matches(isCompletelyDisplayed()));
        editTextView.check(matches(withHint(hintID)));
    }

    public static void typingTextOnEditTextTest(String textToType, ViewInteraction editText) {
        String emptyText = "";
        editText.check(matches(withText(emptyText)));
        editText.perform(typeText(textToType));
        editText.check(matches(not(withText(emptyText))));
        editText.check(matches(withText(textToType)));
    }
}
