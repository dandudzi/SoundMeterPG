package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        TextViewTestHelper.testMultilineTextView(interaction,text);
    }


    @Test
    public void isAnswerDescriptionDsiplayCorrectly1() {
        onView(withId(R.id.answer1)).perform(scrollTo());
        onView(withId(R.id.question_1)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.answer1)).check(matches(isCompletelyDisplayed()));
    }
    @Test
    public void isAnswerDescriptionDsiplayCorrectly2() {
        onView(withId(R.id.answer2)).perform(scrollTo());
        onView(withId(R.id.question_2)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.answer2)).check(matches(isCompletelyDisplayed()));
    }
    @Test
    public void isAnswerDescriptionDsiplayCorrectly3() {
        onView(withId(R.id.answer3)).perform(scrollTo());
        onView(withId(R.id.question_3)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.answer3)).check(matches(isCompletelyDisplayed()));
    }
    @Test
    public void isAnswerDescriptionDsiplayCorrectly4() {
        onView(withId(R.id.answer4)).perform(scrollTo());
        onView(withId(R.id.question_4)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.answer4)).check(matches(isCompletelyDisplayed()));
    }
    @Test
    public void isAnswerDescriptionDsiplayCorrectly5() {
        onView(withId(R.id.answer5)).perform(scrollTo());
        onView(withId(R.id.question_5)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.answer5)).check(matches(isCompletelyDisplayed()));
    }
    @Test
    public void isAnswerDescriptionDsiplayCorrectly6() {
        onView(withId(R.id.answer6)).perform(scrollTo());
        onView(withId(R.id.question_6)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.answer6)).check(matches(isCompletelyDisplayed()));
    }
    @Test
    public void isAnswerDescriptionDsiplayCorrectly7() {
        onView(withId(R.id.answer7)).perform(scrollTo());
        onView(withId(R.id.question_7)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.answer7)).check(matches(isCompletelyDisplayed()));
    }
    @Test
    public void isAnswerDescriptionDsiplayCorrectly8() {
        onView(withId(R.id.answer8)).perform(scrollTo());
        onView(withId(R.id.question_8)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.answer8)).check(matches(isCompletelyDisplayed()));
    }




    @Test
    public void relativePositionTest(){
        Matcher icon = withId(R.id.icon_faq_dialog);
        Matcher title = withId(R.id.title_main_faq_dialog);
        Matcher question1 = withId(R.id.question_1);
        Matcher question2 = withId(R.id.question_2);
        Matcher question3 = withId(R.id.question_3);
        Matcher question4 = withId(R.id.question_4);
        Matcher question5 = withId(R.id.question_5);
        Matcher question6 = withId(R.id.question_6);
        Matcher question7 = withId(R.id.question_7);
        Matcher question8 = withId(R.id.question_8);
        Matcher answer1 = withId(R.id.answer1);
        Matcher answer2 = withId(R.id.answer2);
        Matcher answer3 = withId(R.id.answer3);
        Matcher answer4 = withId(R.id.answer4);
        Matcher answer5 = withId(R.id.answer5);
        Matcher answer6 = withId(R.id.answer6);
        Matcher answer7 = withId(R.id.answer7);
        Matcher answer8 = withId(R.id.answer8);


     //   Matcher github = withId(R.id.github_hyperlink_text_view_faq_dialog);
     //   Matcher licence = withId(R.id.licence_hyperlink_text_view_faq_dialog);
      //  Matcher soundmeterpg = withId(R.id.soundmeterpg_hyperlink_text_view_faq_dialog);
      //  Matcher help = withId(R.id.help_hyperlink_text_view_faq_dialog);

        onView(icon).check(isLeftOf(title));
        onView(icon).check(isAbove(question1));
        onView(question1).check(isAbove(answer1));
        onView(question1).check(isAbove(question2));
        onView(question2).check(isAbove(answer2));
        onView(question2).check(isAbove(question3));
        onView(question3).check(isAbove(answer3));
        onView(question3).check(isAbove(question4));
        onView(question4).check(isAbove(answer4));
        onView(question4).check(isAbove(question5));
        onView(question5).check(isAbove(answer5));
        onView(question5).check(isAbove(question6));
        onView(question6).check(isAbove(answer6));
        onView(question6).check(isAbove(question7));
        onView(question7).check(isAbove(answer7));
        onView(question7).check(isAbove(question8));
        onView(question8).check(isAbove(answer8));
    }

}
