package pl.gda.pg.eti.kask.soundmeterpg;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.LayoutMatchers.hasEllipsizedText;
import static android.support.test.espresso.matcher.LayoutMatchers.hasMultilineText;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by Daniel on 22.07.2016 :).
 */
public class TesterHelper {

    public static void testMultilineTextView(ViewInteraction interaction, String text){
        testTextView(interaction,text);
        interaction.check(matches(hasMultilineText()));
    }

    public static void testSinglelineTextView(ViewInteraction interaction, String text){
        testTextView(interaction,text);
        interaction.check(matches(not(hasMultilineText())));
    }

    private static void testTextView(ViewInteraction interaction, String text){
        interaction.perform(scrollTo());
        interaction.check(matches(isCompletelyDisplayed()));
        interaction.check(matches(withText(text)));
        interaction.check(matches(not(hasEllipsizedText())));
    }

    public static void testMultilineTextView(DataInteraction interaction, String text){
        testTextView(interaction,text);
        interaction.check(matches(hasMultilineText()));
    }

    public static void testSinglelineTextView(DataInteraction interaction, String text){
        testTextView(interaction,text);
        interaction.check(matches(not(hasMultilineText())));
    }

    private static void testTextView(DataInteraction interaction, String text){
        interaction.check(matches(isCompletelyDisplayed()));
        interaction.check(matches(withText(text)));
        interaction.check(matches(not(hasEllipsizedText())));
    }
}
