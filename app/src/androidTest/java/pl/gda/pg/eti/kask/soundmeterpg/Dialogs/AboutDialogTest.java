package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.LastDateException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.VersionException;
import pl.gda.pg.eti.kask.soundmeterpg.FactorAlertDialog;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TesterHelper;

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
 * Created by Daniel on 10.07.2016.
 */



@RunWith(AndroidJUnit4.class)
public class AboutDialogTest {
    private Context context;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public  void initDialog(){
        openContextualActionModeOverflowMenu();
        onView(ViewMatchers.withText(R.string.title_about_dialog)).perform(click());
        context = mActivityRule.getActivity().getBaseContext();
    }

    @Test
    public void isIconDisplayedCorrectly() {
        onView(withId(R.id.icon_about_dialog)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isTitleDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.title_main_about_dialog));
        String text = context.getString(R.string.title_about_dialog);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isAuthorDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.author_about_dialog));
        String text = context.getString(R.string.author_about_dialog);
        TesterHelper.testMultilineTextView(interaction,text);
    }

    @Test
    public void isContactTitleDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.title_contact_about_dialog));
        String text = context.getString(R.string.title_contact_us_about_dialog);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isContactDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.contact_about_dialog));
        String text = context.getString(R.string.email_contact);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isVersionTitleDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.title_version_about_dialog));
        String text = context.getString(R.string.title_version_about_dialog);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isVersionDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.version_about_dialog));
        String text = null;
        try {
            text = FactorAlertDialog.getVersionOfApplication(mActivityRule.getActivity().getBaseContext());
        } catch (VersionException e) {
            e.printStackTrace();
        }
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isLastTitleBuildDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.title_last_build_about_dialog));
        String text = context.getString(R.string.title_last_build_about_dialog);
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isLastBuildDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.last_build_about_dialog));
        String text = null;
        try {
            text = FactorAlertDialog.getLastDateBuildApplication(mActivityRule.getActivity().getBaseContext());
        } catch (LastDateException e) {
            e.printStackTrace();
        }
        TesterHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void testRelativePosition(){
        Matcher icon = withId(R.id.icon_about_dialog);
        Matcher title = withId(R.id.title_main_about_dialog);
        Matcher author = withId(R.id.author_about_dialog);
        Matcher contactTitle = withId(R.id.title_contact_about_dialog);
        Matcher contact = withId(R.id.contact_about_dialog);
        Matcher versionTitle = withId(R.id.title_version_about_dialog);
        Matcher version = withId(R.id.version_about_dialog);
        Matcher lastBuildTitle = withId(R.id.title_last_build_about_dialog);
        Matcher lastBuild = withId(R.id.last_build_about_dialog);

        onView(icon).check(isLeftOf(title));
        onView(icon).check(isAbove(author));
        onView(author).check(isAbove(contactTitle));
        onView(contactTitle).check(isAbove(contact));
        onView(contact).check(isAbove(versionTitle));
        onView(versionTitle).check(isAbove(version));
        onView(version).check(isAbove(lastBuildTitle));
        onView(lastBuildTitle).check(isAbove(lastBuild));
    }

}