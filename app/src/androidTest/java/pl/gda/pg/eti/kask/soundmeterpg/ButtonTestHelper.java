package pl.gda.pg.eti.kask.soundmeterpg;

import android.support.test.espresso.ViewInteraction;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Daniel on 21.08.2016 at 15:59 :).
 */
public class ButtonTestHelper {

    public static void testButtonWithTextDisplayCorrectly(ViewInteraction buttonView, int textId){
        buttonView.check(matches(isCompletelyDisplayed()));
        buttonView.check(matches(withText(textId)));
    }

}
