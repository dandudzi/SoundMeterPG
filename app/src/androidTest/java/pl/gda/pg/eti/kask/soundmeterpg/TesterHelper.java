package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.v7.widget.AppCompatCheckBox;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.LayoutMatchers.hasEllipsizedText;
import static android.support.test.espresso.matcher.LayoutMatchers.hasMultilineText;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.PreferenceMatchers.withSummary;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Daniel on 22.07.2016 :).
 */
public class TesterHelper {

    public static void testMultilineTextView(ViewInteraction interaction, String text) {
        testTextView(interaction, text);
        interaction.check(matches(hasMultilineText()));
    }

    public static void testSinglelineTextView(ViewInteraction interaction, String text) {
        testTextView(interaction, text);
        interaction.check(matches(not(hasMultilineText())));
    }

    private static void testTextView(ViewInteraction interaction, String text) {
        interaction.perform(scrollTo());
        interaction.check(matches(isCompletelyDisplayed()));
        interaction.check(matches(withText(text)));
        interaction.check(matches(not(hasEllipsizedText())));
    }

    public static void testMultilineTextView(DataInteraction interaction, String text) {
        testTextView(interaction, text);
        interaction.check(matches(hasMultilineText()));
    }

    public static void testSinglelineTextView(DataInteraction interaction, String text) {
        testTextView(interaction, text);
        interaction.check(matches(not(hasMultilineText())));
    }

    private static void testTextView(DataInteraction interaction, String text) {
        interaction.check(matches(isCompletelyDisplayed()));
        interaction.check(matches(withText(text)));
        interaction.check(matches(not(hasEllipsizedText())));
    }

    public static DataInteraction getDataInteractionPreferences(String key, int titleId, int summaryId) {
        return onData(allOf(
                is(instanceOf(Preference.class)),
                withKey(key),
                withTitle(titleId),
                withSummary(summaryId)));
    }

    public static DataInteraction getCheckboxPreferences(DataInteraction interaction) {
        return interaction.onChildView(withId(android.R.id.widget_frame))
                .onChildView(withClassName(is(AppCompatCheckBox.class.getName())));
    }

    public static void isPreferenceChecked(int keyId, Context context) {
        DataInteraction gps = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckboxPreferences(gps);
        checkBox.check(matches(isChecked()));
    }

    public static void isPreferenceNotChecked(int keyId, Context context) {
        DataInteraction gps = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckboxPreferences(gps);
        checkBox.check(matches(not(isChecked())));
    }


    public static void isPreferenceEnabled(int keyId, Context context) {
        DataInteraction gps = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckboxPreferences(gps);
        checkBox.check(matches(isEnabled()));
    }

    public static void isPreferenceNotEnabled(int keyId, Context context) {
        DataInteraction gps = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckboxPreferences(gps);
        checkBox.check(matches(not(isEnabled())));
    }

    public static void selectPreference(int keyId, SharedPreferences prefs, Context context) {
        DataInteraction interaction = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckboxPreferences(interaction);
        Boolean isChecked = prefs.getBoolean(context.getString(keyId), true);
        if (!isChecked)
            checkBox.check(matches(not(isChecked()))).perform(click());
    }

    public static void uncheckPreference(int keyId, SharedPreferences prefs, Context context) {
        DataInteraction interaction = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckboxPreferences(interaction);
        Boolean isChecked = prefs.getBoolean(context.getString(keyId), true);
        if (isChecked)
            checkBox.check(matches(isChecked())).perform(click());
    }

    private static DataInteraction getDataInteraction(int keyId, Context context) {
        int titleId;
        int summaryId;
        String key = context.getString(keyId);
        switch (keyId) {
            case R.string.key_private_data_preference:
                titleId = R.string.title_private_data_preference;
                summaryId = R.string.summary_private_data_preference;
                break;
            case R.string.key_working_in_background_preference:
                titleId = R.string.title_working_in_background_preference;
                summaryId = R.string.summary_working_in_background_preference;
                break;
            case R.string.key_recording_audio_preference:
                titleId = R.string.title_recording_audio_preference;
                summaryId = R.string.summary_recording_audio_preference;
                break;
            case R.string.key_gps_preference:
                titleId = R.string.title_gps_preference;
                summaryId = R.string.summary_gps_preference;
                break;
            case R.string.key_internet_preference:
                titleId = R.string.title_internet_preference;
                summaryId = R.string.summary_internet_preference;
                break;
            case R.string.key_internal_storage_preference:
                titleId = R.string.title_internal_storage_preference;
                summaryId = R.string.summary_internal_storage_preference;
                break;
            default:
                titleId = 0;
                summaryId = 0;
                break;
        }

        return getDataInteractionPreferences(key, titleId, summaryId);
    }


}
