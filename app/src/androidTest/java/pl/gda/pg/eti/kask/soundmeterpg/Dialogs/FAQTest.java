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

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

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
 * Created by Daniel on 24.07.2016 :) at 12:13 :).
 */
@RunWith(AndroidJUnit4.class)
public class FAQTest {
    private Context context;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
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
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isDescriptionDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.description_faq_dialog));
        String text = context.getString(R.string.application_description_faq_dialog);
        TextViewTestHelper.testMultilineTextView(interaction,text);
    }

    @Test
    public void isGitHubDescriptionDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.github_description_text_view_faq_dialog));
        String text = context.getString(R.string.github_description_faq_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isGitHubDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.github_hyperlink_text_view_faq_dialog));
        String text = context.getString(R.string.github_link_faq_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isLicenceDescriptionDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.licence_description_text_view_faq_dialog));
        String text = context.getString(R.string.licence_description_faq_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isLicenceDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.licence_hyperlink_text_view_faq_dialog));
        String text = context.getString(R.string.licence_link_faq_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isWebPageDescriptionDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.soundmeterpg_description_text_view_faq_dialog));
        String text = context.getString(R.string.website_description_faq_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isWebPageDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.soundmeterpg_hyperlink_text_view_faq_dialog));
        String text = context.getString(R.string.website_link_faq_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isHelpDescriptionDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.help_description_text_view_faq_dialog));
        String text = context.getString(R.string.help_description_faq_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isHelpDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.help_hyperlink_text_view_faq_dialog));
        String text = context.getString(R.string.help_link_faq_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void relativePositionTest(){
        Matcher icon = withId(R.id.icon_faq_dialog);
        Matcher title = withId(R.id.title_main_faq_dialog);
        Matcher description = withId(R.id.description_faq_dialog);
        Matcher githubDescription = withId(R.id.github_description_text_view_faq_dialog);
        Matcher github = withId(R.id.github_hyperlink_text_view_faq_dialog);
        Matcher licenceDescription = withId(R.id.licence_description_text_view_faq_dialog);
        Matcher licence = withId(R.id.licence_hyperlink_text_view_faq_dialog);
        Matcher soundmeterpgDescription = withId(R.id.soundmeterpg_description_text_view_faq_dialog);
        Matcher soundmeterpg = withId(R.id.soundmeterpg_hyperlink_text_view_faq_dialog);
        Matcher helpDescription = withId(R.id.help_description_text_view_faq_dialog);
        Matcher help = withId(R.id.help_hyperlink_text_view_faq_dialog);

        onView(icon).check(isLeftOf(title));
        onView(icon).check(isAbove(description));
        onView(description).check(isAbove(githubDescription));
        onView(githubDescription).check(isLeftOf(github));
        onView(githubDescription).check(isAbove(licenceDescription));
        onView(licenceDescription).check(isLeftOf(licence));
        onView(licenceDescription).check(isAbove(soundmeterpgDescription));
        onView(soundmeterpgDescription).check(isLeftOf(soundmeterpg));
        onView(soundmeterpgDescription).check(isAbove(helpDescription));
        onView(helpDescription).check(isLeftOf(help));
    }

}
