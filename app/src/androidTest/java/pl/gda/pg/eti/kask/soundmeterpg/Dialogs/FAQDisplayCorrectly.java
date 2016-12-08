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
 * Created by Daniel on 06.09.2016 at 17:53 :).
 */
@Ignore
public class FAQDisplayCorrectly {
    Context context;

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
        ViewInteraction interaction = onView(withId(R.id.title_faq));
        String text = context.getString(R.string.application_description_faq_dialog);
        TextViewTestHelper.testMultilineTextView(interaction,text);
    }

    @Test
    public void isGitHubDisplayedCorrectly() {
      //  ViewInteraction interaction = onView(withId(R.id.g));
      //  String text = context.getString(R.string.);
      //  TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isLicenceDisplayedCorrectly(){
      //  ViewInteraction interaction = onView(withId(R.id.licence_hyperlink_text_view_faq_dialog));
     //   String text = context.getString(R.string.licence_link_faq_dialog);
     //   TextViewTestHelper.testSinglelineTextView(interaction,text);
    }


    @Test
    public void isWebPageDisplayedCorrectly(){
     //   ViewInteraction interaction = onView(withId(R.id.soundmeterpg_hyperlink_text_view_faq_dialog));
      //  String text = context.getString(R.string.website_link_faq_dialog);
      //  TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isHelpDisplayedCorrectly() {
       // ViewInteraction interaction = onView(withId(R.id.help_hyperlink_text_view_faq_dialog));
      //  String text = context.getString(R.string.help_link_faq_dialog);
       // TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void relativePositionTest(){
      //  Matcher icon = withId(R.id.icon_faq_dialog);
     //   Matcher title = withId(R.id.title_main_faq_dialog);
     //   Matcher description = withId(R.id.description_faq_dialog);
     //   Matcher github = withId(R.id.github_hyperlink_text_view_faq_dialog);
     //   Matcher licence = withId(R.id.licence_hyperlink_text_view_faq_dialog);
      //  Matcher soundmeterpg = withId(R.id.soundmeterpg_hyperlink_text_view_faq_dialog);
      //  Matcher help = withId(R.id.help_hyperlink_text_view_faq_dialog);

       // onView(icon).check(isLeftOf(title));
       // onView(icon).check(isAbove(description));
       // onView(description).check(isAbove(github));
       // onView(github).check(isAbove(licence));
       // onView(licence).check(isAbove(soundmeterpg));
       // onView(soundmeterpg).check(isAbove(help));
    }

}
