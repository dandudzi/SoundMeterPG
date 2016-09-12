package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.LastDateException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.VersionException;
import pl.gda.pg.eti.kask.soundmeterpg.InformationAboutThisApplication;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Daniel on 06.09.2016 at 13:04 :).
 */
@Ignore
public class AboutDisplayCorrectly {
    Context context;
    Activity activity;

    @Test
    public void isIconDisplayedCorrectly() {
        onView(withId(R.id.icon_about_dialog)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isTitleDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.title_main_about_dialog));
        String text = context.getString(R.string.title_about_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isAuthorDisplayedCorrectly() {
        ViewInteraction interaction = onView(withId(R.id.author_about_dialog));
        String text = context.getString(R.string.author_about_dialog);
        TextViewTestHelper.testMultilineTextView(interaction,text);
    }

    @Test
    public void isContactTitleDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.title_contact_about_dialog));
        String text = context.getString(R.string.contact_us_title_about_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isContactDisplayedCorrectly() {
        onView(withId(R.id.title_version_about_dialog)).perform(scrollTo());
        ViewInteraction interaction = onView(withId(R.id.contact_about_dialog));
        String text = context.getString(R.string.email_contact);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isVersionTitleDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.title_version_about_dialog));
        String text = context.getString(R.string.version_about_title_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isVersionDisplayedCorrectly() throws VersionException {
        ViewInteraction interaction = onView(withId(R.id.version_about_dialog));
        String text = InformationAboutThisApplication.getVersionOfApplication(activity.getBaseContext());
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isLastTitleBuildDisplayedCorrectly(){
        ViewInteraction interaction = onView(withId(R.id.title_last_build_about_dialog));
        String text = context.getString(R.string.last_build_about_title_dialog);
        TextViewTestHelper.testSinglelineTextView(interaction,text);
    }

    @Test
    public void isLastBuildDisplayedCorrectly() throws LastDateException {
        ViewInteraction interaction = onView(withId(R.id.last_build_about_dialog));
        String text = InformationAboutThisApplication.getLastDateBuildApplication(activity.getBaseContext());
        TextViewTestHelper.testSinglelineTextView(interaction,text);
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
