package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Daniel on 24.07.2016 :).
 */
@RunWith(AndroidJUnit4.class)
public class FAQDialogTest {
    private Context context;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public  void initDialog(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
        context = mActivityRule.getActivity().getBaseContext();
    }

    @Test
    public void isIconDisplayedCorrectly() {
        onView(withId(R.id.icon_faq_dialog)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isTitleDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.title_main_faq_dialog));
        String text = context.getString(R.string.title_faq_dialog);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isDescriptionDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.description_faq_dialog));
        String text = context.getString(R.string.application_description_faq_dialog);
        TesterHelper.testMultilineTextView(interaction,text);
    }

    @Test
    public void isGitHubDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.github_hyperlink_text_view_faq_dialog));
        String text = context.getString(R.string.github_link);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isLicenceDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.licence_hyperlink_text_view_faq_dialog));
        String text = context.getString(R.string.licence_link);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isWebPageDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.soundmeterpg_hyperlink_text_view_faq_dialog));
        String text = context.getString(R.string.soundmeterpg_link);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isHelpDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.help_hyperlink_text_view_faq_dialog));
        String text = context.getString(R.string.help_link);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void testRelativePosition(){
        Matcher icon = withId(R.id.icon_faq_dialog);
        Matcher title = withId(R.id.title_main_faq_dialog);
        Matcher description = withId(R.id.description_faq_dialog);
        Matcher github = withId(R.id.github_hyperlink_text_view_faq_dialog);
        Matcher licence = withId(R.id.licence_hyperlink_text_view_faq_dialog);
        Matcher soundmeterpg = withId(R.id.soundmeterpg_hyperlink_text_view_faq_dialog);
        Matcher help = withId(R.id.help_hyperlink_text_view_faq_dialog);

        onView(icon).check(isLeftOf(title));
        onView(icon).check(isAbove(description));
        onView(description).check(isAbove(github));
        onView(github).check(isAbove(licence));
        onView(licence).check(isAbove(soundmeterpg));
        onView(soundmeterpg).check(isAbove(help));
    }

}
